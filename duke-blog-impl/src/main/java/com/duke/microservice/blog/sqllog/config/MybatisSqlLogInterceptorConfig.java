package com.duke.microservice.blog.sqllog.config;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.duke.microservice.blog.sqllog.interceptor.*;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created duke on 2018/6/28
 */
@Configuration
public class MybatisSqlLogInterceptorConfig {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactories;

    @PostConstruct
    public void addMybatisSqlLogInterceptor() {
        Interceptor interceptor = new MybatisSqlLogInterceptor();

        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }
}
