# ngx.say("distribution 151")

local uri_args = ngx.req.get_uri_args()
local productId = uri_args["productId"]

local host = {"192.168.198.150:8000", "192.168.198.151:8000"}
local hash = ngx.crc32_long(productId)
local index = (hash % 2) + 1
local backend = "http://"..host[index]

local requestPath = uri_args["requestPath"]
requestPath = "/"..requestPath.."?productId="..productId

local http = require("resty.http")
local httpc = http.new()

local resp, err = httpc:request_uri(backend, {
        method = "GET",
        path = requestPath,
        keepalive=false
})

if not resp then
        ngx.say("request error :", err)
        return
end

ngx.say(resp.body)

httpc:close()


