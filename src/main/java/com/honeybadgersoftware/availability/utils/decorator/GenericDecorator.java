package com.honeybadgersoftware.availability.utils.decorator;

public interface GenericDecorator<T, R> {
    R decorate(T t, R r);
}
