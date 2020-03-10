package com.roncoo.eshop.common.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Message<T> {

    private String eventType;

    private String dataType;

    private String id;

    private T data;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message<?> message = (Message<?>) o;
        return Objects.equals(eventType, message.eventType) &&
                Objects.equals(dataType, message.dataType) &&
                Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, dataType, id);
    }
}
