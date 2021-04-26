package com.wrl.xwlb.controller;

import com.wrl.xwlb.common.mvc.CommonResponse;
import com.wrl.xwlb.common.mvc.request.scheduler.AddJobRequest;
import com.wrl.xwlb.common.mvc.request.scheduler.TriggerJobRequest;
import com.wrl.xwlb.service.vo.JobVO;
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
    jobService.addJob(JobVO.fromRequest(request));
    return CommonResponse.success();
  }

  @RequestMapping("updateJob")
  public CommonResponse updateJob(@RequestBody AddJobRequest request) throws SchedulerException {
    jobService.reload(JobVO.fromRequest(request));
    return CommonResponse.success();
  }

  @RequestMapping("triggerJob")
  public CommonResponse triggerJob(@RequestBody TriggerJobRequest request) throws SchedulerException {
    jobService.triggerJob(request.getName(), request.getParam());
    return CommonResponse.success();
  }

  @RequestMapping("pauseJob")
  public CommonResponse pauseJob(@RequestParam String name) throws SchedulerException {
    jobService.pauseJob(name);
    return CommonResponse.success();
  }

  @RequestMapping("resumeJob")
  public CommonResponse resumeJob(@RequestParam String name) throws SchedulerException {
    jobService.resumeJob(name);
    return CommonResponse.success();
  }

  @RequestMapping("removeJob")
  public CommonResponse removeJob(@RequestParam String name) throws SchedulerException {
    jobService.unloadJob(name);
    return CommonResponse.success();
  }

}
