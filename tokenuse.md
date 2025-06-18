# Token使用指南

## 概述
本文档详细说明如何使用JWT Token访问NBCompany系统的API接口。

## 1. 获取Token

### 登录获取Token
```
POST http://localhost:8080/api/v1/auth/login
Content-Type: application/json

{
    "username": "testuser",
    "password": "123456"
}
```

### 响应示例
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjM0NTY3ODkwLCJleHAiOjE2MzQ1NzE0OTB9.example",
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

## 2. Postman环境配置

### 创建环境变量
1. 在Postman中点击右上角的"Environment"下拉菜单
2. 选择"New"创建新环境（如：NBCompany Local）
3. 添加以下变量：
   - `BASE_URL`: `http://localhost:8080`
   - `TOKEN`: 粘贴从登录响应中获取的token值

### 设置请求头
在需要认证的请求中添加以下请求头：
```
Authorization: Bearer {{TOKEN}}
Content-Type: application/json
```

## 3. API使用示例

### 3.1 获取用户信息
```
GET {{BASE_URL}}/api/v1/user/profile
Authorization: Bearer {{TOKEN}}
```

### 3.2 修改用户信息
```
PUT {{BASE_URL}}/api/v1/user/profile
Authorization: Bearer {{TOKEN}}
Content-Type: application/json

{
    "nickname": "新昵称",
    "phoneNumber": "13800000002",
    "email": "new@example.com",
    "gender": 1
}
```

### 3.3 修改密码
```
PUT {{BASE_URL}}/api/v1/user/password
Authorization: Bearer {{TOKEN}}
Content-Type: application/json

{
    "oldPassword": "123456",
    "newPassword": "654321",
    "confirmNewPassword": "654321"
}
```

### 3.4 获取用户列表（管理员权限）
```
GET {{BASE_URL}}/api/v1/admin/users?page=1&size=10
Authorization: Bearer {{TOKEN}}
```

### 3.5 创建新用户（管理员权限）
```
POST {{BASE_URL}}/api/v1/admin/users
Authorization: Bearer {{TOKEN}}
Content-Type: application/json

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

### 3.6 获取企业成员列表（企业管理员权限）
```
GET {{BASE_URL}}/api/v1/company/123/members?page=1&size=10
Authorization: Bearer {{TOKEN}}
```

### 3.7 创建企业成员（企业管理员权限）
```
POST {{BASE_URL}}/api/v1/company/123/members
Authorization: Bearer {{TOKEN}}
Content-Type: application/json

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

## 4. 分享Token给同事

### 方法一：导出Postman环境
1. 在Postman中右键点击环境
2. 选择"Export"
3. 保存为JSON文件
4. 发送给同事
5. 同事在Postman中点击"Import"导入

### 方法二：提供环境变量配置
直接告诉同事在Postman中设置：
```
BASE_URL: http://localhost:8080
TOKEN: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjM0NTY3ODkwLCJleHAiOjE2MzQ1NzE0OTB9.example
```

### 方法三：提供完整的请求示例
将包含token的完整请求示例发送给同事，让他们直接复制使用。

## 5. 错误处理

### 5.1 401 Unauthorized
**原因**：token过期或无效
**解决方案**：重新登录获取新的token

### 5.2 403 Forbidden
**原因**：权限不足
**解决方案**：检查用户角色和权限设置

### 5.3 400 Bad Request
**原因**：请求参数错误
**解决方案**：检查请求体格式和参数

### 5.4 500 Internal Server Error
**原因**：服务器内部错误
**解决方案**：检查服务器日志，联系管理员

## 6. 重要注意事项

### 6.1 Token安全
- 不要将token分享给不相关的人员
- 定期更换token
- 不要在代码中硬编码token

### 6.2 Token有效期
- JWT token通常有有效期（默认24小时）
- 过期后需要重新登录获取新token
- 建议在token过期前主动刷新

### 6.3 环境一致性
- 确保所有同事都使用相同的BASE_URL
- 确保使用相同的API版本

### 6.4 权限管理
- 不同用户可能有不同的权限
- 注意API访问权限限制
- 管理员权限的用户可以访问更多功能

## 7. 常见问题

### Q: Token过期了怎么办？
A: 重新调用登录接口获取新的token

