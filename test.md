# NBCompany API 接口测试文档

本文档提供了 NBCompany 项目所有 API 接口的测试用例。

**注意:**
-   `{{BASE_URL}}` 请替换为您的本地服务地址，例如 `http://localhost:8080`。
-   `{{ADMIN_TOKEN}}` 请替换为平台超级管理员登录后获取的 JWT Token。
-   `{{COMPANY_ADMIN_TOKEN}}` 请替换为企业管理员登录后获取的 JWT Token。
-   `{{USER_TOKEN}}` 请替换为普通用户登录后获取的 JWT Token。
-   `{{COMPANY_ID}}` 和 `{{MEMBER_ID}}` 请替换为实际的 ID。

---

## 1. 认证接口 (`/api/v1/auth`)

### 1.1 用户登录
```bash
curl -X POST -H "Content-Type: application/json" -d '{"username":"admin","password":"password123"}' {{BASE_URL}}/api/v1/auth/login
```

### 1.2 企业注册
```bash
curl -X POST -H "Content-Type: application/json" -d '{"companyName":"新科技公司","companyCode":"TECH001","companyAddress":"科技园一路","companyPhone":"010-12345678","companyEmail":"contact@newtech.com","username":"company_admin","password":"password123","nickname":"企业管理员","phone":"13800138000","email":"admin@newtech.com"}' {{BASE_URL}}/api/v1/auth/register/company
```

### 1.3 用户注册
```bash
curl -X POST -H "Content-Type: application/json" -d '{"username":"testuser","password":"password123","nickname":"测试用户","phoneNumber":"13900139000","email":"testuser@example.com","companyId":1}' {{BASE_URL}}/api/v1/auth/register/user
```

---

## 2. 用户接口 (`/api/v1/user`)

### 2.1 获取当前用户个人信息
```bash
curl -X GET -H "Authorization: Bearer {{USER_TOKEN}}" {{BASE_URL}}/api/v1/user/profile
```

### 2.2 修改当前用户基本资料
```bash
curl -X PUT -H "Content-Type: application/json" -H "Authorization: Bearer {{USER_TOKEN}}" -d '{"nickname":"新的昵称","email":"new.email@example.com"}' {{BASE_URL}}/api/v1/user/profile
```

### 2.3 修改当前用户密码
```bash
curl -X PUT -H "Content-Type: application/json" -H "Authorization: Bearer {{USER_TOKEN}}" -d '{"oldPassword":"password123","newPassword":"newPassword456"}' {{BASE_URL}}/api/v1/user/password
```

---

## 3. 企业接口 (`/api/v1/companies`, `/api/v1/admin/companies`)

### 3.1 获取企业列表 (公开)
```bash
curl -X GET '{{BASE_URL}}/api/v1/companies?keyword=科技&page=1&size=10'
```

### 3.2 审核企业状态 (平台超级管理员)
```bash
curl -X PUT -H "Content-Type: application/json" -H "Authorization: Bearer {{ADMIN_TOKEN}}" -d '{"status":1}' {{BASE_URL}}/api/v1/admin/companies/{{COMPANY_ID}}/status
```

---

## 4. 企业成员管理接口 (`/api/v1/company/{companyId}/members`)

### 4.1 获取企业成员列表 (企业管理员)
```bash
curl -X GET -H "Authorization: Bearer {{COMPANY_ADMIN_TOKEN}}" '{{BASE_URL}}/api/v1/company/{{COMPANY_ID}}/members?page=1&size=10'
```

### 4.2 创建企业成员 (企业管理员)
```bash
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer {{COMPANY_ADMIN_TOKEN}}" -d '{"username":"newmember","password":"password123","nickname":"新成员","phoneNumber":"13700137000","email":"new.member@newtech.com","companyRole":1}' {{BASE_URL}}/api/v1/company/{{COMPANY_ID}}/members
```

### 4.3 修改成员角色 (企业管理员)
```bash
curl -X PUT -H "Content-Type: application/json" -H "Authorization: Bearer {{COMPANY_ADMIN_TOKEN}}" -d '{"companyRole":2}' {{BASE_URL}}/api/v1/company/{{COMPANY_ID}}/members/{{MEMBER_ID}}/role
```

### 4.4 修改成员信息 (企业管理员)
```bash
curl -X PUT -H "Content-Type: application/json" -H "Authorization: Bearer {{COMPANY_ADMIN_TOKEN}}" -d '{"nickname":"修改后的昵称"}' {{BASE_URL}}/api/v1/company/{{COMPANY_ID}}/members/{{MEMBER_ID}}
```

### 4.5 删除成员 (企业管理员)
```bash
curl -X DELETE -H "Authorization: Bearer {{COMPANY_ADMIN_TOKEN}}" {{BASE_URL}}/api/v1/company/{{COMPANY_ID}}/members/{{MEMBER_ID}}
```

