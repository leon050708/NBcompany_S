# NBCompany 企业管理系统 API 测试文档

为您的 API 接口提供测试用例。这些测试用例将基于您提供的接口文档和先前完善的代码逻辑。为了方便测试，我将使用 Postman 格式来展示请求细节，并提供预期结果。

在运行这些测试用例之前，请确保您的 Spring Boot 应用程序已启动，并且数据库已通过 `test.sql` 文件初始化。

**重要提示：**

* **URL 前缀**：所有 URL 都将以 `http://localhost:8080` 为基础。
* **JWT Token 认证**：系统使用 JWT Token 进行认证，登录成功后需要在后续请求的 Header 中添加 `Authorization: Bearer <token>`。
* **`admin` 用户密码**：在 `test.sql` 中，`admin` 用户的密码哈希值是 `$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A`，对应的明文密码是 `password`。
* **企业和用户 ID**：测试用例中使用的企业 ID 和用户 ID 假定为数据库中已存在的或新创建的 ID。

---

## **JWT Token 使用说明**

### **1. 获取 Token**
登录成功后，响应中会返回 JWT Token：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": { ... }
  }
}
```

### **2. 在 Postman 中使用 Token**
1. 在 Postman 中，选择需要认证的请求
2. 在 **Headers** 标签页中添加：
   - **Key**: `Authorization`
   - **Value**: `Bearer <your_token>`
   - 例如：`Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

### **3. 在代码中使用 Token**
```java
// 在请求头中添加 Authorization
HttpHeaders headers = new HttpHeaders();
headers.setBearerAuth(token);
HttpEntity<String> entity = new HttpEntity<>(headers);

// 发送请求
ResponseEntity<String> response = restTemplate.exchange(
    "http://localhost:8080/api/v1/user/profile",
    HttpMethod.GET,
    entity,
    String.class
);
```

### **4. Token 有效期**
JWT Token 默认有效期为 24 小时，过期后需要重新登录获取新的 Token。

---

### **1. 公共接口测试用例 (无需认证)**

#### **1.1 企业注册 (POST /api/v1/auth/register/company)**

* **目的**：验证新企业可以成功注册并处于待审核状态。
* **方法**：`POST`
* **URL**：`http://localhost:8080/api/v1/auth/register/company`
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "companyName": "新建科技公司",
        "contactPerson": "王小明",
        "contactPhone": "13912345678",
        "contactEmail": "wangxm@newtech.com"
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "企业注册成功，请等待平台管理员审核",
            "data": {
                "companyId": 4,
                "companyName": "新建科技公司"
            }
        }
        ```
    * **数据库验证**：`sys_company` 表中应有一条 `company_name` 为 "新建科技公司" 的记录，`status` 为 `0`。

#### **1.2 获取企业列表 (GET /api/v1/companies)**

* **目的**：验证可以获取企业列表，并支持模糊查询和分页。
* **方法**：`GET`
* **URL 1 (无参数)**：`http://localhost:8080/api/v1/companies`
* **预期结果 1**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "获取成功",
            "data": {
                "current": 1,
                "size": 10,
                "total": 4,
                "pages": 1,
                "records": [
                    {
                        "id": 1,
                        "companyName": "数智未来科技有限公司",
                        "contactPerson": "张三",
                        "contactPhone": "13800000001",
                        "contactEmail": "zhangsan@tech.com",
                        "status": 1,
                        "createdAt": "2024-01-01 10:00:00",
                        "updatedAt": "2024-01-01 10:00:00"
                    },
                    {
                        "id": 2,
                        "companyName": "绿色能源集团",
                        "contactPerson": "李四",
                        "contactPhone": "13800000002",
                        "contactEmail": "lisi@energy.com",
                        "status": 1,
                        "createdAt": "2024-01-01 10:00:00",
                        "updatedAt": "2024-01-01 10:00:00"
                    }
                ]
            }
        }
        ```
* **URL 2 (带关键词查询)**：`http://localhost:8080/api/v1/companies?keyword=科技`
* **预期结果 2**：
    * Status Code: `200 OK`
    * Response Body (JSON): 包含名称中带有"科技"的企业（如 `数智未来科技有限公司`、`新建科技公司`）的分页数据。
