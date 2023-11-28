package com.honeybadgersoftware.availability.utils;

public interface GenericDecorator<T, R>{
    R decorate(T t, R r);
}
