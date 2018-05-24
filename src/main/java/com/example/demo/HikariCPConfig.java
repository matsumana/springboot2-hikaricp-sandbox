package com.example.demo;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class HikariCPConfig {

    @Bean(destroyMethod = "close")
    public DataSource dataSource(MBeanExporter mBeanExporter, DataSourceProperties properties) {
        DataSource ds = DataSourceBuilder.create(properties.getClassLoader())
                                         .type(HikariDataSource.class)
                                         .driverClassName(properties.determineDriverClassName())
                                         .url(properties.determineUrl())
                                         .username(properties.determineUsername())
                                         .password(properties.determinePassword())
                                         .build();

        // Configuration to monitor HikariCP metrics via JMX
        HikariDataSource dataSource = (HikariDataSource) ds;
        dataSource.setRegisterMbeans(true);
        dataSource.setPoolName("dataSource");
        mBeanExporter.addExcludedBean("dataSource");

        return dataSource;
    }
}
