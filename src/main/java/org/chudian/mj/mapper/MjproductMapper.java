package org.chudian.mj.mapper;

import org.chudian.mj.bean.Mjproduct;
import org.chudian.mj.common.QueryBase;

import java.util.List;

public interface MjproductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Mjproduct record);

    int insertSelective(Mjproduct record);

    Mjproduct selectByPrimaryKey(Integer id);
    
    Mjproduct selectByPictureid(Integer mjproductMappert);

    int updateByPrimaryKeySelective(Mjproduct record);

    int updateByPrimaryKey(Mjproduct record);

    List<Mjproduct> queryProducts(QueryBase queryBase);
    
    List<Mjproduct> queryProductsByUserid(QueryBase queryBase);
    
    long countProducts (QueryBase queryBase);
    
    void updatetrainstatus();
    
    void updateclicktimesbyid(Integer id);

    boolean isExistsByVideoId(Integer videoId);

    boolean isExistsByPictureId(Integer pictureId);
}