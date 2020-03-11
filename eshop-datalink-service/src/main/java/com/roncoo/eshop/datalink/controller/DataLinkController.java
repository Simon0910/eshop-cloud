package com.roncoo.eshop.datalink.controller;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.keys.ProductKey;
import com.roncoo.eshop.datalink.service.EshopProductService;
import com.roncoo.eshop.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzp on 2020/3/10.
 */
@Slf4j
@RestController
public class DataLinkController {

    @Autowired
    private EshopProductService eshopProductService;

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "product")
    @ResponseBody
    public String getProduct(Long productId) {
        // 先读本地的ehcache，但是我们这里就不做了，因为之前都演示过了，大家自己做就可以了

        // 读redis主集群
        String aggrProductJson = redisService.get(ProductKey.aggregationKey, String.valueOf(productId));
        log.info("data-link: get aggrProductJson = {}", JSONObject.toJSONString(aggrProductJson));
        if (StringUtils.isBlank(aggrProductJson)) {
            String productDataJSON = eshopProductService.findProductById(productId);

            if (StringUtils.isNotBlank(productDataJSON)) {
                JSONObject productDataJSONObject = JSONObject.parseObject(productDataJSON);

                String productPropertyDataJSON = eshopProductService.findProductPropertyByProductId(productId);
                if (productPropertyDataJSON != null && !"".equals(productPropertyDataJSON)) {
                    productDataJSONObject.put(ProductKey.productPropertyKey.getPrefix(), JSONObject.parse(productPropertyDataJSON));
                }

                String productSpecificationDataJSON = eshopProductService.findProductSpecificationByProductId(productId);
                if (StringUtils.isNotBlank(productSpecificationDataJSON)) {
                    productDataJSONObject.put(ProductKey.productSpecificationKey.getPrefix(), JSONObject.parse(productSpecificationDataJSON));
                }

                redisService.set(ProductKey.aggregationKey, String.valueOf(productId), productDataJSONObject.toJSONString());
                log.info("data-link: set aggrProductJson = {}", productDataJSONObject.toJSONString());
                return productDataJSONObject.toJSONString();
            }
        }
        return "";
    }
}
