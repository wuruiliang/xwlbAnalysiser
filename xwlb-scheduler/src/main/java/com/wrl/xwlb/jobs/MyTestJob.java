package com.wrl.xwlb.jobs;

import com.wrl.xwlb.configuration.BaseJob;
import com.wrl.xwlb.util.ClockUtil;
import com.wrl.xwlb.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyTestJob extends BaseJob {

  @Override
  public void execute(JobExecutionContext jobExecutionContext) {
    Param param = getParam(jobExecutionContext, Param.class);

    log.info("job execute. name={}, time={}, param={}",
        jobExecutionContext.getJobDetail().getKey(),
        ClockUtil.dateTimeStringFromTimestamp(ClockUtil.now()),
        JsonUtil.toString(param));
  }

  @Data
  public static class Param {
    private String type;
  }
}
