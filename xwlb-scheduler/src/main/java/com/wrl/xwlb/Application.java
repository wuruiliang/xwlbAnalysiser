package com.wrl.xwlb;

import com.wrl.xwlb.service.impl.QuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.wrl.xwlb.controller")
@Import(CoreConfiguration.class)
@EnableScheduling
@EnableEurekaClient
public class Application implements CommandLineRunner {

  @Autowired
  private QuartzJobService jobService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... strings) {
//    jobService.startJob();
  }
}
