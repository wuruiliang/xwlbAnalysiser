package com.wrl.xwlb.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationListenerConfiguration {
  @Bean
  public EurekaServiceGracefulShutdownListener shutdownListener(ApplicationContext context) {
    return new EurekaServiceGracefulShutdownListener(context, 65);
  }

  @Bean
  public EurekaServiceReadyListener readyListener() {
    return new EurekaServiceReadyListener();
  }
}
