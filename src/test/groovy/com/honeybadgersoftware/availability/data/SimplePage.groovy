package com.honeybadgersoftware.availability.data

import com.google.gson.Gson

class SimplePage {

    List<Long> data

    SimplePage(List<Long> data) {
        this.data = data
    }

    static SimplePage simplePage = new SimplePage([1L, 6L, 7L, 9L, 10L] as List)
    static String json = new Gson().toJson(simplePage)

}
