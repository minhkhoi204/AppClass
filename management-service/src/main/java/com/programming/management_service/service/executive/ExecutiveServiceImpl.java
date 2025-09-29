package com.programming.management_service.service.executive;

import com.programming.management_service.dto.UserDto;
import com.programming.management_service.management_caller.UserClient;
import com.programming.management_service.model.Executive;
import com.programming.management_service.repository.ExecutiveRepository;
import org.springframework.stereotype.Service;

@Service
public class ExecutiveServiceImpl implements ExecutiveService {
    private final ExecutiveRepository executiveRepository;
    private final UserClient userClient;

    public ExecutiveServiceImpl(ExecutiveRepository executiveRepository, UserClient userClient) {
        this.executiveRepository = executiveRepository;
        this.userClient = userClient;
    }

    public Executive getExecutiveWithUser(Long userId) {
        Executive executive = executiveRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Executive not found"));

        // Gọi user-service để lấy thông tin user
        UserDto userDto = userClient.getUserById(userId);
        executive.setUser(userDto);

        return executive;
    }
}
