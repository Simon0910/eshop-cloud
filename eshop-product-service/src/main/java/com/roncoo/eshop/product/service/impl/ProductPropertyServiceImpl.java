package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.rabbitmq.RabbitQueue;
import com.roncoo.eshop.mq.MessageSender;
import com.roncoo.eshop.product.mapper.ProductPropertyMapper;
import com.roncoo.eshop.product.model.ProductProperty;
import com.roncoo.eshop.product.service.ProductPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductPropertyServiceImpl implements ProductPropertyService {

    @Autowired
    private ProductPropertyMapper productPropertyMapper;

    @Autowired
    MessageSender messageSender;

    public void add(ProductProperty productProperty) {
        productPropertyMapper.add(productProperty);
        messageSender.sendAddMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_CHANGE, productProperty);
    }

    public void update(ProductProperty productProperty) {
        productPropertyMapper.update(productProperty);
        messageSender.sendUpdateMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_CHANGE, productProperty);
    }

    public void delete(Long id) {
        productPropertyMapper.delete(id);
        ProductProperty productProperty = new ProductProperty();
        productProperty.setId(id);
        messageSender.sendDeleteMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_CHANGE, productProperty);

    }

    public ProductProperty findById(Long id) {
        return productPropertyMapper.findById(id);
    }

}
