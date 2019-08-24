package com.aaa.lee.shiro.realm;

import com.aaa.lee.shiro.mapper.UserMapper;
import com.aaa.lee.shiro.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/8/3 15:32
 * @Description
 **/
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * @author Seven Lee
     * @description
     *      认证方法
     * @param authenticationToken
     * @date 2019/8/3
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @throws
    **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1.从authenticationToken对象中获取用户名
        String username = (String)authenticationToken.getPrincipal();
        // 2.根据用户名查询用户是否存在
        User user = userMapper.selectUserByUsername(username);
        // 3.判断
        if(null == user) {
            throw new UnknownAccountException("该用户不存在，请重新搜索！");
        }
        // 4.用户存在，需要匹配密码
            // 第一个参数是用户名
            // 第二个参数是密码
            // 第三个参数盐
            // 第四个参数realmName
        // ！！！！！所有的用户名和密码都必须从数据库中获取
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName());
        // 5.存入shiro的session中
         // getSession():是有形参的，参数类型是Boolean
        // true/false，默认值为true
        // true--->false:不会创建新的session对象，一直会从一个session中取，如果当前系统中没有session则返回null
        SecurityUtils.getSubject().getSession().setAttribute("user", user);
        return info;
        // 当认证方法成功以后，shiro会默认跳转到"/"，也就是在shiro的配置文件中配置filter.setSuccessUrl("/index");
        // 这个路径就是跳转到项目根目录中！！最好的情况下需要自己写controller，不用shiro自带的配置信息
    }

    /**
     * @author Seven Lee
     * @description
     *      授权方法
     *      授权方法不生效！！
     *      无论是认证还是授权，只有shiro过滤器检测到(页面：有shiro标签存在，类中：有授权的配置存在)才会被调用，否则默认情况下shiro会认证该项目用不到授权阶段
     *           自动会把授权失效!!
     * @param principalCollection
     * @date 2019/8/3
     * @return org.apache.shiro.authz.AuthorizationInfo
     * @throws
    **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 1.获取用户名/用户对象
            // 具体获取到的是什么，取决于在认证阶段传递的是什么值
            //   SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), this.getName());
            // 上面方法的第一个参数决定的！！！！
        String username = (String)principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 2.通过用户名查询所有的角色信息
        if(!"".equals(username) && null != username) {
            List<String> roleList = userMapper.selectRolesByUsername(username);
            if(roleList.size() > 0) {
                // 3.把角色信息存入到返回值中
                info.addRoles(roleList);
            }
            // 4.通过用户名查询权限信息
            List<String> permissionList = userMapper.selectPermissionsByUsername(username);
            if(permissionList.size() > 0) {
                info.addStringPermissions(permissionList);
            }
        }
        return info;
    }

    public static void main(String[] args) {
        // 加密的方式
        String hashAlgorithmName = "MD5";
        // 明文的密码
        Object credentials = "123456";
        // 盐值
        Object salt = ByteSource.Util.bytes("98k");
        // 加密的次数
        int hashIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        // result:加密过后的密码
        System.out.println(result);
    }



}
