package com.provectus.cardiff.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * Created by artemvlasov on 12/09/15.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan("com.provectus.cardiff.config")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("daoAuthenticationProvider")
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private CardiffAuthenticationEntryPoint entryPoint;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public CardiffUserDetailsService cardiffUserDetailsService() {
        return new CardiffUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        provider.setUserDetailsService(cardiffUserDetailsService());
        return provider;
    }

    @Bean
    public ProviderManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(daoAuthenticationProvider()));
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring().antMatchers("/style/**", "/js/**", "/app/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().authenticationEntryPoint(entryPoint)
                .and()
                    .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/", "/logout", "/signin", "/signup", "/cards", "/test", "/card/info",
                            "/rest/location/**", "/rest/card/get/**", "/rest/person/check/**",
                            "/rest/person/registration", "/rest/person/login")
                        .permitAll()
                    .antMatchers("/admin/persons", "/rest/person/admin/**", "/rest/card/delete",
                            "/rest/tag/custom/admin/**")
                        .hasAuthority("ADMIN")
                .antMatchers("/account/update", "/account", "/card/add", "/rest/card/**",
                        "/rest/person/**", "/rest/card/booking/**", "/rest/tag/custom/**")
                        .hasAnyAuthority("USER", "ADMIN")
                    .anyRequest()
                        .authenticated()
                .and()
                    .formLogin().loginPage("/signin").loginProcessingUrl("/rest/person/login")
                    .passwordParameter("password").failureHandler(new SimpleUrlAuthenticationFailureHandler()).usernameParameter("loginData").permitAll()
                .and()
                    .logout().logoutUrl("/rest/person/logout").logoutSuccessUrl("/")
                .and()
                    .rememberMe().tokenValiditySeconds(604800).rememberMeParameter("rememberMe")
                    .userDetailsService(cardiffUserDetailsService()).tokenRepository(persistentTokenRepository());
    }
}