* **URL 3 (带分页参数)**：`http://localhost:8080/api/v1/companies?page=1&size=2`
* **预期结果 3**：
    * Status Code: `200 OK`
    * Response Body (JSON): 包含第一页的2条企业数据。

#### **1.3 用户注册 (POST /api/v1/auth/register/user)**

* **目的**：验证新用户可以注册并选择所属企业。
* **方法**：`POST`
* **URL**：`http://localhost:8080/api/v1/auth/register/user`
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "username": "newemployee",
        "password": "password123",
        "nickname": "新员工",
        "phoneNumber": "13312345678",
        "email": "newemployee@example.com",
        "gender": 1,
        "companyId": 1
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "用户注册成功，请等待企业管理员分配权限",
            "data": null
        }
        ```
    * **数据库验证**：`sys_user` 表中应有一条 `username` 为 "newemployee" 的记录，`user_type` 为 `1`，`company_id` 为 `1`，`company_role` 为 `1`，`status` 为 `1`。

---

### **2. 用户认证接口测试用例**

#### **2.1 用户登录 (POST /api/v1/auth/login)**

* **目的**：验证用户可以成功登录并获取 JWT Token。
* **方法**：`POST`
* **URL**：`http://localhost:8080/api/v1/auth/login`
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "username": "admin",
        "password": "password"
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "登录成功",
            "data": {
                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYzNTY4OTYwMCwiZXhwIjoxNjM1Nzc2MDAwfQ.example_signature",
                "userInfo": {
                    "id": 1,
                    "username": "admin",
                    "nickname": "平台超级管理员",
                    "userType": 2,
                    "companyId": null,
                    "companyName": null,
                    "companyRole": null
                }
            }
        }
        ```
    * **重要**：保存返回的 `token` 值，用于后续需要认证的请求。

---

### **3. 当前用户个人信息管理接口测试用例 (需要 JWT Token 认证)**

请确保在运行这些测试用例之前，已通过 **2.1 用户登录** 步骤成功登录并获取 JWT Token。

#### **3.1 获取当前用户个人信息 (GET /api/v1/user/profile)**

* **目的**：验证已登录用户可以获取自己的个人信息。
* **方法**：`GET`
* **URL**：`http://localhost:8080/api/v1/user/profile`
* **Header**：
    - `Authorization: Bearer <your_jwt_token>`
    - `Content-Type: application/json`
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "获取成功",
            "data": {
                "id": 1,
                "username": "admin",
                "nickname": "平台超级管理员",
                "phoneNumber": "18888888888",
                "email": "admin@platform.com",
                "gender": 1,
                "userType": 2,
                "companyId": null,
                "companyName": null,
                "companyRole": null,
                "status": 1,
                "createdAt": "2024-01-01 10:00:00"
            }
        }
        ```

#### **3.2 修改当前用户基本资料 (PUT /api/v1/user/profile)**

* **目的**：验证已登录用户可以修改自己的基本资料。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/user/profile`
* **Header**：
    - `Authorization: Bearer <your_jwt_token>`
    - `Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "nickname": "管理员_更新",
        "phoneNumber": "19912345678",
        "email": "admin_updated@platform.com",
        "gender": 2
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "修改成功",
            "data": null
        }
        ```
    * **数据库验证**：`sys_user` 表中 ID 为 `1` 的用户记录应已更新。
    * **二次验证**：再次调用 `GET /api/v1/user/profile` 验证信息是否已更新。

#### **3.3 修改当前用户密码 (PUT /api/v1/user/password)**

