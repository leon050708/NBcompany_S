package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class UpdateCompanyStatusDTO {
    private Integer status; // 目标状态：0:禁用, 1:正常
}