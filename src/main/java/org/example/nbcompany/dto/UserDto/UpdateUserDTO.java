package org.example.nbcompany.dto.UserDto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Long companyId;
    private Integer companyRole;
    private Integer status;

    public UpdateUserDTO(String nickname, String phoneNumber, String email, Integer gender, Long companyId, Integer companyRole, Integer status) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.companyId = companyId;
        this.companyRole = companyRole;
        this.status = status;
    }

    public UpdateUserDTO() {}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyRole() {
        return companyRole;
    }

    public void setCompanyRole(Integer companyRole) {
        this.companyRole = companyRole;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateUserDTO{" +
                "nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", companyId=" + companyId +
                ", companyRole=" + companyRole +
                ", status=" + status +
                '}';
    }
}
