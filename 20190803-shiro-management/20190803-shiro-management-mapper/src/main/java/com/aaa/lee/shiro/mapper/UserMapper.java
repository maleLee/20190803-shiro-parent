package com.aaa.lee.shiro.mapper;

import com.aaa.lee.shiro.model.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User selectUserByUsername(String username);

    List<String> selectRolesByUsername(String username);

    List<String> selectPermissionsByUsername(String username);
}