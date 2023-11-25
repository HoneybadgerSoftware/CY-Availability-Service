package com.honeybadgersoftware.availability.utils;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public interface ManyToOneFactory<T, R, S> {

    S map(T t, R r);

    default List<S> map(List<T> t, R r) {
        return t.stream().map(o1 -> this.map(o1, r)).collect(Collectors.toList());
    }

    default List<S> map(List<T> t, List<R> r, BiPredicate<T, R> matchCriterion) {
        return t.stream().flatMap(
                        o1 -> r.stream()
                                .filter(o2 -> matchCriterion.test(o1, o2))
                                .map(o2 -> map(o1, o2)))
                .collect(Collectors.toList());
    }
}
