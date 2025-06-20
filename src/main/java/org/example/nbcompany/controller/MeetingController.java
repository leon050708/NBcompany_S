package org.example.nbcompany.controller;

import org.example.nbcompany.dto.response.ApiResponse;
import org.example.nbcompany.dto.response.PageResponse;
import org.example.nbcompany.entity.BizMeeting;
import org.example.nbcompany.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/meetings")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping
    public ApiResponse<PageResponse<BizMeeting>>getMeetingList(
            @RequestParam(required = false) String meetingName,
            @RequestParam(required = false) String creatorName,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        PageResponse<BizMeeting>pageResponse = meetingService.listMeetings(meetingName,creatorName,companyId,startDate,endDate,status,page,size);
        return ApiResponse.success("获取成功",pageResponse);
    }
    @GetMapping("/{meetingId}")
    public ApiResponse<BizMeeting> getMeetingDetail(@PathVariable Long meetingId){
        BizMeeting bizMeeting = meetingService.getMeetingDetails(meetingId);
        return ApiResponse.success("获取成功",bizMeeting);
    }
    @PostMapping
    public ApiResponse<Map<String,Long>> createMeeting(
            @RequestBody BizMeeting bizMeeting,
            @RequestAttribute Long userId
    ){
        BizMeeting createdMeeting = meetingService.createMeeting(bizMeeting,userId);
        Map<String,Long> responseData = Map.of("meetingId",createdMeeting.getId());
        String message = (createdMeeting.getStatus()==1) ? "会议创建成功":"会议创建成功，请等待管理员审核";
        return ApiResponse.success(message,responseData);
    }
    @PutMapping("/{meetingId}")
    public ApiResponse<BizMeeting>updateMeeting(
            @PathVariable Long meetingId,
            @RequestBody BizMeeting meetingUpdateData,
            @RequestAttribute Long userId
    ){
        BizMeeting updatedMeeting = meetingService.updateMeeting(meetingId,meetingUpdateData,userId);
        return ApiResponse.success("会议信息修改成功",updatedMeeting);
    }
    @DeleteMapping("/{meetingId}")
    public ApiResponse<Void>deleteMeeting(
            @PathVariable Long meetingId,
            @RequestAttribute Long userId
    ){
        meetingService.deleteMeeting(meetingId,userId);
        return ApiResponse.success("会议删除成功",null);
    }
}
