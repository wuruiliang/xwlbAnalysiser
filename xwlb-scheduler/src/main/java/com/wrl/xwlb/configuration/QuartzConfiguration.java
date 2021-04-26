package com.wrl.xwlb.configuration;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

@Configuration
public class QuartzConfiguration {
  @Resource(name = "jobExecutor")
  private Executor jobExecutor;

  @Bean
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }

  @Bean("schedulerFactoryBean")
  public SchedulerFactoryBean createFactoryBean(JobFactory jobFactory) throws IOException {
    SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
    factoryBean.setJobFactory(jobFactory);
    factoryBean.setTaskExecutor(jobExecutor);
    factoryBean.setOverwriteExistingJobs(true);
    factoryBean.setQuartzProperties(quartzProperties());
    return factoryBean;
  }
  //通过这个类对定时任务进行操作
  @Bean
  public Scheduler scheduler(@Qualifier("schedulerFactoryBean") SchedulerFactoryBean factoryBean) {
    return factoryBean.getScheduler();
  }
}


