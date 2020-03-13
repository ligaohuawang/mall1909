package com.qf.service;

import com.qf.entity.User;

public interface IUserService {
    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    User selectUserByUsername(String username);

    /**
     * 修改用户密码
     *
     * @param username
     * @param newpassword
     */
    void updatePassword(String username, String newpassword);
}
