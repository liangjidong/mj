package org.chudian.mj.mapper;

import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;

import java.util.List;

public interface VideoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Video record);

    int insertSelective(Video record);

    Video selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKey(Video record);

    List<Video> queryVideos(QueryBase queryBase);

    List<Video> queryVideosAll(QueryBase queryBase);
    
    long countVideos (QueryBase queryBase);
}