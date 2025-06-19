package org.example.nbcompany.service;

import org.example.nbcompany.dto.response.PageResponse;
import org.example.nbcompany.entity.BizCollaboration;
import org.example.nbcompany.entity.BizMeeting;
import org.example.nbcompany.entity.BizMeetingRegistration;


public interface MobileService {

    PageResponse<BizCollaboration> listCollaboration(Integer category, int page, int size);

    BizMeeting getMeeting(Long meetingId);

    void registerForMeeting(Long meetingId, BizMeetingRegistration registration, Long userId);

    BizCollaboration getCollaboration(Long collaborationId);
}
