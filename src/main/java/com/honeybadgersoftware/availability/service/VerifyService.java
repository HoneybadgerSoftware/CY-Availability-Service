package com.honeybadgersoftware.availability.service;

import java.util.List;

public interface VerifyService {

    void verifyAvailability(List<Long> ids);
}
