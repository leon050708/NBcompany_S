package org.example.nbcompany.service;

import org.apache.ibatis.annotations.Param;
import org.example.nbcompany.dto.response.PageResponse;
import org.example.nbcompany.entity.BizMeeting;

import java.time.LocalDate;

public interface MeetingService {
    /*@param meetingName 可选的会议名称筛选
     * @param creatorName 可选的创建人名称筛选
     * @param companyId   可选的公司ID筛选
     * @param startDate   可选的开始日期筛选
     * @param endDate     可选的结束日期筛选
     * @param status      可选的状态筛选
     * @param page        请求的页码（从1开始）
     * @param size        每页的项目数量
     * @return 包含会议列表和分页信息的 PageResponse 对象
     */
    PageResponse<BizMeeting>listMeetings(String meetingName, String creatorName, Long companyId, LocalDate startDate, LocalDate endDate, Integer status,int page, int size);

    BizMeeting getMeetingDetails(Long meetingId);
    BizMeeting createMeeting(BizMeeting meeting,Long creatorId);
    BizMeeting updateMeeting(Long meetingId, BizMeeting meetingUpdateData, Long currentUserId);
    void deleteMeeting(Long meetingId,long currentUserId);
    void updateMeetingStatus(Long meetingId,Integer status,long currentUserId);
}
