package org.example.nbcompany.dto.CompanyDto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CompanyMemberDTO {
    private Long id;
    private String username;
    private String nickname;
    private Long companyId;
    private Integer companyRole; // 1:普通员工, 2:企业管理员
    private Integer status;
    private LocalDateTime createdAt;
    // ... 其他可能需要的字段，例如phoneNumber, email
}