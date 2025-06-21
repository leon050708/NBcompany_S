# Excel导出功能测试指南

## 功能说明
课程管理模块支持将课程列表导出为Excel文件，方便用户进行数据分析和备份。

## 权限说明
- **管理员用户**：可以导出所有课程（任何状态）
- **普通用户**：可以导出已发布的课程和自己上传的课程（不论审核状态）

## API接口

### 导出课程列表
- **URL**: `GET /api/courses/export`
- **Headers**: 
  - `Authorization: Bearer {token}`
  - `Accept: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- **Query Parameters**:
  - `status` (可选): 课程状态过滤
    - `0`: 待审核
    - `1`: 已发布  
    - `2`: 审核未通过
  - `companyId` (可选): 企业ID过滤
  - `keyword` (可选): 关键词搜索

## 测试步骤

### 1. 使用Postman测试

#### 设置请求
1. 选择 `GET` 方法
2. URL: `http://localhost:8080/api/courses/export`
3. Headers:
   ```
   Authorization: Bearer {your_jwt_token}
   Accept: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
   ```

#### 发送请求
1. 点击 "Send" 按钮
2. 在响应区域右键选择 "Send and Download"
3. 选择保存位置和文件名（建议使用 `.xlsx` 扩展名）

### 2. 使用curl测试
```bash
curl -X GET "http://localhost:8080/api/courses/export" \
  -H "Authorization: Bearer {your_jwt_token}" \
  -H "Accept: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" \
  --output courses.xlsx
```

## 权限测试场景

### 场景1：管理员导出所有课程
- 用户类型：平台管理员或企业管理员
- 预期结果：可以导出所有状态的课程

### 场景2：普通用户导出已发布课程
- 用户类型：普通用户
- 预期结果：可以导出所有已发布(status=1)的课程

### 场景3：普通用户导出自己的课程
- 用户类型：普通用户
- 预期结果：可以导出自己上传的所有课程（任何状态）

### 场景4：普通用户尝试导出他人未发布课程
- 用户类型：普通用户
- 预期结果：无法导出他人未发布的课程

## 常见问题

### Q: 在Postman中看到乱码怎么办？
A: 这是因为Postman默认以文本形式显示二进制文件。请使用 "Send and Download" 功能下载文件。

### Q: 导出的Excel文件打不开怎么办？
A: 确保：
1. 文件扩展名为 `.xlsx`
2. 使用Excel 2007或更高版本打开
3. 检查文件是否完整下载

### Q: 权限不足怎么办？
A: 检查：
1. 用户是否已登录（token是否有效）
2. 用户权限是否符合要求
3. 是否尝试访问无权限的数据

## 测试数据准备

### 创建测试课程
```sql
-- 创建不同状态的课程
INSERT INTO biz_course (title, description, status, author_id, company_id) VALUES
('已发布课程1', '测试课程1', 1, 1, 1),
('待审核课程1', '测试课程2', 0, 1, 1),
('审核未通过课程1', '测试课程3', 2, 1, 1),
('他人已发布课程', '测试课程4', 1, 2, 1),
('他人待审核课程', '测试课程5', 0, 2, 1);
```

### 创建测试用户
```sql
-- 普通用户
INSERT INTO sys_user (username, password, user_type, company_role) VALUES
('testuser', 'password', 1, 1);

-- 企业管理员
INSERT INTO sys_user (username, password, user_type, company_role, company_id) VALUES
('admin', 'password', 1, 2, 1);
```

## 问题描述
在Postman中测试Excel导出接口时，响应显示乱码。

## 解决方案

### 1. 后端修复（已完成）
已修复后端代码中的Content-Type设置：
```java
// 修复前
headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

// 修复后  
headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
```

### 2. Postman测试步骤

#### 步骤1：创建请求
- Method: `GET`
- URL: `http://localhost:8080/api/courses/export`
- Headers: 
  - `Authorization: Bearer YOUR_TOKEN`

#### 步骤2：使用正确的下载方式
**推荐方法：Send and Download**
1. 点击"Send"按钮旁边的下拉箭头
2. 选择"Send and Download"
3. 选择保存位置
4. 输入文件名：`课程列表.xlsx`

#### 步骤3：验证响应头
确保响应头包含：
```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment; filename="课程列表.xlsx"
```

### 3. 常见问题排查

#### 问题1：仍然显示乱码
**解决方案：**
- 不要使用普通的"Send"按钮
- 必须使用"Send and Download"
- 检查文件名后缀是否为.xlsx

#### 问题2：文件无法打开
**检查项目：**
- 响应状态码是否为200
- 文件大小是否正常（不应该为0）
- 响应体是否为空

#### 问题3：权限错误
**检查项目：**
- Token是否有效
- 用户是否有导出权限
- 请求头是否正确设置

### 4. 测试命令

#### 使用curl测试
```bash
# 获取Token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# 导出Excel
curl -X GET http://localhost:8080/api/courses/export \
  -H "Authorization: Bearer YOUR_TOKEN" \
  --output "课程列表.xlsx"
```

### 5. 验证结果

#### 成功标志：
- 状态码：200
- 文件大小：> 0
- 文件格式：.xlsx
- 可以正常打开Excel文件

#### 文件内容应该包含：
- 课程ID
- 课程名称
- 课程封面
- 课程简介
- 作者名称
- 企业名称
- 审核状态
- 状态描述
- 观看次数
- 创建时间

### 6. 调试技巧

#### 在Postman中添加测试脚本
```javascript
// 检查响应状态
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// 检查Content-Type
pm.test("Content-Type is correct", function () {
    pm.expect(pm.response.headers.get("Content-Type")).to.include("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
});

// 检查文件大小
pm.test("File size is not zero", function () {
    pm.expect(pm.response.size().body).to.be.greaterThan(0);
});
```

### 7. 其他测试工具

#### 使用浏览器测试
1. 在浏览器中访问：`http://localhost:8080/api/courses/export`
2. 添加Authorization Header（需要浏览器插件）
3. 文件会自动下载

#### 使用Postman Collection
导入之前提供的Postman集合，其中包含了正确的Excel导出测试用例。

---

**注意：** 如果问题仍然存在，请检查：
1. 应用是否正常启动
2. 数据库连接是否正常
3. 是否有测试数据
4. 用户权限是否正确设置 