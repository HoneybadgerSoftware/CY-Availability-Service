package com.honeybadgersoftware.availability.utils.factory;

import java.util.List;
import java.util.stream.Collectors;

public interface ManyToOneFactory<T, R, S> {

    S map(T t, R r);

    default List<S> map(List<T> t, R r) {
        return t.stream().map(o1 -> this.map(o1, r)).collect(Collectors.toList());
    }
}
