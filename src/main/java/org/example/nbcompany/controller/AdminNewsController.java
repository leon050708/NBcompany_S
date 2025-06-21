package org.example.nbcompany.controller;

import org.example.nbcompany.dto.NewsDto.AuditNewsDto;
import org.example.nbcompany.dto.response.ApiResponse;
import org.example.nbcompany.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/news")
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
public class AdminNewsController {

    @Autowired
    private NewsService newsService;

    @PutMapping("/{id}/status")
    public ApiResponse<?> auditNews(@PathVariable Long id, @RequestBody AuditNewsDto auditDto) {
        newsService.auditNews(id, auditDto);
        // 修正：调用 success(null) 来匹配已存在的方法
        return ApiResponse.success(null);
    }
}