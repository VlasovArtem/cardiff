package com.provectus.cardiff.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Created by artemvlasov on 23/08/15.
 */
public class CardiffAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {
                ProductionDataSourceConfig.class,
                DevelopmentDataSourceConfig.class,
                RootContextConfig.class,
                AppConfig.class,
                ShiroSecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { ServletContextConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/*" };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("shiroFilter");
        delegatingFilterProxy.setTargetFilterLifecycle(true);
        return new Filter[] {
                characterEncodingFilter, new HttpPutFormContentFilter(), delegatingFilterProxy
        };
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        WebApplicationContext context = super.createRootApplicationContext();
        ((ConfigurableEnvironment) context.getEnvironment()).setActiveProfiles("development");
        return context;
    }
}
