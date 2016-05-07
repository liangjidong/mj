package org.chudian.mj.mapper;

import org.chudian.mj.bean.Audio;

public interface AudioMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Audio record);

    int insertSelective(Audio record);

    Audio selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Audio record);

    int updateByPrimaryKey(Audio record);
}