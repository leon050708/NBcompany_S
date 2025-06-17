package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class RegisterCompanyDTO {
    private String companyName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
}