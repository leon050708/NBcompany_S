会议管理接口测试文档 (/api/v1/meetings)
本文档提供了会议管理子系统所有API接口的详细使用说明和测试用例。

通用设置 (Postman):

环境变量:
BASE_URL: http://localhost:8080
ADMIN_TOKEN: 使用 admin 登录后获取的Token
COMPANY_ADMIN_TOKEN: 使用 zhangsan (企业管理员) 登录后获取的Token
通用请求头 (需要认证的接口):
Authorization: Bearer {{TOKEN}} (这里的 {{TOKEN}} 替换为对应的环境变量)
Content-Type: application/json
1. 获取会议列表

接口: GET {{BASE_URL}}/api/v1/meetings
描述: 公开接口，用于分页和筛选获取所有已发布的会议列表。
权限: 无需认证
请求参数 (Query Parameters)

所有参数均为可选：

meetingName (string): 会议名称，支持模糊查询。
creatorName (string): 创建人姓名，支持模糊查询。
companyId (long): 按企业ID精确筛选 (此参数主要供管理员使用)。
startDate (date): 按会议开始日期筛选，格式 YYYY-MM-DD。
endDate (date): 按会议结束日期筛选，格式 YYYY-MM-DD。
status (integer): 按会议状态筛选 (例如 1 代表已发布)。
page (integer): 页码，默认 1。
size (integer): 每页数量，默认 10。
响应体 (成功)

JSON
{
"code": 200,
"message": "获取成功",
"data": {
"total": 2,
"pages": 1,
"current": 1,
"records": [
{
"id": 1,
"meetingName": "2025全球人工智能开发者大会",
"startTime": "2025-09-01T09:00:00",
"endTime": "2025-09-03T17:00:00",
"location": "北京国家会议中心",
"creatorName": "张三",
"status": 1
}
]
}
}
测试说明

基础测试: 直接向 GET {{BASE_URL}}/api/v1/meetings 发送请求，无需任何Token或参数，验证是否能返回所有已发布会议的分页数据。
筛选测试: 在Postman的 Params 标签页添加 meetingName 参数，值为 大会，验证返回结果是否只包含名称中带“大会”的会议。
2. 获取会议详情

接口: GET {{BASE_VRL}}/api/v1/meetings/{meetingId}
描述: 公开接口，获取单个会议的详细信息。
权限: 无需认证
路径参数 (Path Parameters)

meetingId (long): 要查询的会议的唯一ID。
响应体 (成功)

JSON
{
"code": 200,
"message": "获取成功",
"data": {
"id": 1,
"meetingName": "2025全球人工智能开发者大会",
"startTime": "2025-09-01T09:00:00",
"endTime": "2025-09-03T17:00:00",
"coverImageUrl": null,
"content": null,
"location": "北京国家会议中心",
"organizer": "数智未来科技有限公司",
"agenda": null,
"speakers": null,
"creatorId": 2,
"creatorName": "张三",
"companyId": 1,
"status": 1,
"createdAt": "...",
"updatedAt": "..."
}
}
测试说明

成功用例: 向 GET {{BASE_URL}}/api/v1/meetings/1 发送请求，验证是否能返回ID为1的会议的完整信息。
失败用例: 向 GET {{BASE_URL}}/api/v1/meetings/999 (一个不存在的ID) 发送请求，验证是否会返回错误（例如500错误，消息为“会议不存在”）。
3. 创建新会议

接口: POST {{BASE_URL}}/api/v1/meetings
描述: 创建一个新的会议。
权限: 需要认证 (平台超级管理员或企业管理员)
请求体 (Body)

JSON
{
"meetingName": "季度技术分享会",
"startTime": "2025-07-15T14:00:00",
"endTime": "2025-07-15T16:00:00",
"location": "公司A座一楼会议室",
"organizer": "技术部",
"content": "<p>本次分享会包含前端和后端两个主题。</p>"
}
响应体 (成功)

JSON
{
"code": 200,
"message": "会议创建成功，待审核",
"data": {
"meetingId": 3
}
}
测试说明

