package com.wrl.xwlb.configuration;

import com.wrl.xwlb.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SchedulerListener implements CommandLineRunner {

  @Autowired
  private JobService jobService;

  @Override
  public void run(String... strings) {
    jobService.startJob();
  }
}
