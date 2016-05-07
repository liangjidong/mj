package org.chudian.mj.service.impl;

import com.alibaba.druid.util.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chudian.mj.bean.User;
import org.chudian.mj.common.QueryBase;
import org.chudian.mj.common.Status;
import org.chudian.mj.mapper.UserMapper;
import org.chudian.mj.service.UserService;
import org.chudian.mj.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by onglchen
 * on 15-3-11.
 */
@Service
public class UserServiceImpl implements UserService {

    private Log logger = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public int add(User user) {
        User user_db = userMapper.queryByName(user.getName());
        if(user_db == null) {
            String pwd = user.getPassword();
            String encpwd = EncryptionUtil.encrypt(pwd);
            user.setPassword(encpwd);
            if (userMapper.insert(user) > 0) {
                logger.debug("添加用户" + user.getName() + "成功");
                return Status.SUCCESS;
            }
        }
        return Status.EXISTS;
    }


    /**
     *
     * @param user
     * @return 0 成功  1 失败  7 不存在
     */
    @Override
    public int update(User user) {
        User user2 =userMapper.selectByPrimaryKey(user.getId());
        if(user2 == null){
            logger.warn("尝试更新用户" + user.getId() + " ,但是用户不存在");
            return Status.NOT_EXISTS;
        }
        //为用户密码加密
        if (user.getPassword() != null) {
            String pwd = user.getPassword();
            String encryptedPassword = EncryptionUtil.encrypt(pwd);
            if (!StringUtils.isEmpty(user.getPassword())) {
                if (user2.getPassword().equals(pwd))
                    user.setPassword(null);
                else
                    user.setPassword(encryptedPassword);
            }
        }
        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            logger.debug("更新用户成功 UserName: " + user2.getName());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    /**
     *
     * @param user
     * @return 0 成功 1 失败  7 不存在
     */
    @Override
    public int delete(User user) {
        User user2 = userMapper.selectByPrimaryKey(user.getId());
        if (user2 == null) {
            logger.warn("尝试删除用户，但是用户不存在");
            return Status.NOT_EXISTS;
        }
        if (userMapper.deleteByPrimaryKey(user.getId()) > 0 ) {
            logger.debug("删除用户成功 Username: " + user.getName());
            return Status.SUCCESS;
        }
        return Status.ERROR;
    }

    /**
     *
     * @param id
     * @return doctor
     */
    @Override
    public User get(int id) {
        User user2 = userMapper.selectByPrimaryKey(id);
        if (user2 == null) {
            logger.warn("用户 ID: " + id + " 不存在");
        } else {
            logger.debug("用户 ID: " + id + "成功");
        }
        return user2;
    }


    /**
     *
     * @param queryBase
     */
    @Override
    public void query(QueryBase queryBase) {
        if (logger.isDebugEnabled()) {
            logger.debug("根据参数 " + queryBase.getParameters() + "  查询医生");
        }
        queryBase.setResults(userMapper.queryUsers(queryBase));
        queryBase.setTotalRow(userMapper.countUsers(queryBase));
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public int login(User user) {
        User user2 = userMapper.queryByName(user.getName());
        if (user2 == null) {
            return Status.NOT_EXISTS;
        }
        String encryptPassword = EncryptionUtil.encrypt(user.getPassword());
        if(!encryptPassword.equals(user2.getPassword())){
            return Status.PASSWD_NOT_MATCH;
        }

        user.setId(user2.getId());
        user.setHeadImg(user2.getHeadImg());
        return Status.SUCCESS;
    }
    
    @Override
    public User selectByName(String name){
    	return userMapper.selectByName(name);
    }
}
