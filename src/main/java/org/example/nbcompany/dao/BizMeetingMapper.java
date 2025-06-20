package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.nbcompany.entity.BizMeeting;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BizMeetingMapper {
    List<BizMeeting> selectByConditions(@Param("meetingName") String meetingName,
                                        @Param("creatorName") String creatorName,
                                        @Param("companyId") Long companyId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("status") Integer status,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);
    long countByConditions(
            @Param("meetingName") String meetingName,
            @Param("creatorName") String creatorName,
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") Integer status);
    BizMeeting selectById(@Param("id") Long id);
    int insert(BizMeeting meeting);
    int update(BizMeeting meeting);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