---

## 5. 平台用户管理接口 (`/api/v1/admin/users`)

### 5.1 获取所有用户列表 (平台超级管理员)
```bash
curl -X GET -H "Authorization: Bearer {{ADMIN_TOKEN}}" '{{BASE_URL}}/api/v1/admin/users?username=test&page=1&size=10'
```

### 5.2 创建用户 (平台超级管理员)
```bash
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer {{ADMIN_TOKEN}}" -d '{"username":"adminuser","password":"password123","nickname":"平台用户","userType":2,"status":1}' {{BASE_URL}}/api/v1/admin/users
```

### 5.3 修改用户信息 (平台超级管理员)
```bash
curl -X PUT -H "Content-Type: application/json" -H "Authorization: Bearer {{ADMIN_TOKEN}}" -d '{"nickname":"修改后的平台用户","status":0}' {{BASE_URL}}/api/v1/admin/users/{{MEMBER_ID}}
```

# 接口详细输入输出示例

## 认证相关接口 (/api/v1/auth)

### 1. 企业注册
- **POST** `/api/v1/auth/register/company`
- 权限：无需认证
- 请求体：
```json
{
  "companyName": "新企业",
  "contactPerson": "张三",
  "contactPhone": "13800000000",
  "contactEmail": "test@company.com"
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "企业注册成功，请等待平台管理员审核",
  "data": {
    "companyId": 123,
    "companyName": "新企业"
  }
}
```

### 2. 用户注册
- **POST** `/api/v1/auth/register/user`
- 权限：无需认证
- 请求体：
```json
{
  "username": "user1",
  "password": "123456",
  "nickname": "小明",
  "phoneNumber": "13800000001",
  "email": "user1@company.com",
  "gender": 1,
  "companyId": 123,
  "verifyCode": "abcd"
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "用户注册成功，请等待企业管理员分配权限",
  "data": null
}
```

### 3. 用户登录
- **POST** `/api/v1/auth/login`
- 权限：无需认证
- 请求体：
```json
{
  "username": "user1",
  "password": "123456"
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "jwt-token-string",
    "userInfo": {
      "id": 1,
      "username": "user1",
      "nickname": "小明",
      "userType": 1,
      "companyId": 123,
      "companyName": "新企业",
      "companyRole": 1
    }
  }
}
```

---

## 用户相关接口 (/api/v1/user)

### 1. 获取当前用户信息
- **GET** `/api/v1/user/profile`
- 权限：需要用户认证
- 响应体：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "username": "user1",
    "nickname": "小明",
    "phoneNumber": "13800000001",
    "email": "user1@company.com",
    "gender": 1,
    "userType": 1,
    "companyId": 123,
    "companyName": "新企业",
    "companyRole": 1,
    "status": 1,
    "createdAt": "2024-01-01 10:00:00"
  }
}
```

### 2. 修改当前用户信息
- **PUT** `/api/v1/user/profile`
- 权限：需要用户认证
- 请求体：
```json
{
  "nickname": "小明2",
  "phoneNumber": "13800000002",
  "email": "user1@company.com",
  "gender": 1
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "修改成功",
  "data": null
}
```

### 3. 修改当前用户密码
- **PUT** `/api/v1/user/password`
- 权限：需要用户认证
- 请求体：
```json
{
  "oldPassword": "password123",
  "newPassword": "newPassword456",
  "confirmNewPassword": "newPassword456"
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null
}
```

---

## 平台管理员接口 (/api/v1/admin)

### 1. 获取用户列表
- **GET** `/api/v1/admin/users`
- 权限：平台超级管理员
- 查询参数（可选）：companyId, companyRole, userType, username, phoneNumber, status, page, size
- 响应体：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 100,
    "pages": 10,
    "current": 1,
    "records": [
      {
        "id": 1,
        "username": "user1",
        "nickname": "小明",
        "phoneNumber": "13800000001",
        "email": "user1@company.com",
        "gender": 1,
        "userType": 1,
        "companyId": 123,
        "companyName": "新企业",
        "companyRole": 1,
        "status": 1,
        "createdAt": "2024-01-01 10:00:00"
      }
    ]
  }
}
```

### 2. 创建新用户
- **POST** `/api/v1/admin/users`
- 权限：平台超级管理员
- 请求体：
```json
{
  "username": "user2",
  "password": "123456",
  "nickname": "小红",
  "phoneNumber": "13800000002",
  "email": "user2@company.com",
  "gender": 2,
  "userType": 1,
  "companyId": 123,
  "companyRole": 2,
  "status": 1
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "用户创建成功",
  "data": null
}
```

### 3. 修改用户信息
- **PUT** `/api/v1/admin/users/{userId}`
- 权限：平台超级管理员
- 请求体：同上
- 响应体：
```json
{
  "code": 200,
  "message": "用户信息修改成功",
  "data": null
}
```

