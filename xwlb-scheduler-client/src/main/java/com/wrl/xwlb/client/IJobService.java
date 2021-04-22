package com.wrl.xwlb.client;

import com.wrl.xwlb.common.mvc.CommonResponse;
import com.wrl.xwlb.common.mvc.request.scheduler.AddJobRequest;
import com.wrl.xwlb.common.mvc.request.scheduler.TriggerJobRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@SchedulerFeignClient
@RequestMapping("/job")
public interface IJobService {

  @RequestMapping(value = "addJob", method = RequestMethod.POST)
  CommonResponse addJob(AddJobRequest request);

  @RequestMapping(value = "triggerJob", method = RequestMethod.POST)
  CommonResponse triggerJob(TriggerJobRequest request);

  @RequestMapping(value = "stopJob", method = RequestMethod.GET)
  CommonResponse stopJob(String name);

  @RequestMapping(value = "restartJob", method = RequestMethod.GET)
  CommonResponse restartJob(String name);

}
