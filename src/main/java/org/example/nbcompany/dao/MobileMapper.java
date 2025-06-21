package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nbcompany.dto.response.MobileBizCollaborationResponse;
import org.example.nbcompany.entity.BizCollaboration;
import org.example.nbcompany.entity.BizMeeting;
import org.example.nbcompany.entity.BizMeetingRegistration;

import java.util.List;

@Mapper
public interface MobileMapper {

    List<MobileBizCollaborationResponse> getCollaborations(Integer category, int offset, int size);

    int getTotalCount(@Param("category") Integer category);

    BizMeeting findByMeetingId(@Param("meetingId") Long meetingId);


    //插入一条会议注册回执
    int insert(BizMeetingRegistration registration);

    //根据会议ID和用户ID查询回执，用于检查是否重复提交
    BizMeetingRegistration findByMeetingIdAndUserId(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

    MobileBizCollaborationResponse findByCollaborationId(@Param("collaborationId") Long collaborationId);
}
