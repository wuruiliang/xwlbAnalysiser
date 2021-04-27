package com.wrl.xwlb.controller;

import com.wrl.xwlb.common.mvc.CommonResponse;
import com.wrl.xwlb.common.mvc.request.scheduler.AddJobRequest;
import com.wrl.xwlb.common.mvc.request.scheduler.TriggerJobRequest;
import com.wrl.xwlb.service.IJobService;
import com.wrl.xwlb.service.vo.JobVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {

  @Autowired
  private IJobService jobService;

  @RequestMapping("addJob")
  public CommonResponse addJob(@RequestBody AddJobRequest request) {
    jobService.addJob(JobVO.fromRequest(request));
    return CommonResponse.success();
  }

  @RequestMapping("updateJob")
  public CommonResponse updateJob(@RequestBody AddJobRequest request) {
    jobService.updateJob(JobVO.fromRequest(request));
    return CommonResponse.success();
  }

  @RequestMapping("triggerJob")
  public CommonResponse triggerJob(@RequestBody TriggerJobRequest request) {
    jobService.triggerJob(request.getName(), request.getParam());
    return CommonResponse.success();
  }

  @RequestMapping("pauseJob")
  public CommonResponse pauseJob(@RequestParam String name) {
    jobService.pauseJob(name);
    return CommonResponse.success();
  }

  @RequestMapping("resumeJob")
  public CommonResponse resumeJob(@RequestParam String name) {
    jobService.resumeJob(name);
    return CommonResponse.success();
  }

  @RequestMapping("removeJob")
  public CommonResponse removeJob(@RequestParam String name) {
    jobService.removeJob(name);
    return CommonResponse.success();
  }

}
