package com.wrl.xwlb.controller;

import com.wrl.xwlb.client.IJobService;
import com.wrl.xwlb.common.mvc.CommonResponse;
import com.wrl.xwlb.common.mvc.request.scheduler.AddJobRequest;
import com.wrl.xwlb.common.mvc.request.scheduler.TriggerJobRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

  @Autowired
  private IJobService jobService;

  @RequestMapping(value = "addJob", method = RequestMethod.POST)
  public CommonResponse addJob(@RequestBody AddJobRequest request) {
    return jobService.addJob(request);
  }

  @RequestMapping(value = "updateJob", method = RequestMethod.POST)
  public CommonResponse updateJob(@RequestBody AddJobRequest request) {
    return jobService.updateJob(request);
  }

  @RequestMapping(value = "triggerJob", method = RequestMethod.POST)
  public CommonResponse triggerJob(@RequestBody TriggerJobRequest request) {
    return jobService.triggerJob(request);
  }

  @RequestMapping("pauseJob")
  public CommonResponse pauseJob(@RequestParam String name) {
    return jobService.pauseJob(name);
  }

  @RequestMapping("resumeJob")
  public CommonResponse restartJob(@RequestParam String name) {
    return jobService.resumeJob(name);
  }

  @RequestMapping("removeJob")
  public CommonResponse removeJob(@RequestParam String name) {
    return jobService.removeJob(name);
  }

}
