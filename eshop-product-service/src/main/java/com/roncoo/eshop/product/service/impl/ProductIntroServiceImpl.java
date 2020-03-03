package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.rabbitmq.RabbitQueue;
import com.roncoo.eshop.mq.MessageSender;
import com.roncoo.eshop.product.mapper.ProductIntroMapper;
import com.roncoo.eshop.product.model.ProductIntro;
import com.roncoo.eshop.product.service.ProductIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductIntroServiceImpl implements ProductIntroService {

    @Autowired
    private ProductIntroMapper productIntroMapper;

    @Autowired
    MessageSender messageSender;

    public void add(ProductIntro productIntro) {
        productIntroMapper.add(productIntro);
        messageSender.sendAddMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_INTRO_CHANGE, productIntro);
    }

    public void update(ProductIntro productIntro) {
        productIntroMapper.update(productIntro);
        messageSender.sendUpdateMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_INTRO_CHANGE, productIntro);
    }

    public void delete(Long id) {
        productIntroMapper.delete(id);
        ProductIntro productIntro = new ProductIntro();
        productIntro.setId(id);
        messageSender.sendDeleteMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.PRODUCT_INTRO_CHANGE, productIntro);
    }

    public ProductIntro findById(Long id) {
        return productIntroMapper.findById(id);
    }

}
