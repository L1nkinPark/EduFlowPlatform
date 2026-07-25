package com.lms.frontend.service;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.PublicStatsResponse;

public interface PublicStatsService {
    ApiResponse<PublicStatsResponse> getPublicStats();
}
