package org.example.nbcompany.dto;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String username;
    private String password; // 创建时需要密码
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Integer userType;
    private Long companyId;
    private Integer companyRole;
    private Integer status;

    public CreateUserDTO(String username, String password, String nickname, String phoneNumber, String email, Integer gender, Integer userType, Long companyId, Integer companyRole, Integer status) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.userType = userType;
        this.companyId = companyId;
        this.companyRole = companyRole;
        this.status = status;
    }

    public CreateUserDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
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
        return "CreateUserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", userType=" + userType +
                ", companyId=" + companyId +
                ", companyRole=" + companyRole +
                ", status=" + status +
                '}';
    }
}