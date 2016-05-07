package org.chudian.mj.mapper;

import org.chudian.mj.bean.Gruop;

public interface GruopMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Gruop record);

    int insertSelective(Gruop record);

    Gruop selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Gruop record);

    int updateByPrimaryKey(Gruop record);
}