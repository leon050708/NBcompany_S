package org.example.nbcompany.service.impl;

import org.example.nbcompany.dao.MobileMapper;
import org.example.nbcompany.dto.response.MobileBizCollaborationResponse;
import org.example.nbcompany.dto.response.PageResponse;
import org.example.nbcompany.entity.BizMeeting;
import org.example.nbcompany.entity.BizMeetingRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MobileService implements org.example.nbcompany.service.MobileService {

    @Autowired
    private MobileMapper mobileMapper;

    @Override
    public PageResponse<MobileBizCollaborationResponse> listCollaboration(Integer category, int page, int size) {

        PageResponse<MobileBizCollaborationResponse> response = new PageResponse<>();
        response.setCurrent(page);
        response.setRecords(mobileMapper.getCollaborations(category, (page - 1) * size, size));
        response.setTotal(mobileMapper.getTotalCount(category));
        response.setPages((int) Math.ceil((double) response.getTotal() / size));

        return  response;
    }

    @Override
    public BizMeeting getMeeting(Long meetingId) {
        BizMeeting meeting = mobileMapper.findByMeetingId(meetingId);
        return meeting;
    }

    @Override
    @Transactional
    public void registerForMeeting(Long meetingId, BizMeetingRegistration registration, Long userId) {
        // 1. 检查会议是否存在
        BizMeeting meeting = mobileMapper.findByMeetingId(meetingId);
        if (meeting == null) {
            throw new RuntimeException("会议不存在，ID: " + meetingId);
        }

        // 2. 检查用户是否已提交过回执
        BizMeetingRegistration existingRegistration = mobileMapper.findByMeetingIdAndUserId(meetingId, userId);
        if (existingRegistration != null) {
            throw new RuntimeException("您已提交过该会议的回执，请勿重复提交");
        }
        // 3. 【重要】安全性处理：覆盖或设置关键字段，防止客户端恶意提交
        //    即使客户端在JSON中传入了id, userId等字段，我们也要用服务器端的数据覆盖它们。
        registration.setId(null); // 强制为null，确保是新增操作
        registration.setMeetingId(meetingId); // 使用从URL中获取的ID，而不是请求体中的
        registration.setUserId(userId); // 使用从安全上下文中获取的当前用户ID
        registration.setRegisteredAt(LocalDateTime.now()); // 使用服务器当前时间
        mobileMapper.insert(registration);
    }

    @Override
    public MobileBizCollaborationResponse getCollaboration(Long collaborationId) {

        MobileBizCollaborationResponse collaboration = mobileMapper.findByCollaborationId(collaborationId);
        return collaboration;
    }

}
