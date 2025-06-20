package org.example.nbcompany.dto.NewsDto;

import lombok.Data;

@Data
public class NewsQueryDto {
    private String title;
    private String authorName;
    private Long companyId;
    private Integer status;
}