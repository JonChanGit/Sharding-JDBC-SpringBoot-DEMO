package com.example.demo.shardingjdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Jon Chan
 * @date 2021/11/13 11:19 上午
 */
@AutoConfigureAfter({CommonConfig.class})
@Configuration
@MapperScan(basePackages = "com.example.demo.shardingjdbc.mapper.alone",sqlSessionFactoryRef = "primarySqlSessionFactory")
@AllArgsConstructor
public class MybatisPrimaryConfig {

    private PathMatchingResourcePatternResolver resolver;
    private Environment environment;


    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 主数据源
     * @return
     */
    @Bean(name = "primaryDatasource")
    @ConfigurationProperties(prefix = "datasource.primary")
    public DataSource primaryDatasource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 事务管理器
     * @return
     */
    @Bean(name = "primaryDatasourceTransactionManager")
    public DataSourceTransactionManager primaryDatasourceTransactionManager(){
        return new DataSourceTransactionManager(primaryDatasource());
    }

    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(primaryDatasource());
        //sqlSessionFactory.setMapperLocations(resolveMapperLocations(environment.getProperty("mybusiness.primary.mapper-locations").split(",")));
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setDbConfig(new GlobalConfig.DbConfig().setDbType(DbType.MYSQL));
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
    }

    public Resource[] resolveMapperLocations(String[] locations) {
        return Stream.of(Optional.ofNullable(locations).orElse(new String[0]))
                .flatMap(location -> Stream.of(getResources(location)))
                .toArray(Resource[]::new);
    }


    private Resource[] getResources(String location) {
        try {
            return resolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

}
