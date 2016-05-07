package org.chudian.mj.mapper;

import org.chudian.mj.bean.PictureVideo;

public interface PictureVideoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PictureVideo record);

    int insertSelective(PictureVideo record);

    PictureVideo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PictureVideo record);

    int updateByPrimaryKey(PictureVideo record);
}