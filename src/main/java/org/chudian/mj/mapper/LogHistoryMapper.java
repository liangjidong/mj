package org.chudian.mj.mapper;

import java.util.List;

import org.chudian.mj.bean.LogHistory;
import org.chudian.mj.bean.Video;
import org.chudian.mj.common.QueryBase;

public interface LogHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogHistory record);

    int insertSelective(LogHistory record);

    LogHistory selectByPrimaryKey(Integer id);
    
    List<LogHistory> selectByUserId(QueryBase queryBase);

    int updateByPrimaryKeySelective(LogHistory record);

    int updateByPrimaryKey(LogHistory record);

    List<LogHistory> queryByUserId(QueryBase queryBase);
    
    long countLogHistory (QueryBase queryBase);
    
}