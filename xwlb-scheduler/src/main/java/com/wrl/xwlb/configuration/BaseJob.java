package com.wrl.xwlb.configuration;

import com.wrl.xwlb.util.JsonUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public abstract class BaseJob implements Job {
  protected <M> M getParam(JobExecutionContext context, Class<M> clazz) {
    return JsonUtil.fromOrException(JsonUtil.toString(context.getJobDetail().getJobDataMap()), clazz);
  }
}
