package org.example.nbcompany.controller;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.NewsDto.CreateNewsDto;
import org.example.nbcompany.dto.NewsDto.NewsQueryDto;
import org.example.nbcompany.dto.NewsDto.NewsResponseDto;
import org.example.nbcompany.dto.NewsDto.UpdateNewsDto;
import org.example.nbcompany.dto.response.ApiResponse;
import org.example.nbcompany.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping
    public ApiResponse<NewsResponseDto> createNews(@RequestBody CreateNewsDto createNewsDto) {
        return ApiResponse.success(newsService.createNews(createNewsDto));
    }

    @GetMapping
    public ApiResponse<PageInfo<NewsResponseDto>> getAllNews(@RequestParam(defaultValue = "1") int pageNum,
                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                             NewsQueryDto queryDto) {
        return ApiResponse.success(newsService.getAllNews(pageNum, pageSize, queryDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<NewsResponseDto> getNewsDetail(@PathVariable Long id) {
        return ApiResponse.success(newsService.getNewsDetail(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<NewsResponseDto> updateNews(@PathVariable Long id, @RequestBody UpdateNewsDto updateNewsDto) {
        return ApiResponse.success(newsService.updateNews(id, updateNewsDto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        // 修正：调用 success(null) 来匹配已存在的方法
        return ApiResponse.success(null);
    }
}