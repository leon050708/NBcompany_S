package org.example.nbcompany.dto.UserDto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String phoneNumber;
    private String email;
    private Integer gender;
    private Integer userType;
    private Long companyId;
    private Integer companyRole;
    private Integer status;
    private LocalDateTime createdAt;

    public UserDTO(LocalDateTime createdAt, Integer status, Integer companyRole, Long companyId, Integer userType, Integer gender, String email, String phoneNumber, String nickname, String username, Long id) {
        this.createdAt = createdAt;
        this.status = status;
        this.companyRole = companyRole;
        this.companyId = companyId;
        this.userType = userType;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.username = username;
        this.id = id;
    }

    public UserDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}