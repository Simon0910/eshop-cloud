package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.rabbitmq.RabbitQueue;
import com.roncoo.eshop.mq.MessageSender;
import com.roncoo.eshop.product.mapper.BrandMapper;
import com.roncoo.eshop.product.model.Brand;
import com.roncoo.eshop.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    MessageSender messageSender;

    @Override
    public void add(Brand brand) {
        brandMapper.add(brand);
        messageSender.sendAddMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.BRAND_CHANGE, brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.update(brand);
        messageSender.sendUpdateMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.BRAND_CHANGE, brand);
    }

    @Override
    public void delete(Long id) {
        brandMapper.delete(id);
        Brand brand = new Brand();
        brand.setId(id);
        messageSender.sendDeleteMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.BRAND_CHANGE, brand);
    }

    @Override
    public Brand findById(Long id) {
        return brandMapper.findById(id);
    }

    @Override
    public List<Brand> findByIds(String ids) {
        return brandMapper.findByIds(ids);
    }

}
