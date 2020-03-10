package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.rabbitmq.RabbitQueue;
import com.roncoo.eshop.mq.MessageSender;
import com.roncoo.eshop.product.mapper.ProductSpecificationMapper;
import com.roncoo.eshop.product.model.ProductSpecification;
import com.roncoo.eshop.product.service.ProductSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSpecificationServiceImpl implements ProductSpecificationService {

    @Autowired
    private ProductSpecificationMapper productSpecificationMapper;

    @Autowired
    MessageSender messageSender;

    public void add(ProductSpecification productSpecification) {
        productSpecificationMapper.add(productSpecification);
        messageSender.sendAddMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_SPECIFICATION_CHANGE, productSpecification);
    }

    public void update(ProductSpecification productSpecification) {
        productSpecificationMapper.update(productSpecification);
        messageSender.sendUpdateMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_SPECIFICATION_CHANGE, productSpecification);
    }

    public void delete(Long id) {
        productSpecificationMapper.delete(id);
        ProductSpecification productSpecification = new ProductSpecification();
        productSpecification.setId(id);
        messageSender.sendDeleteMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_SPECIFICATION_CHANGE, productSpecification);

    }

    public ProductSpecification findById(Long id) {
        return productSpecificationMapper.findById(id);
    }

    @Override
    public ProductSpecification findByProductId(Long productId) {
        return productSpecificationMapper.findByProductId(productId);
    }

}
