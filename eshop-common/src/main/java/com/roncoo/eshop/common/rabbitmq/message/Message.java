package com.roncoo.eshop.common.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

    private String eventType;

    private String dataType;

    private String id;
}