### Q: 为什么有些API访问不了？
A: 检查用户权限，某些API需要特定的用户角色

### Q: 如何知道token是否有效？
A: 尝试调用一个简单的API（如获取用户信息）来验证

### Q: 可以同时使用多个token吗？
A: 可以，但建议使用同一个token以保持一致性

## 8. 最佳实践

1. **统一管理**：使用Postman环境变量统一管理token
2. **定期更新**：定期更新token以确保安全
3. **权限最小化**：只给用户必要的权限
4. **日志记录**：记录token使用情况以便追踪
5. **错误处理**：实现完善的错误处理机制

## 9. Java代码中获取Token信息

### 9.1 在Controller中获取当前用户ID

```java
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    
    @GetMapping("/profile")
    public ApiResponse<UserProfileResponse> getProfile(@RequestAttribute Long userId) {
        // userId 是从JWT token中解析出来的用户ID
        // 由JwtAuthFilter自动设置到request attributes中
        return userService.getProfile(userId);
    }
    
    @PutMapping("/profile")
    public ApiResponse<Void> updateProfile(@RequestAttribute Long userId, 
                                         @RequestBody UpdateProfileRequest request) {
        // 使用从token中获取的userId
        return userService.updateProfile(userId, request);
    }
}
```

### 9.2 在Service中获取用户信息

```java
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private SysUserMapper userMapper;
    
    public ApiResponse<UserProfileResponse> getProfile(Long userId) {
        // 使用从token中获取的userId查询用户信息
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }
        
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        // ... 设置其他字段
        
        return ApiResponse.success(response);
    }
}
```

### 9.3 权限检查示例

```java
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    
    @GetMapping("/users")
    public ApiResponse<PageResponse<SysUser>> getUsers(@RequestAttribute Long userId,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        // 检查用户是否有管理员权限
        SysUser currentUser = userMapper.selectById(userId);
        if (currentUser == null || currentUser.getUserType() != 0) {
            return ApiResponse.error("权限不足");
        }
        
        // 执行管理员操作
        Page<SysUser> userPage = userMapper.selectPage(page, size);
        return ApiResponse.success(PageResponse.from(userPage));
    }
}
```

### 9.4 企业管理员权限检查

```java
@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {
    
    @GetMapping("/{companyId}/members")
    public ApiResponse<PageResponse<SysUser>> getCompanyMembers(@RequestAttribute Long userId,
                                                               @PathVariable Long companyId,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        // 检查用户是否是该企业的管理员
        SysUser currentUser = userMapper.selectById(userId);
        if (currentUser == null || 
            !currentUser.getCompanyId().equals(companyId) || 
            currentUser.getCompanyRole() != 1) {
            return ApiResponse.error("权限不足");
        }
        
        // 执行企业管理员操作
        Page<SysUser> membersPage = userMapper.selectByCompanyId(companyId, page, size);
        return ApiResponse.success(PageResponse.from(membersPage));
    }
}
```

### 9.5 工具类获取当前用户

```java
@Component
public class UserContext {
    
    public static Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Object userId = request.getAttribute("userId");
            if (userId != null) {
                return (Long) userId;
            }
        }
        return null;
    }
    
    public static SysUser getCurrentUser() {
        Long userId = getCurrentUserId();
        if (userId != null) {
            // 这里需要注入SysUserMapper，可以通过ApplicationContext获取
            return SpringContextHolder.getBean(SysUserMapper.class).selectById(userId);
        }
        return null;
    }
}
```

### 9.6 使用示例

```java
@RestController
public class SomeController {
    
    @GetMapping("/some-endpoint")
    public ApiResponse<String> someMethod() {
        // 获取当前用户ID
        Long userId = UserContext.getCurrentUserId();
        
        // 获取当前用户完整信息
        SysUser currentUser = UserContext.getCurrentUser();
        
        // 根据用户信息进行业务逻辑处理
        if (currentUser.getUserType() == 0) {
            // 平台管理员逻辑
        } else if (currentUser.getCompanyRole() == 1) {
            // 企业管理员逻辑
        } else {
            // 普通用户逻辑
        }
        
        return ApiResponse.success("操作成功");
    }
}
```

## 10. 联系支持

如果在使用过程中遇到问题，请联系系统管理员或查看系统日志获取更多信息。 