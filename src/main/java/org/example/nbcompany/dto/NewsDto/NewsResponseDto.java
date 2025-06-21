package org.example.nbcompany.dto.NewsDto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NewsResponseDto {
    private Long id;
    private String title;
    private String coverImageUrl;
    private String summary;
    private String content;
    private Long authorId;
    private String authorName;
    private Long companyId;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createdAt;
}