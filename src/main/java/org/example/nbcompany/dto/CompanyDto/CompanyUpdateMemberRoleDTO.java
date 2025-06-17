package org.example.nbcompany.dto.CompanyDto;

import lombok.Data;

@Data
public class CompanyUpdateMemberRoleDTO {
    private Integer companyRole; // 1:普通员工, 2:企业管理员
}