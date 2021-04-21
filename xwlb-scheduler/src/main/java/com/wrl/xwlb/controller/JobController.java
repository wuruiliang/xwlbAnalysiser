package com.wrl.xwlb.controller;

import com.wrl.xwlb.common.mvc.CommonResponse;
import com.wrl.xwlb.controller.request.AddJobRequest;
import com.wrl.xwlb.controller.request.TriggerJobRequest;
import com.wrl.xwlb.service.JobService;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {

  @Autowired
  private JobService jobService;

  @RequestMapping("addJob")
  public CommonResponse addJob(@RequestBody AddJobRequest request) throws SchedulerConfigException {
    jobService.addJob(request.getName(), request.getClassName(), request.getCron(), request.getParam(), request.getDesc());
    return CommonResponse.success();
  }

  @RequestMapping("triggerJob")
  public CommonResponse triggerJob(@RequestBody TriggerJobRequest request) throws ClassNotFoundException, SchedulerException {
    jobService.triggerJob(request.getName(), request.getParam());
    return CommonResponse.success();
  }

  @RequestMapping("stopJob")
  public CommonResponse stopJob(@RequestParam String name) throws SchedulerException {
    jobService.stopJob(name);
    return CommonResponse.success();
  }

  @RequestMapping("restartJob")
  public CommonResponse restartJob(@RequestParam String name) throws SchedulerException, ClassNotFoundException {
    jobService.restartJob(name);
    return CommonResponse.success();
  }

}
