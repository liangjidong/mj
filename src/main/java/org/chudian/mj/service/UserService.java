package org.chudian.mj.service;

import org.chudian.mj.bean.User;

/**
 * Created by onglchen
 * on 15-3-11.
 */
public interface UserService extends BaseService<User> {

    public int login(User user);

    public User selectByName(String name);
}