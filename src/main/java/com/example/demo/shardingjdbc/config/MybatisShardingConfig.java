package com.example.demo.shardingjdbc.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Jon Chan
 * @date 2021/11/13 3:12 下午
 */
@AutoConfigureAfter({CommonConfig.class})
@Configuration
@MapperScan(basePackages = "com.example.demo.shardingjdbc.mapper.sharding",sqlSessionFactoryRef = "shardingSqlSessionFactory")
@RequiredArgsConstructor
public class MybatisShardingConfig {
    @Resource(name = "primaryDatasource")
    private DataSource primaryDatasource;


    @Bean(name = "shardingDataSource")
    DataSource shardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getCourseTableRuleConfiguration());
        // shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("course");
        // shardingRuleConfig.getBroadcastTables().add("t_config"); 广播表
        // shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
        // shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", new ModuloShardingTableAlgorithm()));
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }

    /**
     * 主键策略
     * @return
     */
    private static KeyGeneratorConfiguration getKeyGeneratorConfiguration() {
        KeyGeneratorConfiguration result = new KeyGeneratorConfiguration("SNOWFLAKE", "id");
        return result;
    }

    TableRuleConfiguration getCourseTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("course", "t1.course_$->{1..2}");// 指定表的分布情况
        result.setKeyGeneratorConfig(getKeyGeneratorConfiguration());
        // 设置分表规则
        result.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id","course_$->{id % 2 + 1}"));
        return result;
    }

    TableRuleConfiguration getOrderItemTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("t_order_item", "ds${0..1}.t_order_item${0..1}");
        return result;
    }

    Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("t1", primaryDatasource);
        return result;
    }

    @Bean(name = "shardingSqlSessionFactory")
    public SqlSessionFactory shardingSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(shardingDataSource());
        //sqlSessionFactory.setMapperLocations(resolveMapperLocations(environment.getProperty("mybusiness.primary.mapper-locations").split(",")));
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setDbConfig(new GlobalConfig.DbConfig().setDbType(DbType.MYSQL));
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
    }
}
