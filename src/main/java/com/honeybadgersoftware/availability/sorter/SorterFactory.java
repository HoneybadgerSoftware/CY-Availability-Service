package com.honeybadgersoftware.availability.sorter;

import com.honeybadgersoftware.availability.model.entity.AvailabilityEntity;
import com.honeybadgersoftware.availability.utils.sorting.GenericSorter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SorterFactory implements GenericSorter<AvailabilityEntity, Long> {

    public static <T, R> GenericSorter<T, R> createSorter(Function<T, R> groupFunction) {
        return new GenericSorter<T, R>() {
            @Override
            public Map<R, List<T>> sort(Collection<T> items, Function<T, R> function) {
                return GenericSorter.super.sort(items, groupFunction);
            }
        };
    }
}


