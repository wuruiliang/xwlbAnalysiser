package com.wrl.xwlb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CoreConfiguration.class)
public class SchedulerApplication {
  public static void main(String[] args) {
    SpringApplication.run(SchedulerApplication.class, args);
  }
}
