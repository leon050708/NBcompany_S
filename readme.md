Of course. Here are example test cases for each of the `SysUser` API endpoints you've built.

---
## **1. 创建用户 (Create User)**

This tests the `POST /api/users` endpoint.

### **测试用例 1.1: 完整信息注册**
This is the standard case where the front-end provides all relevant information.

* **Method**: `POST`
* **URL**: `http://localhost:8080/api/users`
* **Body (JSON)**:
    ```json
    {
        "username": "zhangwei",
        "password": "password123",
        "nickname": "张伟",
        "phoneNumber": "13811112222",
        "email": "zhangwei@example.com",
        "gender": 1,
        "companyId": 1,
        "companyRole": 1,
        "status": 1,
        "userType": 1
    }
    ```

### **测试用例 1.2: 最简信息注册**
This case tests if your backend logic (like **方案 B** from the previous answer) correctly sets default values when the frontend only provides the bare minimum.

* **Method**: `POST`
* **URL**: `http://localhost:8080/api/users`
* **Body (JSON)**:
    ```json
    {
        "username": "lisi",
        "password": "a_strong_password",
        "nickname": "李四"
    }
    ```

---
## **2. 获取用户 (Read User)**

This tests the `GET` endpoints. No request body is needed.

### **测试用例 2.1: 根据 ID 获取单个用户**

* **Method**: `GET`
* **URL**: `http://localhost:8080/api/users/1`
  *(Assuming a user with ID `1` exists)*

### **测试用例 2.2: 获取用户列表（分页）**

* **Method**: `GET`
* **URL**: `http://localhost:8080/api/users?pageNum=1&pageSize=5`
  *(This requests the first page, with 5 users per page)*

---
## **3. 更新用户 (Update User)**

This tests the `PUT /api/users/{id}` endpoint. The request body typically contains only the fields that need to be changed.

### **测试用例 3.1: 更新部分信息**
This example updates the nickname, phone number, and company role for the user with ID `2`.

* **Method**: `PUT`
* **URL**: `http://localhost:8080/api/users/2`
* **Body (JSON)**:
    ```json
    {
        "nickname": "李四-更新",
        "phoneNumber": "13987654321",
        "companyRole": 2
    }
    ```

---
## **4. 删除用户 (Delete User)**

This tests the `DELETE /api/users/{id}` endpoint. No request body is needed.

### **测试用例 4.1: 删除指定用户**

* **Method**: `DELETE`
* **URL**: `http://localhost:8080/api/users/3`
  *(This attempts to delete the user with ID `3`)*

测试用例


方案一：【临时/开发阶段】暂时允许所有 API 访问
如果您现在只是想测试您的 Controller-Service-DAO 流程是否通畅，不希望被登录验证所干扰，可以修改安全配置，暂时“放行”所有或特定的 API 路径。

这是推荐的调试方法。

请修改您项目中的 SecurityConfig.java 文件，代码如下：

代码段

package com.example.myproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. 禁用 CSRF（对于无状态的 REST API 是标准操作）
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. 配置 URL 的授权规则
            .authorizeHttpRequests(authz -> authz
                // 允许对 /api/users/** 下的所有路径进行未经身份验证的访问
                .requestMatchers("/api/users/**").permitAll()
                // 你可以更精细地控制，比如只允许注册（POST）
                // .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                // 除了上面放行的路径，所有其他请求都需要身份验证
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
说明:

csrf(AbstractHttpConfigurer::disable): 禁用了 CSRF 保护，这对于提供给第三方客户端（如 Postman、手机 App）的 REST API 来说是必要的。
requestMatchers("/api/users/**").permitAll(): 这是核心配置。它告诉 Spring Security，任何匹配 /api/users/ 后面跟任意路径的请求（例如 /api/users/1, /api/users），都允许 (permitAll) 访问，无需登录。
anyRequest().authenticated(): 这是一个兜底规则，确保未来您添加的其他接口（如 /api/orders/**）默认仍然是受保护的。
修改完这个配置后，重启您的应用，再次用 Postman 访问 /api/users 就不会再报 401 错误了。

方案二：【测试阶段】在 Postman 中提供认证信息
如果您想保持安全配置不变，并在一个受保护的环境下进行测试，您可以在 Postman 中提供 Spring Security 默认生成的用户名和密码。

启动您的 Spring Boot 应用。

查看控制台 (Console)。您会看到一行类似下面这样的日志，其中包含一个随机生成的密码：

Using generated security password: a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6
打开 Postman，在您的请求中：

切换到 Authorization 标签页。
Type 选择 Basic Auth。
Username 输入 user (这是默认用户名)。
Password 输入您在控制台看到的那一长串随机密码。
现在再发送请求，就可以成功访问了。