### 4. 修改企业状态
- **PUT** `/api/v1/admin/companies/{companyId}/status`
- 权限：平台超级管理员
- 请求体：
```json
{
  "status": 1
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "企业状态修改成功",
  "data": null
}
```

---

## 企业管理员接口 (/api/v1/company)

### 1. 获取企业成员列表
- **GET** `/api/v1/company/{companyId}/members`
- 权限：企业管理员
- 查询参数（可选）：username, companyRole, status, page, size
- 响应体：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "total": 20,
    "pages": 2,
    "current": 1,
    "records": [
      {
        "id": 101,
        "username": "employee1",
        "nickname": "小李",
        "companyId": 123,
        "companyRole": 1,
        "status": 1,
        "createdAt": "2024-05-01 09:00:00"
      }
    ]
  }
}
```

### 2. 创建企业成员
- **POST** `/api/v1/company/{companyId}/members`
- 权限：企业管理员
- 请求体：
```json
{
  "username": "employee2",
  "password": "123456",
  "nickname": "小王",
  "phoneNumber": "13800000003",
  "email": "employee2@company.com",
  "gender": 1,
  "companyRole": 1,
  "status": 1
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "成员创建成功",
  "data": null
}
```

### 3. 修改成员角色
- **PUT** `/api/v1/company/{companyId}/members/{memberId}/role`
- 权限：企业管理员
- 请求体：
```json
{
  "companyRole": 2
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "成员权限修改成功",
  "data": null
}
```

### 4. 修改成员信息
- **PUT** `/api/v1/company/{companyId}/members/{memberId}`
- 权限：企业管理员
- 请求体：
```json
{
  "nickname": "小王2",
  "phoneNumber": "13800000004",
  "email": "employee2@company.com",
  "gender": 1,
  "status": 1
}
```
- 响应体：
```json
{
  "code": 200,
  "message": "成员信息修改成功",
  "data": null
}
```

### 5. 删除成员
- **DELETE** `/api/v1/company/{companyId}/members/{memberId}`
- 权限：企业管理员
- 响应体：
```json
{
  "code": 200,
  "message": "成员删除成功",
  "data": null
}
```

## 环境准备

### Postman环境变量
```
BASE_URL: http://localhost:8080
TOKEN: 登录后获取的token
```

### 通用请求头
```
Authorization: Bearer {{TOKEN}}
Content-Type: application/json
```

## 接口列表

### 1. 企业注册
- **接口**：`POST {{BASE_URL}}/api/v1/auth/register/company`
- **输入**：
```json
{
    "companyName": "测试企业",
    "contactPerson": "张三",
    "contactPhone": "13800000000",
    "contactEmail": "test@company.com"
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "企业注册成功，请等待平台管理员审核",
    "data": {
        "companyId": 123,
        "companyName": "测试企业"
    }
}
```

### 2. 用户注册
- **接口**：`POST {{BASE_URL}}/api/v1/auth/register/user`
- **输入**：
```json
{
    "username": "testuser",
    "password": "123456",
    "nickname": "测试用户",
    "phoneNumber": "13800000001",
    "email": "test@example.com",
    "gender": 1,
    "companyId": 123,
    "verifyCode": "1234"
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "用户注册成功，请等待企业管理员分配权限",
    "data": null
}
```

### 3. 用户登录
- **接口**：`POST {{BASE_URL}}/api/v1/auth/login`
- **输入**：
```json
{
    "username": "testuser",
    "password": "123456"
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "userInfo": {
            "id": 1,
            "username": "testuser",
            "nickname": "测试用户",
            "userType": 1,
            "companyId": 123,
            "companyName": "测试企业",
            "companyRole": 1
        }
    }
}
```

### 4. 获取用户信息
- **接口**：`GET {{BASE_URL}}/api/v1/user/profile`
- **输入**：无
- **输出**：
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "id": 1,
        "username": "testuser",
        "nickname": "测试用户",
        "phoneNumber": "13800000001",
        "email": "test@example.com",
        "gender": 1,
        "userType": 1,
        "companyId": 123,
        "companyName": "测试企业",
        "companyRole": 1,
        "status": 1,
        "createdAt": "2024-01-01 10:00:00"
    }
}
```

### 5. 修改用户信息
- **接口**：`PUT {{BASE_URL}}/api/v1/user/profile`
- **输入**：
```json
{
    "nickname": "新昵称",
    "phoneNumber": "13800000002",
    "email": "new@example.com",
    "gender": 1
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "修改成功",
    "data": null
}
```

