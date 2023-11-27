package com.honeybadgersoftware.availability.utils.sorting;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface GenericSorter<T, R> {

    default Map<R, List<T>> sort(Collection<T> t, Function<T, R> function) {
        return t.stream().collect(Collectors.groupingBy(function));
    }
}
