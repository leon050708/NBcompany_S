为您的 API 接口提供测试用例。这些测试用例将基于您提供的接口文档和先前完善的代码逻辑。为了方便测试，我将使用 Postman 格式来展示请求细节，并提供预期结果。

在运行这些测试用例之前，请确保您的 Spring Boot 应用程序已启动，并且数据库已通过 `test.sql` 文件初始化。

**重要提示：**

* **URL 前缀**：所有 URL 都将以 `http://localhost:8080` 为基础。
* **登录会话**：Postman 在成功登录 `POST /api/v1/auth/login` 后会自动管理 `JSESSIONID` Cookie，后续受保护的请求会使用该 Cookie 进行认证。
* **`admin` 用户密码**：在 `test.sql` 中，`admin` 用户的密码哈希值是 `$2a$10$fL3n3v9v5b.npL/E/e4BGe.xRz.w6A7D9E0b6A5A4A3A2A1A0A`，对应的明文密码是 `password`。
* **企业和用户 ID**：测试用例中使用的企业 ID 和用户 ID 假定为数据库中已存在的或新创建的 ID。

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
                "companyId": <新创建的企业ID, 例如4>,
                "companyName": "新建科技公司",
                "status": 0 // 待审核状态
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
    * Response Body (JSON): 包含所有企业的分页数据，至少包含 `数智未来科技有限公司`、`绿色能源集团`、`新风医疗股份有限公司` 和 `新建科技公司` (如果已注册)。
* **URL 2 (带关键词查询)**：`http://localhost:8080/api/v1/companies?keyword=科技`
* **预期结果 2**：
    * Status Code: `200 OK`
    * Response Body (JSON): 包含名称中带有“科技”的企业（如 `数智未来科技有限公司`、`新建科技公司`）的分页数据。
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
        "companyId": 1 // 假设已有一个激活的企业ID，例如“数智未来科技有限公司”的ID为1
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

* **目的**：验证用户可以成功登录并获取会话。
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
                "token": "your_jwt_token_here", // 实际会返回一个 JWT
                "userInfo": {
                    "id": 1,
                    "username": "admin",
                    "nickname": "平台超级管理员",
                    "userType": 2, // 平台超级管理员
                    "companyId": null,
                    "companyName": null,
                    "companyRole": null
                }
            }
        }
        ```
    * **Postman**：Postman 会自动保存响应头中的 `Set-Cookie: JSESSIONID=...`，用于后续的请求。

---

### **3. 当前用户个人信息管理接口测试用例 (需要认证)**

请确保在运行这些测试用例之前，已通过 **2.1 用户登录** 步骤成功登录。

#### **3.1 获取当前用户个人信息 (GET /api/v1/user/profile)**

* **目的**：验证已登录用户可以获取自己的个人信息。
* **方法**：`GET`
* **URL**：`http://localhost:8080/api/v1/user/profile`
* **Header**：(Postman 会自动带上 `JSESSIONID` Cookie)
* **预期结果**：
    * Status Code: `200 OK`
    * Response Body (JSON): 返回当前登录用户的详细信息，例如：
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
                "createdAt": "2024-01-01 10:00:00" // 实际时间
            }
        }
        ```

#### **3.2 修改当前用户基本资料 (PUT /api/v1/user/profile)**

* **目的**：验证已登录用户可以修改自己的基本资料。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/user/profile`
* **Header**：`Content-Type: application/json` (Postman 会自动带上 `JSESSIONID` Cookie)
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
* **Header**：`Content-Type: application/json` (Postman 会自动带上 `JSESSIONID` Cookie)
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

### **4. 平台超级管理员接口测试用例 (需要 `admin` 账户认证)**

请确保在运行这些测试用例之前，已通过 **2.1 用户登录** 步骤使用 `admin` 账户成功登录。

#### **4.1 平台超级管理员获取用户列表 (GET /api/v1/admin/users)**

* **目的**：验证平台超级管理员可以获取所有用户列表，并支持多条件筛选和分页。
* **方法**：`GET`
* **URL 1 (无参数)**：`http://localhost:8080/api/v1/admin/users`
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
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "username": "admin_created_user",
        "password": "password_for_new_user",
        "nickname": "管理员创建",
        "phoneNumber": "13600000000",
        "email": "admin@created.com",
        "gender": 1,
        "userType": 1,      // 企业用户
        "companyId": 2,     // 隶属于绿色能源集团
        "companyRole": 2,   // 企业管理员
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
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "nickname": "王五-已激活",
        "status": 1,        // 从禁用改为正常
        "companyRole": 2    // 提升为企业管理员
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
    * **验证修改用户类型**：
        * **URL**: `http://localhost:8080/api/v1/admin/users/2` (假设 `张三` 的 ID 是 `2`)
        * **Body (raw, JSON)**:
            ```json
            {
                "userType": 2 // 将张三提升为平台超级管理员
            }
            ```
        * **预期结果**: `200 OK`。数据库验证 `user_type` 变为 `2`，`company_id` 和 `company_role` 变为 `null`。

#### **4.4 平台超级管理员审核企业状态 (PUT /api/v1/admin/companies/{companyId}/status)**

* **目的**：验证平台超级管理员可以审核企业状态（激活或禁用企业）。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/admin/companies/3/status` (假设 `新风医疗股份有限公司` 的 ID 是 `3`，且当前状态为 `0`)
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "status": 1 // 将企业状态改为正常
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

### **5. 企业管理员接口测试用例 (需要企业管理员账户认证)**

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
    * **预期结果**：`200 OK`，并获取 `JSESSIONID`。

接下来，所有测试用例都假设您已登录为 `张三` (企业ID `1`)。

#### **5.1 企业管理员获取成员列表 (GET /api/v1/company/{companyId}/members)**

* **目的**：验证企业管理员可以获取自己企业下的成员列表，并支持筛选。
* **方法**：`GET`
* **URL 1 (获取 ID 为 1 的企业成员)**：`http://localhost:8080/api/v1/company/1/members`
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
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "username": "company_employee",
        "password": "emp_password",
        "nickname": "公司员工",
        "phoneNumber": "13100001111",
        "email": "emp@company1.com",
        "gender": 0,
        "companyRole": 1, // 普通员工
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
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "companyRole": 2 // 将其提升为企业管理员
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
    * **错误用例 (修改平台超级管理员)**：
        * **URL**：`http://localhost:8080/api/v1/company/1/members/1/role` (尝试修改 `admin` 用户)
        * **Body**: 同上
        * **预期结果**: Status Code: `403 Forbidden` 或 `500 Internal Server Error` (取决于业务异常处理)。

#### **5.4 企业管理员修改成员信息 (PUT /api/v1/company/{companyId}/members/{memberId})**

* **目的**：验证企业管理员可以修改其企业下成员的基本信息和状态。
* **方法**：`PUT`
* **URL**：`http://localhost:8080/api/v1/company/1/members/<新创建员工的ID>` (例如，假设新创建员工 ID 为 5)
* **Header**：`Content-Type: application/json`
* **Body (raw, JSON)**：
    ```json
    {
        "nickname": "公司员工-更新",
        "email": "updated_emp@company1.com",
        "status": 0 // 禁用该成员
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
    * **错误用例 (删除平台超级管理员)**：
        * **URL**：`http://localhost:8080/api/v1/company/1/members/1` (尝试删除 `admin` 用户)
        * **预期结果**: Status Code: `403 Forbidden` 或 `500 Internal Server Error`。

---