### 6. 修改密码
- **接口**：`PUT {{BASE_URL}}/api/v1/user/password`
- **输入**：
```json
{
    "oldPassword": "123456",
    "newPassword": "654321",
    "confirmNewPassword": "654321"
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "密码修改成功",
    "data": null
}
```
- **错误输出**：
```json
{
    "code": 500,
    "message": "原密码错误",
    "data": null
}
```
或
```json
{
    "code": 500,
    "message": "两次输入的新密码不一致",
    "data": null
}
```

### 7. 获取用户列表（管理员）
- **接口**：`GET {{BASE_URL}}/api/v1/admin/users`
- **输入**：
```
companyId=123&companyRole=1&userType=1&username=test&phoneNumber=138&status=1&page=1&size=10
```
- **输出**：
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "total": 100,
        "pages": 10,
        "current": 1,
        "records": [
            {
                "id": 1,
                "username": "testuser",
                "nickname": "测试用户",
                "phoneNumber": "13800000001",
                "email": "test@example.com",
                "gender": 1,
                "userType": 1,
                "companyId": 123,
                "companyName": "测试企业",
                "companyRole": 1,
                "status": 1,
                "createdAt": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

### 8. 创建新用户（管理员）
- **接口**：`POST {{BASE_URL}}/api/v1/admin/users`
- **输入**：
```json
{
    "username": "newuser",
    "password": "123456",
    "nickname": "新用户",
    "phoneNumber": "13800000003",
    "email": "new@example.com",
    "gender": 1,
    "userType": 1,
    "companyId": 123,
    "companyRole": 1,
    "status": 1
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "用户创建成功",
    "data": null
}
```

### 9. 修改用户信息（管理员）
- **接口**：`PUT {{BASE_URL}}/api/v1/admin/users/1`
- **输入**：
```json
{
    "nickname": "修改后的用户",
    "phoneNumber": "13800000004",
    "email": "updated@example.com",
    "gender": 1,
    "status": 1
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "用户信息修改成功",
    "data": null
}
```

### 10. 修改企业状态（管理员）
- **接口**：`PUT {{BASE_URL}}/api/v1/admin/companies/123/status`
- **输入**：
```json
{
    "status": 1
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "企业状态修改成功",
    "data": null
}
```

### 11. 获取企业成员列表
- **接口**：`GET {{BASE_URL}}/api/v1/company/123/members`
- **输入**：
```
username=test&companyRole=1&status=1&page=1&size=10
```
- **输出**：
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "total": 20,
        "pages": 2,
        "current": 1,
        "records": [
            {
                "id": 1,
                "username": "testuser",
                "nickname": "测试用户",
                "companyId": 123,
                "companyRole": 1,
                "status": 1,
                "createdAt": "2024-01-01 10:00:00"
            }
        ]
    }
}
```

### 12. 创建企业成员
- **接口**：`POST {{BASE_URL}}/api/v1/company/123/members`
- **输入**：
```json
{
    "username": "newmember",
    "password": "123456",
    "nickname": "新成员",
    "phoneNumber": "13800000005",
    "email": "member@example.com",
    "gender": 1,
    "companyRole": 1,
    "status": 1
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "成员创建成功",
    "data": null
}
```

### 13. 修改成员角色
- **接口**：`PUT {{BASE_URL}}/api/v1/company/123/members/1/role`
- **输入**：
```json
{
    "companyRole": 2
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "成员权限修改成功",
    "data": null
}
```

### 14. 修改成员信息
- **接口**：`PUT {{BASE_URL}}/api/v1/company/123/members/1`
- **输入**：
```json
{
    "nickname": "修改后的成员",
    "phoneNumber": "13800000006",
    "email": "updated@example.com",
    "gender": 1,
    "status": 1
}
```
- **输出**：
```json
{
    "code": 200,
    "message": "成员信息修改成功",
    "data": null
}
```

### 15. 删除成员
- **接口**：`DELETE {{BASE_URL}}/api/v1/company/123/members/1`
- **输入**：无
- **输出**：
```json
{
    "code": 200,
    "message": "成员删除成功",
    "data": null
}
```
- **错误输出**：
```json
{
    "code": 500,
    "message": "不能删除最后一个企业管理员",
    "data": null
}
```

## 错误码说明

### 1. 认证错误 (401)
```json
{
    "code": 401,
    "message": "未授权",
    "data": null
}
```

### 2. 权限错误 (403)
```json
{
    "code": 403,
    "message": "权限不足",
    "data": null
}
```

### 3. 参数错误 (400)
```json
{
    "code": 400,
    "message": "参数错误",
    "data": null
}
```

### 4. 服务器错误 (500)
```json
{
    "code": 500,
    "message": "服务器内部错误",
    "data": null
}
```

## 测试注意事项

1. 所有需要认证的接口都要带上token
2. 修改密码时确保两次密码一致
3. 删除企业成员时注意不能删除最后一个管理员
4. 分页参数page从1开始
5. 注意检查响应中的code和message 