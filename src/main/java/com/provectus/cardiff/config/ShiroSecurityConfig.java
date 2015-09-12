package com.provectus.cardiff.config;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by artemvlasov on 24/08/15.
 */
@Configuration
public class ShiroSecurityConfig {

    @Bean
    public CardiffRealm getCardiffRealm() {
        return new CardiffRealm();
    }
    @Bean
    public MemoryConstrainedCacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }
    @Bean
    public WebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(getCardiffRealm());
        manager.setCacheManager(cacheManager());
        return manager;
    }
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{securityManager()});
        return factoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl("/signin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/");
        Map<String, String> chainDefinitionMap = new HashMap<>();
        chainDefinitionMap.put("/rest/person/login", "anon");
        chainDefinitionMap.put("/rest/person/registration", "anon");
        chainDefinitionMap.put("/rest/person/admin", "authc, roles[ADMIN]");
        chainDefinitionMap.put("/rest/person/delete/*", "authc, roles[ADMIN]");
        chainDefinitionMap.put("/rest/person/restore/*", "authc, roles[ADMIN]");
        chainDefinitionMap.put("/rest/person/update/role/*", "authc, roles[ADMIN]");
        chainDefinitionMap.put("/rest/person/authenticated", "authc");
        chainDefinitionMap.put("/rest/person/password/update", "authc");
        chainDefinitionMap.put("/rest/person/update", "authc");
        chainDefinitionMap.put("/rest/person/authorized", "authc");
        chainDefinitionMap.put("/rest/person/get/*", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionMap);
        return shiroFilterFactoryBean;
    }
}
