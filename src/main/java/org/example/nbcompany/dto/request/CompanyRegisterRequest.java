package org.example.nbcompany.dto.request;

import lombok.Data;

@Data
public class CompanyRegisterRequest {
    private String companyName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
} 