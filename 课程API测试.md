# 课程管理API接口测试文档

本文档提供了课程管理系统所有API接口的测试用例。

**注意:**
- `{{BASE_URL}}` 请替换为您的本地服务地址，例如 `http://localhost:8080`
- `{{ADMIN_TOKEN}}` 请替换为平台管理员登录后获取的JWT Token
- `{{USER_TOKEN}}` 请替换为普通用户登录后获取的JWT Token
- `{{COURSE_ID}}` 请替换为实际的课程ID

---

## 1. 课程创建接口

### 1.1 创建课程
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Authorization: Bearer {{USER_TOKEN}}" \
  -H "Content-Type: application/json" \
  -d '{
    "courseName": "Java基础教程",
    "coverImageUrl": "https://example.com/cover.jpg",
    "summary": "Java编程语言基础入门课程，适合初学者学习",
    "courseVideoUrl": "https://example.com/video.mp4",
    "sortOrder": 1
  }'
```

**请求数据说明:**
- `courseName`: 课程名称（必填）
- `coverImageUrl`: 课程封面图片URL
- `summary`: 课程简介
- `courseVideoUrl`: 课程视频URL
- `sortOrder`: 排序值

**响应示例:**
```json
{
  "code": 200,
  "message": "课程创建成功",
  "data": {
    "id": 1,
    "courseName": "Java基础教程",
    "coverImageUrl": "https://example.com/cover.jpg",
    "summary": "Java编程语言基础入门课程，适合初学者学习",
    "courseVideoUrl": "https://example.com/video.mp4",
    "sortOrder": 1,
    "authorId": 123,
    "authorName": "张三",
    "companyId": 456,
    "companyName": "腾讯科技",
    "status": 0,
    "statusDesc": "待审核",
    "viewCount": 0,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

---

## 2. 课程查询接口

### 2.1 获取课程详情
```bash
curl -X GET http://localhost:8080/api/courses/1 \
  -H "Authorization: Bearer {{USER_TOKEN}}"
```

**响应示例:**
```json
{
  "code": 200,
  "message": "获取课程详情成功",
  "data": {
    "id": 1,
    "courseName": "Java基础教程",
    "coverImageUrl": "https://example.com/cover.jpg",
    "summary": "Java编程语言基础入门课程，适合初学者学习",
    "courseVideoUrl": "https://example.com/video.mp4",
    "sortOrder": 1,
    "authorId": 123,
    "authorName": "张三",
    "companyId": 456,
    "companyName": "腾讯科技",
    "status": 1,
    "statusDesc": "已发布",
    "viewCount": 150,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

### 2.2 获取课程列表（分页）
```bash
curl -X GET 'http://localhost:8080/api/courses?pageNum=1&pageSize=10' \
  -H "Authorization: Bearer {{USER_TOKEN}}"
```

**查询参数说明:**
- `pageNum`: 页码，默认1
- `pageSize`: 每页大小，默认10
- `courseName`: 课程名称（模糊查询）
- `status`: 课程状态（0:待审核, 1:已发布, 2:审核未通过）
- `authorName`: 作者名称（模糊查询）
- `companyName`: 企业名称（模糊查询）

**响应示例:**
```json
{
  "code": 200,
  "message": "获取课程列表成功",
  "data": {
    "records": [
      {
        "id": 1,
        "courseName": "Java基础教程",
        "coverImageUrl": "https://example.com/cover.jpg",
        "summary": "Java编程语言基础入门课程，适合初学者学习...",
        "authorName": "张三",
        "companyName": "腾讯科技",
        "status": 1,
        "statusDesc": "已发布",
        "viewCount": 150,
        "createdAt": "2024-01-15T10:30:00"
      }
    ],
    "current": 1,
    "pages": 5,
    "total": 100
  }
}
```

### 2.3 按条件筛选课程
```bash
# 按课程名称搜索
curl -X GET 'http://localhost:8080/api/courses?courseName=Java&pageNum=1&pageSize=10' \
  -H "Authorization: Bearer {{USER_TOKEN}}"

# 按状态筛选
curl -X GET 'http://localhost:8080/api/courses?status=1&pageNum=1&pageSize=10' \
  -H "Authorization: Bearer {{USER_TOKEN}}"

# 按作者搜索
curl -X GET 'http://localhost:8080/api/courses?authorName=张三&pageNum=1&pageSize=10' \
  -H "Authorization: Bearer {{USER_TOKEN}}"

# 按企业搜索
curl -X GET 'http://localhost:8080/api/courses?companyName=腾讯&pageNum=1&pageSize=10' \
  -H "Authorization: Bearer {{USER_TOKEN}}"
```

---

## 3. 课程更新接口

### 3.1 更新课程信息
```bash
curl -X PUT http://localhost:8080/api/courses/1 \
  -H "Authorization: Bearer {{USER_TOKEN}}" \
  -H "Content-Type: application/json" \
  -d '{
    "courseName": "Java进阶教程",
    "coverImageUrl": "https://example.com/new-cover.jpg",
    "summary": "Java编程语言进阶课程，包含高级特性",
    "courseVideoUrl": "https://example.com/advanced-video.mp4",
    "sortOrder": 2,
    "authorName": "张三",
    "status": 1
  }'
```

**请求数据说明:**
- 所有字段都是可选的，只更新提供的字段
- `courseName`: 课程名称
- `coverImageUrl`: 课程封面图片URL
- `summary`: 课程简介
- `courseVideoUrl`: 课程视频URL
- `sortOrder`: 排序值
- `authorName`: 作者名称
- `status`: 课程状态

**响应示例:**
```json
{
  "code": 200,
  "message": "课程更新成功",
  "data": {
    "id": 1,
    "courseName": "Java进阶教程",
    "coverImageUrl": "https://example.com/new-cover.jpg",
    "summary": "Java编程语言进阶课程，包含高级特性",
    "courseVideoUrl": "https://example.com/advanced-video.mp4",
    "sortOrder": 2,
    "authorId": 123,
    "authorName": "张三",
    "companyId": 456,
    "companyName": "腾讯科技",
    "status": 1,
    "statusDesc": "已发布",
    "viewCount": 150,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:30:00"
  }
}
```

---

## 4. 课程删除接口

### 4.1 删除课程
```bash
curl -X DELETE http://localhost:8080/api/courses/1 \
  -H "Authorization: Bearer {{USER_TOKEN}}"
```

**响应示例:**
```json
{
  "code": 200,
  "message": "课程删除成功",
  "data": null
}
```

---

## 5. 课程审核接口

### 5.1 审核通过课程（仅平台管理员）
```bash
curl -X POST http://localhost:8080/api/courses/audit \
  -H "Authorization: Bearer {{ADMIN_TOKEN}}" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "status": 1,
    "auditRemark": "课程内容符合要求，审核通过"
  }'
