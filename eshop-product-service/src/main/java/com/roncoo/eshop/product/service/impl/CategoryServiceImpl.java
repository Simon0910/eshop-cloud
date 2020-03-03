package com.roncoo.eshop.product.service.impl;

import com.roncoo.eshop.common.Business;
import com.roncoo.eshop.common.rabbitmq.RabbitQueue;
import com.roncoo.eshop.mq.MessageSender;
import com.roncoo.eshop.product.mapper.CategoryMapper;
import com.roncoo.eshop.product.model.Category;
import com.roncoo.eshop.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    MessageSender messageSender;

    public void add(Category category) {
        categoryMapper.add(category);
        messageSender.sendAddMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.CATEGORY_CHANGE, category);
    }

    public void update(Category category) {
        categoryMapper.update(category);
        messageSender.sendUpdateMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.CATEGORY_CHANGE, category);
    }

    public void delete(Long id) {
        categoryMapper.delete(id);
        Category category = new Category();
        category.setId(id);
        messageSender.sendDeleteMessage(RabbitQueue.DATA_CHANGE_QUEUE, Business.CATEGORY_CHANGE, category);
    }

    public Category findById(Long id) {
        return categoryMapper.findById(id);
    }

}
