package org.chudian.mj.mapper;

import org.chudian.mj.bean.User;
import org.chudian.mj.common.QueryBase;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);
    
    User selectByName(String name);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User queryByName(String name);

    List<User> queryUsers(QueryBase queryBase);

    long countUsers(QueryBase queryBase);
    
}