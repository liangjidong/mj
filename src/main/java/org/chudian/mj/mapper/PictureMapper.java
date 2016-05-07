package org.chudian.mj.mapper;

import org.chudian.mj.bean.Picture;
import org.chudian.mj.common.QueryBase;

import java.util.List;

public interface PictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Picture record);

    int insertSelective(Picture record);

    Picture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);

    List<Picture> queryPictures(QueryBase queryBase);

    long countPictures(QueryBase queryBase);

    Picture selectByName(String name);
}