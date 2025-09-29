package com.programming.management_service.service.executive;

import com.programming.management_service.model.Executive;

public interface ExecutiveService {
    Executive getExecutiveWithUser(Long userId);
}
