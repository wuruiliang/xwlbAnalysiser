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

  @RequestMapping(value = "triggerJob", method = RequestMethod.POST)
  public CommonResponse triggerJob(@RequestBody TriggerJobRequest request) {
    return jobService.triggerJob(request);
  }

  @RequestMapping("stopJob")
  public CommonResponse stopJob(@RequestParam String name) {
    return jobService.stopJob(name);
  }

  @RequestMapping("restartJob")
  public CommonResponse restartJob(@RequestParam String name) {
    return jobService.restartJob(name);
  }

}
