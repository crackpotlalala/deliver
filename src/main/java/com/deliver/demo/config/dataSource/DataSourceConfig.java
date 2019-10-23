package com.deliver.demo.config.dataSource;

import com.deliver.demo.enums.DataSourceNames;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan("com.deliver.demo.mapper")
public class DataSourceConfig {
    /**
     * db1数据库配置
     */
    @Bean(DataSourceNames.FIRST)
    @ConfigurationProperties(prefix = "db1.datasource")
    public DataSource db1Source() {
        return DataSourceBuilder.create().build();
    }

    /**
     * db2数据库配置
     */
    @Bean(DataSourceNames.SECOND)
    @ConfigurationProperties(prefix = "db2.datasource")
    public DataSource db2Source() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 动态数据库配置
     */
    @Primary
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(db1Source());

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap(5);
        dsMap.put(DataSourceNames.FIRST, db1Source());
        dsMap.put(DataSourceNames.SECOND, db2Source());

        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;
    }

    /**
     * 配置@Transactional注解事物
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }

}