* **目的**：验证已登录用户可以修改自己的密码。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/user/password`
* **Header**：
    - `Authorization: Bearer <your_jwt_token>`
    - `Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "oldPassword": "password",
        "newPassword": "new_secure_password",
        "confirmNewPassword": "new_secure_password"
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "密码修改成功",
            "data": null
        }
        ```
    * **二次验证**：尝试用旧密码登录，应失败；尝试用新密码登录，应成功。

---

### **4. 平台超级管理员接口测试用例 (需要 `admin` 账户 JWT Token 认证)**

请确保在运行这些测试用例之前，已通过 **2.1 用户登录** 步骤使用 `admin` 账户成功登录并获取 JWT Token。

#### **4.1 平台超级管理员获取用户列表 (GET /api/v1/admin/users)**

* **目的**：验证平台超级管理员可以获取所有用户列表，并支持多条件筛选和分页。
* **方法**：`GET`
* **URL 1 (无参数)**：`http://localhost:8080/api/v1/admin/users`
* **Header**：
    - `Authorization: Bearer <admin_jwt_token>`
    - `Content-Type: application/json`
* **预期结果 1**：
    * Status Code: `200 OK`
    * Response Body (JSON): 包含所有用户的分页数据。
* **URL 2 (按企业ID过滤)**：`http://localhost:8080/api/v1/admin/users?companyId=1`
* **预期结果 2**：
    * Status Code: `200 OK`
    * Response Body (JSON): 仅包含 `companyId` 为 `1` 的用户，例如 `张三`。
* **URL 3 (按用户类型和状态过滤)**：`http://localhost:8080/api/v1/admin/users?userType=1&status=0`
* **预期结果 3**：
    * Status Code: `200 OK`
    * Response Body (JSON): 仅包含用户类型为企业用户 (`1`) 且状态为禁用 (`0`) 的用户，例如 `王五`。

#### **4.2 平台超级管理员创建用户 (POST /api/v1/admin/users)**

* **目的**：验证平台超级管理员可以创建新用户，并指定其企业和角色。
* **方法**：`POST`
* **URL**：`http://localhost:8080/api/v1/admin/users`
* **Header**：
    - `Authorization: Bearer <admin_jwt_token>`
    - `Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "username": "admin_created_user",
        "password": "password_for_new_user",
        "nickname": "管理员创建",
        "phoneNumber": "13600000000",
        "email": "admin@created.com",
        "gender": 1,
        "userType": 1,
        "companyId": 2,
        "companyRole": 2,
        "status": 1
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "用户创建成功",
            "data": null
        }
        ```
    * **数据库验证**：`sys_user` 表中应新增一条记录。

#### **4.3 平台超级管理员修改用户信息 (PUT /api/v1/admin/users/{userId})**

* **目的**：验证平台超级管理员可以修改任意用户的基本信息、用户类型和企业角色。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/admin/users/4` (假设 `王五` 的 ID 是 `4`)
* **Header**：
    - `Authorization: Bearer <admin_jwt_token>`
    - `Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "nickname": "王五-已激活",
        "status": 1,
        "companyRole": 2
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "用户信息修改成功",
            "data": null
        }
        ```
    * **数据库验证**：`sys_user` 表中 ID 为 `4` 的用户记录应已更新。

#### **4.4 平台超级管理员审核企业状态 (PUT /api/v1/admin/companies/{companyId}/status)**

* **目的**：验证平台超级管理员可以审核企业状态（激活或禁用企业）。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/admin/companies/3/status` (假设 `新风医疗股份有限公司` 的 ID 是 `3`，且当前状态为 `0`)
* **Header**：
    - `Authorization: Bearer <admin_jwt_token>`
    - `Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "status": 1
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "企业状态修改成功",
            "data": null
        }
        ```
    * **数据库验证**：`sys_company` 表中 ID 为 `3` 的企业记录 `status` 应已更新为 `1`。

---

### **5. 企业管理员接口测试用例 (需要企业管理员账户 JWT Token 认证)**

首先，需要一个企业管理员账户进行登录。我们可以使用 `test.sql` 中的 `张三` (`zhangsan`)，其 `company_id` 为 `1`，`company_role` 为 `2` (企业管理员)。

1.  **登录 `张三` 账户**：
    * **方法**：`POST`
    * **URL**：`http://localhost:8080/api/v1/auth/login`
    * **Body (raw, JSON)**：
        ```json
        {
            "username": "zhangsan",
            "password": "password"
        }
        ```
    * **预期结果**：`200 OK`，并获取 JWT Token。

接下来，所有测试用例都假设您已登录为 `张三` (企业ID `1`) 并获取了 JWT Token。

