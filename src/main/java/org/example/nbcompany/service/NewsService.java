package org.example.nbcompany.service;

import com.github.pagehelper.PageInfo;
import org.example.nbcompany.dto.NewsDto.*;

public interface NewsService {

    NewsResponseDto createNews(CreateNewsDto createNewsDto);

    PageInfo<NewsResponseDto> getAllNews(int pageNum, int pageSize, NewsQueryDto queryDto);

    NewsResponseDto getNewsDetail(Long newsId);

    NewsResponseDto updateNews(Long newsId, UpdateNewsDto updateNewsDto);

    void deleteNews(Long newsId);

    void auditNews(Long newsId, AuditNewsDto auditNewsDto);
}