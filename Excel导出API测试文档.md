# 课程列表Excel导出API测试文档

## API接口信息

- **接口地址**: `GET /api/courses/export`
- **功能描述**: 导出课程列表到Excel文件
- **权限要求**: 需要用户登录
- **返回格式**: Excel文件流

## 请求参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| courseName | String | 否 | 课程名称（模糊查询） |
| status | Integer | 否 | 课程状态（0:待审核, 1:已发布, 2:审核未通过） |
| authorName | String | 否 | 作者名称（模糊查询） |
| companyName | String | 否 | 企业名称（模糊查询） |

## 请求头

```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

## 响应头

```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment; filename="课程列表.xlsx"
Content-Length: {文件大小}
```

## 权限控制

- **普通用户**: 只能导出已发布的课程（status=1）
- **企业管理员**: 可以导出本企业的所有课程
- **平台管理员**: 可以导出所有课程

## 测试用例

### 1. 导出所有已发布课程（普通用户）

**请求**:
```bash
curl -X GET "http://localhost:8080/api/courses/export" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  --output "课程列表.xlsx"
```

**预期结果**: 下载Excel文件，包含所有已发布的课程

### 2. 按课程名称筛选导出

**请求**:
```bash
curl -X GET "http://localhost:8080/api/courses/export?courseName=Java" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  --output "Java课程列表.xlsx"
```

**预期结果**: 下载Excel文件，只包含课程名称包含"Java"的课程

### 3. 按状态筛选导出（管理员）

**请求**:
```bash
curl -X GET "http://localhost:8080/api/courses/export?status=0" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  --output "待审核课程列表.xlsx"
```

**预期结果**: 下载Excel文件，只包含待审核的课程

### 4. 按作者筛选导出

**请求**:
```bash
curl -X GET "http://localhost:8080/api/courses/export?authorName=张三" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  --output "张三的课程列表.xlsx"
```

**预期结果**: 下载Excel文件，只包含作者为"张三"的课程

### 5. 按企业筛选导出

**请求**:
```bash
curl -X GET "http://localhost:8080/api/courses/export?companyName=腾讯" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  --output "腾讯课程列表.xlsx"
```

**预期结果**: 下载Excel文件，只包含企业名称包含"腾讯"的课程

### 6. 多条件组合筛选

**请求**:
```bash
curl -X GET "http://localhost:8080/api/courses/export?status=1&companyName=阿里&courseName=技术" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  --output "阿里技术课程列表.xlsx"
```

**预期结果**: 下载Excel文件，包含阿里企业已发布且课程名称包含"技术"的课程

## 错误响应

### 1. 未登录（401）

**请求**:
```bash
curl -X GET "http://localhost:8080/api/courses/export"
```

**响应**:
```
HTTP/1.1 401 Unauthorized
```

### 2. 无权限导出特定状态（403）

**请求**（普通用户尝试导出待审核课程）:
```bash
curl -X GET "http://localhost:8080/api/courses/export?status=0" \
  -H "Authorization: Bearer USER_JWT_TOKEN"
```

**响应**:
```
HTTP/1.1 403 Forbidden
```

### 3. 服务器错误（500）

**响应**:
```
HTTP/1.1 500 Internal Server Error
```

## Excel文件格式

导出的Excel文件包含以下列：

| 列名 | 说明 |
|------|------|
| 课程ID | 课程的唯一标识 |
| 课程名称 | 课程的名称 |
| 课程封面 | 课程封面图片URL |
| 课程简介 | 课程的简要描述 |
| 作者名称 | 课程作者姓名 |
| 企业名称 | 发布企业名称 |
| 审核状态 | 数字状态码 |
| 状态描述 | 人类可读的状态描述 |
| 观看次数 | 课程被观看的次数 |
| 创建时间 | 课程创建时间 |

## 前端集成示例

```javascript
// 导出Excel函数
async function exportToExcel(params = {}) {
    try {
        // 构建查询参数
        const queryString = new URLSearchParams(params).toString();
        const url = `/api/courses/export?${queryString}`;
        
        // 发送请求
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${getToken()}`,
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error(`导出失败: ${response.status}`);
        }
        
        // 下载文件
        const blob = await response.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = '课程列表.xlsx';
        link.click();
        window.URL.revokeObjectURL(downloadUrl);
        
        console.log('导出成功');
    } catch (error) {
        console.error('导出失败:', error);
    }
}

// 使用示例
exportToExcel({
    status: 1,
    companyName: '腾讯'
});
```

## 注意事项

1. **文件大小限制**: 大量数据导出时注意内存使用
2. **权限验证**: 确保用户有相应权限
3. **错误处理**: 前端需要妥善处理各种错误情况
4. **用户体验**: 导出大文件时建议显示进度提示
5. **安全性**: 验证用户权限，防止未授权访问 