```

### 5.2 审核拒绝课程
```bash
curl -X POST http://localhost:8080/api/courses/audit \
  -H "Authorization: Bearer {{ADMIN_TOKEN}}" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "status": 2,
    "auditRemark": "课程内容不符合要求，需要修改"
  }'
```

**请求数据说明:**
- `id`: 课程ID（必填）
- `status`: 审核状态（1:通过, 2:拒绝）
- `auditRemark`: 审核备注

**响应示例:**
```json
{
  "code": 200,
  "message": "课程审核成功",
  "data": {
    "id": 1,
    "courseName": "Java基础教程",
    "status": 1,
    "statusDesc": "已发布",
    "updatedAt": "2024-01-15T12:00:00"
  }
}
```

---

## 6. Excel导出接口

### 6.1 导出课程列表
```bash
curl -X GET http://localhost:8080/api/courses/export \
  -H "Authorization: Bearer {{USER_TOKEN}}" \
  --output "课程列表.xlsx"
```

### 6.2 按条件筛选导出
```bash
# 导出已发布课程
curl -X GET 'http://localhost:8080/api/courses/export?status=1' \
  -H "Authorization: Bearer {{USER_TOKEN}}" \
  --output "已发布课程.xlsx"

# 导出特定企业的课程
curl -X GET 'http://localhost:8080/api/courses/export?companyName=腾讯' \
  -H "Authorization: Bearer {{USER_TOKEN}}" \
  --output "腾讯课程.xlsx"

# 导出特定作者的课程
curl -X GET 'http://localhost:8080/api/courses/export?authorName=张三' \
  -H "Authorization: Bearer {{USER_TOKEN}}" \
  --output "张三的课程.xlsx"

# 多条件组合导出
curl -X GET 'http://localhost:8080/api/courses/export?status=1&companyName=腾讯&courseName=Java' \
  -H "Authorization: Bearer {{USER_TOKEN}}" \
  --output "腾讯Java课程.xlsx"
```

**权限说明:**
- **管理员用户**: 可以导出所有课程（任何状态）
- **普通用户**: 可以导出已发布的课程和自己上传的课程（不论审核状态）

### 6.3 Postman中使用Excel导出（解决乱码问题）

**方法一：使用Postman的Send and Download功能**
1. 在Postman中创建GET请求：`http://localhost:8080/api/courses/export`
2. 添加Authorization Header：`Bearer {{USER_TOKEN}}`
3. 点击"Send"按钮旁边的下拉箭头
4. 选择"Send and Download"
5. 选择保存位置和文件名（如：课程列表.xlsx）

**方法二：设置正确的响应处理**
1. 在Postman中创建GET请求：`http://localhost:8080/api/courses/export`
2. 添加Authorization Header：`Bearer {{USER_TOKEN}}`
3. 在"Tests"标签页中添加以下脚本：
```javascript
// 检查响应状态
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// 检查Content-Type
pm.test("Content-Type is correct", function () {
    pm.expect(pm.response.headers.get("Content-Type")).to.include("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
});

// 检查Content-Disposition
pm.test("Content-Disposition is present", function () {
    pm.expect(pm.response.headers.get("Content-Disposition")).to.include("attachment");
});

// 检查响应体不为空
pm.test("Response body is not empty", function () {
    pm.expect(pm.response.body).to.not.be.empty;
});
```

