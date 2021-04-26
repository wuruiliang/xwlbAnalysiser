package com.wrl.xwlb.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionType;
import com.wrl.xwlb.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.wrl.xwlb.service.vo.JobVO;

import java.util.Map;

@Slf4j
@Service
public class JobService {

  @Autowired
  private Scheduler scheduler;

  private static final String NORMAL_GROUP = "NORMAL";

  private static final String TEMP_GROUP = "TEMP";

  public void addJob(JobVO jobVO) throws SchedulerConfigException {
    checkParam(jobVO);

    loadJob(jobVO);
  }

  /**
   * 临时触发一次任务
   * @param name
   */
  public void triggerJob(String name, String param) throws SchedulerException {
    if (StringUtils.isEmpty(param)) {
      param = "{}";
    }

    checkJson(param);

    checkJobExist(name);

    tempTriggerJob(name, param);
  }

  public void pauseJob(String name) throws SchedulerException {
    checkJobExist(name);
    scheduler.pauseJob(JobKey.jobKey(name, NORMAL_GROUP));
  }

  public void resumeJob(String name) throws SchedulerException {
    checkJobExist(name);
    scheduler.resumeJob(JobKey.jobKey(name, NORMAL_GROUP));
  }


  private void checkParam(JobVO jobVO) {
    if (StringUtils.isEmpty(jobVO.getParam())) {
      jobVO.setParam("{}");
    }

    try {
      Class.forName(jobVO.getClassName()).asSubclass(Job.class);
    } catch (ClassNotFoundException ce) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "类名不存在。");
    }

    if(!CronExpression.isValidExpression(jobVO.getCron())) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "cron表达式错误。");
    }

    checkJson(jobVO.getParam());
  }

  private void checkJson(String json) {
    try {
      JsonUtil.fromOrException(json, Object.class);
    } catch (Exception e) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "json格式错误。");
    }
  }

  private void checkJobExist(String jobName) throws SchedulerException {
    JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, NORMAL_GROUP));
    if (jobDetail == null) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, String.format("name=%s的job不存在。", jobName));
    }
  }

  private void tempTriggerJob(String jobName, String param) throws SchedulerException {
    JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, NORMAL_GROUP));

    JobDetail tempJobDetail = JobBuilder.newJob()
        .withIdentity(JobKey.jobKey(jobName, TEMP_GROUP))
        .withDescription(jobDetail.getDescription())
        .usingJobData(new JobDataMap(JsonUtil.fromOrException(param, new TypeReference<Map<?, ?>>() {})))
        .ofType(jobDetail.getJobClass()).build();

    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TEMP_GROUP).build();

    scheduler.scheduleJob(tempJobDetail, trigger);
  }


  private void loadJob(JobVO jobVO) throws SchedulerConfigException {
    try {
      JobDetail jobDetail = getJobDetail(jobVO);
      CronTrigger cronTrigger = getCronTrigger(jobVO);
      scheduler.scheduleJob(jobDetail, cronTrigger);
    } catch (Exception e) {
      log.error("加载定时任务异常",e);
      throw new SchedulerConfigException("加载定时任务异常", e);
    }
  }

  public void unloadJob(String name) throws SchedulerException {
    checkJobExist(name);

    // 停止触发器
    scheduler.pauseTrigger(TriggerKey.triggerKey(name, NORMAL_GROUP));

    // 卸载定时任务
    scheduler.unscheduleJob(TriggerKey.triggerKey(name, NORMAL_GROUP));

    // 删除原来的job
    scheduler.deleteJob(JobKey.jobKey(name, NORMAL_GROUP));
  }

  public void reload(JobVO jobVO) throws SchedulerException {
    checkParam(jobVO);

    unloadJob(jobVO.getName());

    loadJob(jobVO);
  }

  //组装JobDetail
  private JobDetail getJobDetail(JobVO jobVO) throws ClassNotFoundException {
    return JobBuilder.newJob()
        .withIdentity(JobKey.jobKey(jobVO.getName(), NORMAL_GROUP))
        .withDescription(jobVO.getDesc())
        .usingJobData(new JobDataMap(
            JsonUtil.fromOrException(jobVO.getParam(),
            new TypeReference<Map<?, ?>>() {})
        ))
        .ofType(Class.forName(jobVO.getClassName()).asSubclass(Job.class))
        .build();
  }

  //组装CronTrigger
  private CronTrigger getCronTrigger(JobVO jobVO){
    return TriggerBuilder.newTrigger()
        .withIdentity(TriggerKey.triggerKey(jobVO.getName(), NORMAL_GROUP))
        .withSchedule(CronScheduleBuilder.cronSchedule(jobVO.getCron()))
        .build();
  }

}
