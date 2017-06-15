package com.cloud.chuchen;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableOAuth2Sso
@SpringCloudApplication
@EnableHystrix
@EnableZuulProxy
public class ZuulServerApplication extends WebSecurityConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/index.html", "/assets/**", "/login", "/api/catalog/**").permitAll().anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()); // What Angular would like is for the server to send it a cookie called "XSRF-TOKEN" and if it sees that, it will send the value back as a header named "X-XSRF-TOKEN". 需要注意withHttpOnlyFalse后容易受到XSS攻击
        //.and().csrf().disable(); // 这样虽然可以工作，但不安全
    }

    ///**
    // * 使用认证信息过滤器添加是否已认证的相关信息
    // * @return
    // */
    //@Bean
    //public FilterRegistrationBean authInfoFilterRegistration() {
    //    FilterRegistrationBean registration = new FilterRegistrationBean();
    //    registration.setFilter(new AuthInfoFilter());
    //    registration.setName("authInfoFilter");
    //    registration.setDispatcherTypes(DispatcherType.REQUEST);
    //    registration.addUrlPatterns("/*"); // 记住是"/*"，而不是"/**"，后者不会过滤请求
    //    registration.setOrder(1);
    //    return registration;
    //}
}

