package com.roncoo.eshop.common.rabbitmq;

public interface RabbitQueue {

    String DATA_CHANGE_QUEUE = "data-change-queue";
    String AGGR_DATA_CHANGE_QUEUE = "aggr-data-change-queue";
    String HIGH_PRIORITY_AGGR_DATA_CHANGE_QUEUE = "high-priority-aggr-data-change-queue";
    String REFRESH_AGGR_DATA_CHANGE_QUEUE = "refresh-aggr-data-change-queue";

}
