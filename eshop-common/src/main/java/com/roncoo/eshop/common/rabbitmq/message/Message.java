package com.roncoo.eshop.common.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Message<T> {

    private String eventType;

    private String dataType;

    private String id;

    private T data;
}
