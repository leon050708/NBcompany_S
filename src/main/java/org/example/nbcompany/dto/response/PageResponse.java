package org.example.nbcompany.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {
    private long total;
    private int pages;
    private int current;
    private List<T> records;
} 