使用 zhangsan (企业管理员) 登录，获取 COMPANY_ADMIN_TOKEN。
向 POST {{BASE_URL}}/api/v1/meetings 发送请求。
在 Authorization 标签页设置 Bearer Token 为 {{COMPANY_ADMIN_TOKEN}}。
在 Body 标签页设置请求体为上面的JSON。
验证返回的 message 是否为“会议创建成功，待审核”，并记下返回的 meetingId。
(可选) 使用 admin 登录获取 ADMIN_TOKEN 并重复上述步骤，验证返回的 message 是否为“会议创建成功”。
4. 编辑会议信息

接口: PUT {{BASE_URL}}/api/v1/meetings/{meetingId}
描述: 更新一个已存在会议的信息。
权限: 需要认证 (平台超级管理员或会议创建者所属企业的企业管理员)
路径参数 (Path Parameters)

meetingId (long): 要编辑的会议的ID。
请求体 (Body)

JSON
{
"meetingName": "季度技术分享会 - [已更新标题]",
"startTime": "2025-07-15T14:00:00",
"endTime": "2025-07-15T16:00:00",
"location": "公司B座三楼报告厅",
"organizer": "技术部 & 产品部",
"content": "<p>本次分享会包含前端和后端两个主题，并新增了产品演示环节。</p>"
}
响应体 (成功)

JSON
{
"code": 200,
"message": "会议信息修改成功",
"data": {
"id": 1,
"meetingName": "季度技术分享会 - [已更新标题]",
"location": "公司B座三楼报告厅",
// ... 其他更新后的字段
}
}
测试说明

使用 zhangsan (企业管理员) 登录，获取 COMPANY_ADMIN_TOKEN。
向 PUT {{BASE_URL}}/api/v1/meetings/1 (假设要修改ID为1的会议，该会议由 zhangsan 的公司创建) 发送请求。
设置好 Authorization 和 Body。
验证返回的数据是否已更新。
权限测试: 使用另一个公司的用户（如 lisi）的Token尝试修改ID为1的会议，验证是否会返回“权限不足”的错误。
5. 删除会议

接口: DELETE {{BASE_URL}}/api/v1/meetings/{meetingId}
描述: 删除一个会议。
权限: 需要认证 (平台超级管理员或会议创建者所属企业的企业管理员)
路径参数 (Path Parameters)

meetingId (long): 要删除的会议的ID。
响应体 (成功)

JSON
{
"code": 200,
"message": "会议删除成功",
"data": null
}
测试说明

为了安全，建议先创建一个用于删除的会议（例如，用 zhangsan 的Token创建一个新会议，得到ID为4）。
使用 zhangsan 的 COMPANY_ADMIN_TOKEN，向 DELETE {{BASE_URL}}/api/v1/meetings/4 发送请求。
验证是否返回成功的消息。
验证删除结果: 再次向 GET {{BASE_URL}}/api/v1/meetings/4 发送请求，验证是否返回“会议不存在”的错误。
6. 审核会议 (管理员)

接口: PUT {{BASE_URL}}/api/v1/admin/meetings/{meetingId}/status
描述: 平台超级管理员审核会议状态。
权限: 需要平台超级管理员认证
路径参数 (Path Parameters)

meetingId (long): 要审核的会议的ID。
请求体 (Body)

JSON
{
"status": 1
}
status (integer): 目标状态。1 表示审核通过，2 表示审核不通过 。
响应体 (成功)

JSON
{
"code": 200,
"message": "会议审核成功",
"data": null
}
测试说明

先用企业管理员 (zhangsan) 的Token创建一个新会议（假设ID为5），此时该会议状态为 0 (待审核)。
使用平台超级管理员 (admin) 登录，获取 ADMIN_TOKEN。
向 PUT {{BASE_URL}}/api/v1/admin/meetings/5/status 发送请求。
设置好 Authorization (使用ADMIN_TOKEN) 和 Body。
验证是否返回成功。
验证审核结果: 再次向 GET {{BASE_URL}}/api/v1/meetings/5 发送请求，验证返回数据中的 status 字段是否已变为 1。