#### **5.1 企业管理员获取成员列表 (GET /api/v1/company/{companyId}/members)**

* **目的**：验证企业管理员可以获取自己企业下的成员列表，并支持筛选。
* **方法**：`GET`
* **URL 1 (获取 ID 为 1 的企业成员)**：`http://localhost:8080/api/v1/company/1/members`
* **Header**：
    - `Authorization: Bearer <zhangsan_jwt_token>`
    - `Content-Type: application/json`
* **预期结果 1**：
    * Status Code: `200 OK`
    * Response Body (JSON): 包含 `companyId` 为 `1` 的用户列表，例如 `张三`。
* **URL 2 (按角色过滤)**：`http://localhost:8080/api/v1/company/1/members?companyRole=1`
* **预期结果 2**：
    * Status Code: `200 OK`
    * Response Body (JSON): 仅包含 `companyId` 为 `1` 且 `companyRole` 为 `1` (普通员工) 的成员。

#### **5.2 企业管理员创建成员 (POST /api/v1/company/{companyId}/members)**

* **目的**：验证企业管理员可以在自己企业下创建新成员。
* **方法**：`POST`
* **URL**：`http://localhost:8080/api/v1/company/1/members` (为企业 ID `1` 创建成员)
* **Header**：
    - `Authorization: Bearer <zhangsan_jwt_token>`
    - `Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "username": "company_employee",
        "password": "emp_password",
        "nickname": "公司员工",
        "phoneNumber": "13100001111",
        "email": "emp@company1.com",
        "gender": 0,
        "companyRole": 1,
        "status": 1
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "成员创建成功",
            "data": null
        }
        ```
    * **数据库验证**：`sys_user` 表中应新增一条记录，`company_id` 为 `1`。

#### **5.3 企业管理员修改成员角色 (PUT /api/v1/company/{companyId}/members/{memberId}/role)**

* **目的**：验证企业管理员可以修改其企业下成员的企业内部角色。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/company/1/members/<新创建员工的ID>/role` (例如，假设新创建员工 ID 为 5)
* **Header**：
    - `Authorization: Bearer <zhangsan_jwt_token>`
    - `Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "companyRole": 2
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "成员权限修改成功",
            "data": null
        }
        ```
    * **数据库验证**：新创建员工的 `company_role` 应更新为 `2`。

#### **5.4 企业管理员修改成员信息 (PUT /api/v1/company/{companyId}/members/{memberId})**

* **目的**：验证企业管理员可以修改其企业下成员的基本信息和状态。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/company/1/members/<新创建员工的ID>` (例如，假设新创建员工 ID 为 5)
* **Header**：
    - `Authorization: Bearer <zhangsan_jwt_token>`
    - `Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "nickname": "公司员工-更新",
        "email": "updated_emp@company1.com",
        "status": 0
    }
    ```
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "成员信息修改成功",
            "data": null
        }
        ```
    * **数据库验证**：新创建员工的 `nickname`、`email`、`status` 应已更新。

#### **5.5 企业管理员删除成员 (DELETE /api/v1/company/{companyId}/members/{memberId})**

* **目的**：验证企业管理员可以删除其企业下的成员。
* **方法**：`DELETE`
* **URL**：`http://localhost:8080/api/v1/company/1/members/<新创建员工的ID>` (例如，假设新创建员工 ID 为 5)
* **Header**：
    - `Authorization: Bearer <zhangsan_jwt_token>`
    - `Content-Type: application/json`
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON):
        ```json
        {
            "code": 200,
            "message": "成员删除成功",
            "data": null
        }
        ```
    * **数据库验证**：`sys_user` 表中 ID 为 `5` 的记录应已被删除。

---

## **错误处理测试用例**

### **1. 认证失败测试**

#### **1.1 未提供 Token**
* **方法**：`GET`
* **URL**：`http://localhost:8080/api/v1/user/profile`
* **Header**：无 Authorization Header
* **预期结果**：
    * Status Code: `401 Unauthorized`
    * Response Body (JSON):
        ```json
        {
            "code": 401,
            "message": "未授权访问",
            "data": null
        }
        ```

