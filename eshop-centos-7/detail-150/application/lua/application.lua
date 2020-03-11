local cjson = require("cjson")
local http = require("resty.http")
local redis = require("resty.redis")

local function close_redis(red)
    if not red then return end
    local pool_max_idle_time = 10000
    local pool_size = 100
    local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)
    if not ok then ngx.say("set keepalive error : ", err) end
end

local uri_args = ngx.req.get_uri_args()
local productId = uri_args["productId"]

local cache_ngx = ngx.shared.my_cache
local productCacheKey = "product_" .. productId
local productCache = cache_ngx:get(productCacheKey)

if productCache == "" or productCache == nil then
    local datalinkFailureCntKey = "datalinkFailureCnt"
    local datalinkDegradeKey = "datalinkDegrade"
    local datalinkDegradeTimeKey = "datalinkDegradeTime"

    local slaveRedisDegradeKey = "slaveRedisDegrade"
    local slaveRedisDegradeTimeKey = "slaveRedisDegradeTime"
    local slaveRedisDegrade = cache_ngx:get(slaveRedisDegradeKey)

    if slaveRedisDegrade == "true" then
        local datalinkDegrade = cache_ngx:get(datalinkDegradeKey);
        if datalinkDegrade == "true" then
            local red = redis:new()
            red:set_timeout(1000)
            local ip = "192.168.198.150"
            local port = 1111
            local ok, err = red:connect(ip, port)
            local redisResp, redisErr = red:get("ProductKey:aggregationKey:" .. productId)
            productCache = redisResp

            local datalinkDegradeTime = cache_ngx:get(datalinkDegradeTimeKey)
            local curTime = os.time()
            local diffTime = os.difftime(curTime, datalinkDegradeTime)
            if diffTime > 60 then
                local httpc = http.new()
                local resp, err = httpc:request_uri("http://192.168.198.1:8767", {
                    method = "GET",
                    path = "/product?productId=" .. productId
                })
                if resp then
                    cache_ngx:set(datalinkDegradeKey, "false");
                end
            end

        else
            local httpc = http.new()
            local resp, err = httpc:request_uri("http://192.168.198.1:8767", {
                method = "GET",
                path = "/product?productId=" .. productId
            })
            if not resp then
                ngx.say("request error :", err)
                local datalinkFailureCnt = cache_ngx:get(datalinkFailureCntKey)
                cache_ngx:set(datalinkFailureCntKey, datalinkFailureCnt + 1)
                if datalinkFailureCnt > 10 then
                    cache_ngx:set(datalinkDegradeKey, "true")
                    cache_ngx:set(datalinkDegradeTimeKey, os.time())
                end
            end
            productCache = resp.body
            local slaveRedisDegradeTime =
                cache_ngx:get(slaveRedisDegradeTimeKey)
            local curTime = os.time()
            local diffTime = os.difftime(curTime, slaveRedisDegradeTime)
            if diffTime > 60 then
                local red = redis:new()
                red:set_timeout(1000)
                local ip = "192.168.198.151"
                local port = 1111
                local ok, err = red:connect(ip, port)
                if ok then
                    cache_ngx:set(slaveRedisDegradeKey, "false")
                end
            end
        end

    else
        local red = redis:new()
        red:set_timeout(1000)
        local ip = "192.168.198.151"
        local port = 1111
        local ok, err = red:connect(ip, port)
        if not ok then
            ngx.say("connect to redis error : ", err)
            local slaveFailureCntKey = "slaveFailureCnt"
            local slaveRedisFailureCnt = cache_ngx:get(slaveFailureCntKey)
            cache_ngx:set(slaveFailureCntKey, laveRedisFailureCnt + 1)
            if laveRedisFailureCnt > 10 then
                cache_ngx:set(slaveRedisDegradeKey, "true")
                cache_ngx:set(slaveRedisDegradeTimeKey, os.time())
            end
            return close_redis(red)
        end

        local redisResp, redisErr = red:get("ProductKey:aggregationKey:" .. productId)
        if redisResp == ngx.null or redisResp == "" or redisResp == nil then
            local datalinkDegrade = cache_ngx:set(datalinkDegradeKey);
            if datalinkDegrade == "true" then
                local red = redis:new()
                red:set_timeout(1000)
                local ip = "192.168.198.150"
                local port = 1111
                local ok, err = red:connect(ip, port)
                local redisResp, redisErr = red:get("ProductKey:aggregationKey:" .. productId)
                productCache = redisResp

                local datalinkDegradeTime =
                    cache_ngx:get(datalinkDegradeTimeKey)
                local curTime = os.time()
                local diffTime = os.difftime(curTime, datalinkDegradeTime)
                if diffTime > 60 then
                    local httpc = http.new()
                    local resp, err = httpc:request_uri("http://192.168.198.1:8767", {
                            method = "GET",
                            path = "/product?productId=" .. productId
                        })
                    if resp then
                        cache_ngx:set(datalinkDegradeKey, "false");
                    end
                end

            else
                local httpc = http.new()
                local resp, err = httpc:request_uri("http://192.168.198.1:8767", {
                    method = "GET",
                    path = "/product?productId=" .. productId
                })

                if not resp then
                    ngx.say("request error :", err)
                    local datalinkFailureCnt =
                        cache_ngx:get(datalinkFailureCntKey)
                    cache_ngx:set(datalinkFailureCntKey, datalinkFailureCnt + 1)
                    if datalinkFailureCnt > 10 then
                        cache_ngx:set(datalinkDegradeKey, "true")
                        cache_ngx:set(datalinkDegradeTimeKey, os.time())
                    end
                end
                productCache = resp.body
            end
        else
            productCache = redisResp
        end

    end

    math.randomseed(tostring(os.time()):reverse():sub(1, 7))
    local expireTime = math.random(600, 1200)

    cache_ngx:set(productCacheKey, productCache, expireTime)
end

local context = {productInfo = productCache}

local template = require("resty.template")
template.render("product.html", context)

