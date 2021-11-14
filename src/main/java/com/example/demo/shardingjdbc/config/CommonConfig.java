package com.example.demo.shardingjdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author Jon Chan
 * @date 2021/11/13 11:18 上午
 */
@Configuration
public class CommonConfig {
    @Bean
    public PathMatchingResourcePatternResolver resourcePatternResolver(){
        return new PathMatchingResourcePatternResolver();
    }
}
