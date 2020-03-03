package com.roncoo.eshop.common;

public abstract class AbstractMessage implements MessageInterface {

    protected abstract String findId();

    @Override
    public String messageId() {
        return findId();
    }
}
