package org.example.nbcompany.controller;

import org.example.nbcompany.dto.response.ApiResponse;
import org.example.nbcompany.dto.response.PageResponse;
import org.example.nbcompany.entity.BizCollaboration;
import org.example.nbcompany.entity.BizMeeting;
import org.example.nbcompany.entity.BizMeetingRegistration;
import org.example.nbcompany.service.impl.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/mobile")
public class MobileController {
    @Autowired
    private MobileService mobileService;


    @GetMapping("/collaborations")
    public ApiResponse<PageResponse<BizCollaboration>> getCollaborations(@RequestParam(value = "category", required = false) Integer category,
                                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<BizCollaboration> response=mobileService.listCollaboration(category,page,size);
        // 构造响应体
        return ApiResponse.success("获取成功", response);
    }


    @PostMapping("/collaborations/meeting/{meetingId}")
    public ApiResponse<BizMeeting> getMeeting(@PathVariable Long meetingId) {

        BizMeeting meeting = mobileService.getMeeting(meetingId);

        if (meeting == null) {
            // 如果 service 返回 null，说明会议不存在，返回 404 Not Found
            return ApiResponse.error(404, "会议不存在");
        }
        // 成功找到会议，返回 200 OK 和会议数据
        return ApiResponse.success("获取成功", meeting);
    }

    @PostMapping("meetings/{meetingId}/register")
    public ApiResponse<Void> registerForMeeting(@PathVariable Long meetingId ,@RequestBody BizMeetingRegistration registration) {

        try {
            // 调用更新后的 service 方法
            mobileService.registerForMeeting(meetingId, registration,  registration.getUserId());
            return ApiResponse.success("会议回执提交成功", null);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("会议不存在")) {
                return ApiResponse.error(404, e.getMessage());
            }
            if (e.getMessage().contains("重复提交")) {
                return ApiResponse.error(409, e.getMessage());
            }
            return ApiResponse.error(500, "服务器内部错误");
        }
    }

    @GetMapping("/collaborations/{collaborationId}")
    public ApiResponse<BizCollaboration> getCollaboration(@PathVariable Long collaborationId) {

        BizCollaboration collaborationDetails = mobileService.getCollaboration(collaborationId);
        if (collaborationDetails == null) {
            return ApiResponse.error(404, "合作模块不存在");
        }
        // 成功找到，返回成功响应和数据
        return ApiResponse.success("获取成功", collaborationDetails);
    }


}

