package com.aaa.lee.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.aaa.lee.shiro.realm.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2019/8/3 13:11
 * @Description
 *      @SpringBootApplication：把ShiroConfig类作为自动配置类加载进springboot的容器中
 *          也就是相当于spring配置文件中的shiro.xml配置
 **/
@SpringBootApplication
public class ShiroConfig {

    /**
     * @author Seven Lee
     * @description
     *      把shiro的生命周期交给spring进行托管
     *      也就是相当于创建了一个<bean></bean>
     * @param []
     * @date 2019/8/3
     * @return org.apache.shiro.spring.LifecycleBeanPostProcessor
     * @throws
    **/
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor processor = new LifecycleBeanPostProcessor();
        return processor;
    }

    /**
     * @author Seven Lee
     * @description
     *      配置的shiro的密码加密
     *          HashAlgorithmName:使用加密方式(RES,MD5...)
     *          HashIterations:对密码加密的次数
     *          StoredCredentialsHexEncoded:把密码转换为16进制
     * @param []
     * @date 2019/8/3
     * @return org.apache.shiro.authc.credential.HashedCredentialsMatcher
     * @throws
    **/
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("MD5");
        matcher.setHashIterations(1024);
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }

    /**
     * @author Seven Lee
     * @description
     *      定义自己的Realm对象
     *      自定义realm对象
     *      如果realm中需要对密码进行加密格式的管理可以使用set的形式把密码加密放入
     * @param []
     * @date 2019/8/3
     * @return com.aaa.lee.shiro.realm.ShiroRealm
     * @throws
    **/
    @Bean(name = "shiroRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        // shiroRealm.setCacheManager(ehCacheManager());
        return shiroRealm;
    }

    /**
     * @author Seven Lee
     * @description
     *      shiro中使用缓存的方式(默认的情况下使用的是ehCache)
     *          可以使用任意缓存进行代替(redis,mamercache,mongoDB,Hbase...)
     *          需要自己实现配置
     * @param []
     * @date 2019/8/3
     * @return org.apache.shiro.cache.ehcache.EhCacheManager
     * @throws 
    **/
    @Bean(name = "ehCacheManager")
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager ehCacheManager() {
        return new EhCacheManager();
    }

    /**
     * @author Seven Lee
     * @description
     *      securityManager:调度shiro的所有模块之间的配合(相当于dubbo的zookeeper)
     * @param []
     * @date 2019/8/3
     * @return org.apache.shiro.web.mgt.DefaultWebSecurityManager
     * @throws 
    **/
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        //securityManager.setCacheManager(ehCacheManager());
        return securityManager;
    }

    /**
     * @author Seven Lee
     * @description
     *      1.创建shiro过滤器的工厂对象:ShiroFilterFactoryBean
     *      2.创建工厂目的就是需要获取到shiro过滤器链
     *      3.shiro过滤器链的作用就是和shiro.xml配置<bean id="shiroFilter"></bean>
     *          创建的作用就是为了一系列的shiro过滤配置信息
     *      4.创建LinkedHashMap对象
     *          put(key, value)
     *          key:代表了路径
     *          value:shiro所支持的标签
     *
     * @param []
     * @date 2019/8/3
     * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
     * @throws
    **/
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
        filter.setSecurityManager(securityManager());
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        filter.setLoginUrl("/login");
       // filter.setSuccessUrl("/index");
        filter.setUnauthorizedUrl("/404");

        filter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return filter;
    }

    /**
     * @author Seven Lee
     * @description
     *      @ConditionalOnMissingBean:
     *          在shiro会自动加载DefaultAdvisorAutoProxyCreator对象
     *          当shiro并没有进行加载的时候以上注解所标注的类才会被加载进springboot的配置文件
     *          <bean id="DefaultAdvisorAutoProxyCreator"></bean>
     *          spring并没有去加载shiro自带的，才会去加载
     * @param []
     * @date 2019/8/3
     * @return org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
     * @throws 
    **/
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * @author Seven Lee
     * @description
     *      AuthorizationAttributeSourceAdvisor:
     *          为了加载shiro的各个对象(元数据)
     * @param []
     * @date 2019/8/3
     * @return org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
     * @throws 
    **/
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aASA = new AuthorizationAttributeSourceAdvisor();
        aASA.setSecurityManager(securityManager());
        return aASA;
    }

    /**
     * @author Seven Lee
     * @description
     *      在页面上使用shiro标签的时候
     *      如果是jsp，直接导入shiro的标准标签库即可
     *      如果thymeleaf需要导入jar包，并且配置shiro的方言
     * @param []
     * @date 2019/8/3
     * @return at.pollux.thymeleaf.shiro.dialect.ShiroDialect
     * @throws 
    **/
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

}
