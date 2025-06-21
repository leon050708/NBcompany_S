package org.example.nbcompany.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.nbcompany.dto.NewsDto.NewsQueryDto;
import org.example.nbcompany.entity.BizNews;
import java.util.List;

@Mapper
public interface NewsMapper {
    int insert(BizNews news);
    int update(BizNews news);
    int deleteById(Long id);
    BizNews findById(Long id);
    List<BizNews> findList(NewsQueryDto query);
}