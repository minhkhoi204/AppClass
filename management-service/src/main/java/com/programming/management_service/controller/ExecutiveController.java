package com.programming.management_service.controller;

import com.programming.management_service.model.Executive;
import com.programming.management_service.service.executive.ExecutiveService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/executives")
public class ExecutiveController {

    private final ExecutiveService executiveService;

    public ExecutiveController(ExecutiveService executiveService) {
        this.executiveService = executiveService;
    }

    @GetMapping("/{userId}")
    public Executive getExecutiveByUserId(@PathVariable Long userId) {
        return executiveService.getExecutiveWithUser(userId);
    }
}
