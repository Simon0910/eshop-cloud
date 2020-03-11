package com.roncoo.eshop.price.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.common.keys.PriceKey;
import com.roncoo.eshop.price.mapper.ProductPriceMapper;
import com.roncoo.eshop.price.model.ProductPrice;
import com.roncoo.eshop.price.service.ProductPriceService;
import com.roncoo.eshop.redis.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceServiceImpl implements ProductPriceService {

    @Autowired
    private ProductPriceMapper productPriceMapper;

    @Autowired
    RedisService redisService;

    @Override
    public void add(ProductPrice productPrice) {
        productPriceMapper.add(productPrice);
        redisService.set(PriceKey.priceKey, String.valueOf(productPrice.getId()), productPrice);
    }

    @Override
    public void update(ProductPrice productPrice) {
        productPriceMapper.update(productPrice);
    }

    @Override
    public void delete(Long id) {
        productPriceMapper.delete(id);
    }

    @Override
    public ProductPrice findById(Long id) {
        return productPriceMapper.findById(id);
    }

    @Override
    public ProductPrice findByProductId(Long productId) {
        String dataJSON = redisService.get(PriceKey.priceKey, String.valueOf(productId));
        if (StringUtils.isNotBlank(dataJSON)) {
            JSONObject dataJSONObject = JSONObject.parseObject(dataJSON);
            dataJSONObject.put("id", "-1");
            return JSONObject.parseObject(dataJSONObject.toJSONString(), ProductPrice.class);
        } else {
            return productPriceMapper.findByProductId(productId);
        }
    }

}
