package com.wrl.xwlb.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.wrl.xwlb.model.core.ExceptionTranslator;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JooqConfiguration {

  @Autowired
  MysqlConfig dbConfig;

  @Bean
  public DataSource dataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setUrl(dbConfig.getUrl());
    dataSource.setUsername(dbConfig.getUserName());
    dataSource.setPassword(dbConfig.getPassword());
    dataSource.setInitialSize(dbConfig.getInitialSize());
    dataSource.setMinIdle(dbConfig.getMinIdle());
    dataSource.setMaxActive(dbConfig.getMaxActive());
    dataSource.setMaxWait(dbConfig.getMaxWait());
    return dataSource;
  }

  @Bean
  public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
    return new DataSourceConnectionProvider(dataSource);
  }

  @Bean
  public ExceptionTranslator exceptionTransformer() {
    return new ExceptionTranslator();
  }

  @Bean
  public DefaultConfiguration configuration(
      DataSourceConnectionProvider connectionProvider, ExceptionTranslator exceptionTranslator) {

    DefaultConfiguration configuration = new DefaultConfiguration();
    configuration.set(connectionProvider);
    configuration.set(SQLDialect.MYSQL);
    configuration.set(new DefaultExecuteListenerProvider(exceptionTranslator));

    return configuration;
  }

  @Bean
  public DefaultDSLContext dslContext(DefaultConfiguration configuration) {
    return new DefaultDSLContext(configuration);
  }
}
