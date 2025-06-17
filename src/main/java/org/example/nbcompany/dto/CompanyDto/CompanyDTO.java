package org.example.nbcompany.dto.CompanyDto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CompanyDTO {
    private Long id;
    private String companyName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private Integer status; // 0:禁用, 1:正常, 待审核状态也可以用0表示
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}