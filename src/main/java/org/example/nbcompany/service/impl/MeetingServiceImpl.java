package org.example.nbcompany.service.impl;

import org.example.nbcompany.dao.BizMeetingMapper;
import org.example.nbcompany.dao.SysUserMapper;
import org.example.nbcompany.dto.response.PageResponse;
import org.example.nbcompany.entity.BizMeeting;
import org.example.nbcompany.entity.SysUser;
import org.example.nbcompany.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private BizMeetingMapper bizMeetingMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public PageResponse<BizMeeting>listMeetings(String meetingName, String creatorName, Long companyId,
                                                LocalDate startDate, LocalDate endDate, Integer status,
                                                int page, int size) {
        int offset = (page - 1) * size;
        List<BizMeeting> meetings = bizMeetingMapper.selectByConditions(
                meetingName, creatorName, companyId, startDate, endDate, status, offset, size);
        long totalRecords = bizMeetingMapper.countByConditions(
                meetingName, creatorName, companyId, startDate, endDate, status);
        PageResponse<BizMeeting> pageResponse = new PageResponse<>();
        pageResponse.setRecords(meetings); // 设置当前页的数据列表
        pageResponse.setTotal(totalRecords); // 设置总记录数
        pageResponse.setCurrent(page); // 设置当前页码
        pageResponse.setPages((int)Math.ceil((double)totalRecords / size));
        return pageResponse;
    }
    @Override
    public BizMeeting getMeetingDetails(Long meetingId) {
        BizMeeting meeting = bizMeetingMapper.selectById(meetingId);
        if(meeting==null){
            throw new RuntimeException("会议不存在，ID:"+meetingId);
        }
        return meeting;
    }
    @Override
    public BizMeeting createMeeting(BizMeeting meeting,Long creatorId) {
        SysUser creator = sysUserMapper.selectById(creatorId);
        if(creator==null){
            throw new RuntimeException("用户不存在 ID:"+creatorId);
        }
        meeting.setCreatorId(creator.getId());
        meeting.setCreatorName(creator.getNickname());
        meeting.setCompanyId(creator.getCompanyId());
        meeting.setCreatedAt(LocalDateTime.now());
        meeting.setUpdatedAt(LocalDateTime.now());
        if (creator.getUserType()==2){
            meeting.setStatus(1);
        }else  {
            meeting.setStatus(0);
        }
        bizMeetingMapper.insert(meeting);
        return meeting;
    }
    @Override
    public BizMeeting updateMeeting(Long meetingId, BizMeeting meetingUpdateData, Long currentUserId){
        BizMeeting existingMeeting = bizMeetingMapper.selectById(meetingId);
        if(existingMeeting==null){
            throw new RuntimeException("会议不存在ID:"+meetingId);
        }
        SysUser currentUser = sysUserMapper.selectById(currentUserId);
        if(currentUser==null){
            throw new RuntimeException("用户不存在 ID:"+currentUserId);
        }
        boolean hasPermission = false;
        if(currentUser.getUserType()==2){
            hasPermission = true;
        } else if (currentUser.getCompanyRole()==2) {
            if (existingMeeting.getCompanyId() !=null && currentUser.getCompanyId().equals(existingMeeting.getCompanyId())) {
                hasPermission = true;
            }
        }
        if(!hasPermission){
            throw new RuntimeException("权限不足，无法修改此会议");
        }
        existingMeeting.setMeetingName(meetingUpdateData.getMeetingName());
        existingMeeting.setStartTime(meetingUpdateData.getStartTime());
        existingMeeting.setEndTime(meetingUpdateData.getEndTime());
        existingMeeting.setCoverImageUrl(meetingUpdateData.getCoverImageUrl());
        existingMeeting.setContent(meetingUpdateData.getContent());
        existingMeeting.setLocation(meetingUpdateData.getLocation());
        existingMeeting.setOrganizer(meetingUpdateData.getOrganizer());
        existingMeeting.setAgenda(meetingUpdateData.getAgenda());
        existingMeeting.setSpeakers(meetingUpdateData.getSpeakers());
        existingMeeting.setUpdatedAt(LocalDateTime.now());
        bizMeetingMapper.update(existingMeeting);
        return existingMeeting;
    }
    @Override
    public void deleteMeeting(Long meetingId,long currentUserId) {
        BizMeeting existingmeeting = bizMeetingMapper.selectById(meetingId);
        if(existingmeeting==null){
            throw new RuntimeException("会议不存在ID:"+meetingId);
        }
        SysUser currentUser = sysUserMapper.selectById(currentUserId);
        if(currentUser==null){
            throw new RuntimeException("用户不存在 ID:"+currentUserId);
        }
        boolean hasPermission = false;
        if(currentUser.getUserType()==2){
            hasPermission = true;
        }else if (currentUser.getCompanyRole()==2) {
            if (existingmeeting.getCompanyId() !=null && currentUser.getCompanyId().equals(existingmeeting.getCompanyId())) {
                hasPermission = true;
            }
        }
        if(!hasPermission){
            throw new RuntimeException("权限不足，无法删除会议");
        }
        bizMeetingMapper.deleteById(meetingId);
    }
    @Override
    public void updateMeetingStatus(Long meetingId,Integer status,long currentUserId) {
        SysUser currentUser = sysUserMapper.selectById(currentUserId);
        if(currentUser == null || currentUser.getUserType()!=2){
            throw new RuntimeException("无法审核，权限不足");
        }
        BizMeeting meeting = bizMeetingMapper.selectById(meetingId);
        if(meeting==null){
            throw new RuntimeException("无法审核，会议不存在ID:"+meetingId);
        }
        if(status!=1&&status!=0){
            throw new RuntimeException("无效状态，只能设置1或者0");
        }
        bizMeetingMapper.updateStatus(meetingId,status);
    }

    }
