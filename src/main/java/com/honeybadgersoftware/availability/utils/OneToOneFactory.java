package com.honeybadgersoftware.availability.utils;

import java.util.List;
import java.util.stream.Collectors;

public interface OneToOneFactory<T, R> {

    R map(T t);

    default List<R> map(List<T> t) {
        return t.stream().map(this::map).collect(Collectors.toList());
    }
}