#### **1.2 Token 无效**
* **方法**：`GET`
* **URL**：`http://localhost:8080/api/v1/user/profile`
* **Header**：`Authorization: Bearer invalid_token`
* **预期结果**：
    * Status Code: `401 Unauthorized`
    * Response Body (JSON):
        ```json
        {
            "code": 401,
            "message": "Token无效",
            "data": null
        }
        ```

#### **1.3 Token 过期**
* **方法**：`GET`
* **URL**：`http://localhost:8080/api/v1/user/profile`
* **Header**：`Authorization: Bearer expired_token`
* **预期结果**：
    * Status Code: `401 Unauthorized`
    * Response Body (JSON):
        ```json
        {
            "code": 401,
            "message": "Token已过期",
            "data": null
        }
        ```

### **2. 权限不足测试**

#### **2.1 普通用户访问管理员接口**
* **方法**：`GET`
* **URL**：`http://localhost:8080/api/v1/admin/users`
* **Header**：`Authorization: Bearer <普通用户token>`
* **预期结果**：
    * Status Code: `403 Forbidden`
    * Response Body (JSON):
        ```json
        {
            "code": 403,
            "message": "权限不足",
            "data": null
        }
        ```

### **3. 业务逻辑错误测试**

#### **3.1 企业不存在**
* **方法**：`GET`
* **URL**：`http://localhost:8080/api/v1/company/999/members`
* **Header**：`Authorization: Bearer <valid_token>`
* **预期结果**：
    * Status Code: `404 Not Found`
    * Response Body (JSON):
        ```json
        {
            "code": 404,
            "message": "企业不存在",
            "data": null
        }
        ```

#### **3.2 用户不存在**
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/admin/users/999`
* **Header**：`Authorization: Bearer <admin_token>`
* **Body (raw, JSON)**：
    ```json
    {
        "nickname": "不存在的用户"
    }
    ```
* **预期结果**：
    * Status Code: `404 Not Found`
    * Response Body (JSON):
        ```json
        {
            "code": 404,
            "message": "用户不存在",
            "data": null
        }
        ```

---

## **测试环境准备**

### **1. 数据库初始化**
确保已执行 `test.sql` 文件，创建测试数据：
```sql
-- 创建企业
INSERT INTO sys_company (company_name, contact_person, contact_phone, contact_email, status) VALUES
('数智未来科技有限公司', '张三', '13800000001', 'zhangsan@tech.com', 1),
('绿色能源集团', '李四', '13800000002', 'lisi@energy.com', 1),
('新风医疗股份有限公司', '王五', '13800000003', 'wangwu@medical.com', 0);

-- 创建用户
INSERT INTO sys_user (username, password, nickname, phone_number, email, gender, user_type, company_id, company_role, status) VALUES
('admin', '$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A', '平台超级管理员', '18888888888', 'admin@platform.com', 1, 2, NULL, NULL, 1),
('zhangsan', '$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A', '张三', '13800000001', 'zhangsan@tech.com', 1, 1, 1, 2, 1),
('lisi', '$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A', '李四', '13800000002', 'lisi@energy.com', 1, 1, 2, 2, 1),
('wangwu', '$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A', '王五', '13800000003', 'wangwu@medical.com', 1, 1, 3, 1, 0);
```

### **2. 应用程序启动**
```bash
cd /Users/leon/Desktop/javaProject/NBCompany_spring
mvn spring-boot:run
```

### **3. 测试工具**
推荐使用 Postman 进行 API 测试，可以方便地管理 JWT Token 和请求头。

---

## **注意事项**

1. **Token 管理**：每次登录后都会生成新的 JWT Token，需要及时更新测试请求中的 Token。
2. **数据隔离**：测试过程中创建的数据可能会影响其他测试用例，建议在测试完成后清理测试数据。
3. **错误处理**：系统会对各种异常情况进行处理，返回相应的错误码和错误信息。
4. **权限控制**：不同角色的用户只能访问其权限范围内的接口。
5. **数据验证**：系统会对输入数据进行验证，不符合要求的数据会被拒绝。

---

*最后更新时间：2024年1月*