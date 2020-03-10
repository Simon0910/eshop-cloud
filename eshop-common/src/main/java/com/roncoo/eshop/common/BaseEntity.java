package com.roncoo.eshop.common;

import lombok.Data;

@Data
public class BaseEntity extends AbstractMessage {
    public Long id;

    @Override
    protected String findId() {
        return String.valueOf(id);
    }

}
