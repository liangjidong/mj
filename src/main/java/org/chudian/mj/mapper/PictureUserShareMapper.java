package org.chudian.mj.mapper;

import org.chudian.mj.bean.PictureUserShare;

public interface PictureUserShareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureUserShare record);

    int insertSelective(PictureUserShare record);

    PictureUserShare selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureUserShare record);

    int updateByPrimaryKey(PictureUserShare record);
}