**方法三：使用Postman的Pre-request Script**
在"Pre-request Script"标签页中添加：
```javascript
// 设置请求头
pm.request.headers.add({
    key: 'Accept',
    value: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
});
```

**方法四：直接下载文件**
1. 在Postman中发送请求后
2. 在响应区域点击"Save Response"
3. 选择"Save to a file"
4. 输入文件名：`课程列表.xlsx`
5. 点击保存

**常见问题解决：**

1. **如果显示乱码**：
   - 确保响应头中的Content-Type正确
   - 使用"Send and Download"而不是普通的"Send"
   - 检查文件名后缀是否为.xlsx

2. **如果文件无法打开**：
   - 检查响应状态码是否为200
   - 确认响应体不为空
   - 验证文件大小是否正常

3. **如果权限错误**：
   - 检查Authorization Header是否正确
   - 确认Token是否有效
   - 验证用户是否有导出权限

**响应头说明：**
```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment; filename="课程列表.xlsx"
Content-Length: [文件大小]
```

**导出文件说明:**
- 文件名: 课程列表.xlsx
- 包含列: 课程ID、课程名称、课程封面、课程简介、作者名称、企业名称、审核状态、状态描述、观看次数、创建时间

---

## 权限说明

### 用户角色
- **普通用户**: 只能查看和导出已发布的课程
- **企业管理员**: 可以管理本企业的所有课程
- **平台管理员**: 可以管理所有课程，包括审核功能

### 课程状态
- **0**: 待审核
- **1**: 已发布
- **2**: 审核未通过

### 权限矩阵

| 操作 | 普通用户 | 企业管理员 | 平台管理员 |
|------|----------|------------|------------|
| 查看已发布课程 | ✅ | ✅ | ✅ |
| 查看待审核课程 | ❌ | ✅ (本企业) | ✅ |
| 查看审核未通过课程 | ❌ | ✅ (本企业) | ✅ |
| 创建课程 | ✅ | ✅ | ✅ |
| 更新课程 | ❌ | ✅ (本企业) | ✅ |
| 删除课程 | ❌ | ✅ (本企业) | ✅ |
| 审核课程 | ❌ | ❌ | ✅ |
| 导出已发布课程 | ✅ | ✅ | ✅ |
| 导出自己的课程 | ✅ (任何状态) | ✅ | ✅ |
| 导出所有课程 | ❌ | ✅ (本企业) | ✅ |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误或业务逻辑错误 |
| 401 | 用户未登录 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

**常见错误响应:**
```json
{
  "code": 400,
  "message": "课程名称不能为空",
  "data": null
}
```

```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "无权限访问此课程",
  "data": null
}
```

---

## 测试数据准备

### 1. 插入测试用户
```sql
-- 平台管理员
INSERT INTO sys_user (id, username, password, nickname, user_type, company_id, company_role) 
VALUES (1, 'admin', 'password', '管理员', 2, NULL, NULL);

-- 普通用户
INSERT INTO sys_user (id, username, password, nickname, user_type, company_id, company_role) 
VALUES (2, 'user1', 'password', '张三', 1, 1, 1);

-- 企业管理员
INSERT INTO sys_user (id, username, password, nickname, user_type, company_id, company_role) 
VALUES (3, 'company_admin', 'password', '企业管理员', 1, 1, 2);
```

### 2. 插入测试企业
```sql
INSERT INTO sys_company (id, company_name, status) 
VALUES (1, '腾讯科技', 1);

INSERT INTO sys_company (id, company_name, status) 
VALUES (2, '阿里巴巴', 1);
```

### 3. 插入测试课程
```sql
INSERT INTO biz_course (id, course_name, cover_image_url, summary, course_video_url, sort_order, author_id, author_name, company_id, status, view_count, created_at, updated_at) 
VALUES (1, 'Java基础教程', 'https://example.com/java.jpg', 'Java编程语言基础入门课程', 'https://example.com/java.mp4', 1, 2, '张三', 1, 0, 0, NOW(), NOW());

INSERT INTO biz_course (id, course_name, cover_image_url, summary, course_video_url, sort_order, author_id, author_name, company_id, status, view_count, created_at, updated_at) 
VALUES (2, 'Python数据分析', 'https://example.com/python.jpg', 'Python数据分析实战课程', 'https://example.com/python.mp4', 2, 2, '张三', 1, 1, 150, NOW(), NOW());
```

### 4. 获取JWT Token
```bash
# 平台管理员登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# 普通用户登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"password"}'

# 企业管理员登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"company_admin","password":"password"}'
```

**登录响应示例:**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "nickname": "管理员",
      "userType": 2
    }
  }
}
``` 