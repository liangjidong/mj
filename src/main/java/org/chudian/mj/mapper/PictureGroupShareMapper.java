package org.chudian.mj.mapper;

import org.chudian.mj.bean.PictureGroupShare;

public interface PictureGroupShareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureGroupShare record);

    int insertSelective(PictureGroupShare record);

    PictureGroupShare selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureGroupShare record);

    int updateByPrimaryKey(PictureGroupShare record);
}