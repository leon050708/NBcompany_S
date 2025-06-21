package org.example.nbcompany.controller;

import org.example.nbcompany.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/hello")
    public ApiResponse<String> hello() {
        return ApiResponse.success("Hello, API is working!");
    }

    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "NBCompany");
        info.put("version", "1.0.0");
        info.put("status", "running");
        info.put("timestamp", System.currentTimeMillis());
        return ApiResponse.success("获取成功", info);
    }

    @PostMapping("/echo")
    public ApiResponse<Map<String, Object>> echo(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Echo response");
        response.put("received", request);
        response.put("timestamp", System.currentTimeMillis());
        return ApiResponse.success("回显成功", response);
    }
} 