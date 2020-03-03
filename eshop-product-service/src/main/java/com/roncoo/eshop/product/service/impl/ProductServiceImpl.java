package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.rabbitmq.RabbitQueue;
import com.roncoo.eshop.mq.MessageSender;
import com.roncoo.eshop.product.mapper.ProductMapper;
import com.roncoo.eshop.product.model.Product;
import com.roncoo.eshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    MessageSender messageSender;

    public void add(Product product) {
        productMapper.add(product);
        messageSender.sendAddMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_CHANGE, product);
    }

    public void update(Product product) {
        productMapper.update(product);
        messageSender.sendUpdateMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_CHANGE, product);
    }

    public void delete(Long id) {
        productMapper.delete(id);
        Product product = new Product();
        product.setId(id);
        messageSender.sendDeleteMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_CHANGE, product);
    }

    public Product findById(Long id) {
        return productMapper.findById(id);
    }

}
