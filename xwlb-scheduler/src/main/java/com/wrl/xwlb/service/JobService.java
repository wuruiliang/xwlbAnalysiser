package com.wrl.xwlb.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionType;
import com.wrl.xwlb.common.transactional.CommonTransactional;
import com.wrl.xwlb.enums.JobStatus;
import com.wrl.xwlb.model.JobConfigModel;
import com.wrl.xwlb.model.generated.tables.records.JobConfigRecord;
import com.wrl.xwlb.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JobService {

  @Autowired
  private JobConfigModel jobConfigModel;

  @Autowired
  private Scheduler scheduler;

  private static final String DEFAULT_GROUP = "DEFAULT";

  private static final String TEMP_GROUP = "TEMP";

  @CommonTransactional
  public void addJob(String name, String className, String cron, String param, String desc) throws SchedulerConfigException {
    if (StringUtils.isEmpty(param)) {
      param = "{}";
    }
    checkParam(className, cron, param);
    JobConfigRecord jobConfigRecord = jobConfigModel.init(name, className, cron, param, desc);
    loadJob(jobConfigRecord.getName());
  }

  /**
   * 全量加载并启动定时任务
   */
  public void startJob(){
    List<JobConfigRecord> jobConfigRecords = jobConfigModel.findByStatus(JobStatus.USING);
    if (CollectionUtils.isEmpty(jobConfigRecords)){
      log.info("定时任务加载数据为空");
      return;
    }
    for (JobConfigRecord record : jobConfigRecords) {
      CronTrigger cronTrigger;
      JobDetail jobDetail;
      try {
        cronTrigger = getCronTrigger(record);
        jobDetail = getJobDetail(record);
        scheduler.scheduleJob(jobDetail, cronTrigger);
        log.info("名称：{} 定时任务加载成功", record.getName());
      }catch (Exception e){
        log.error("名称：{} 定时任务加载失败", record.getName(), e);
      }
    }
    try {
      scheduler.start();
    } catch (SchedulerException e) {
      log.error("定时任务加载失败", e);
    }
  }

  /**
   * 临时触发一次任务
   * @param name
   */
  public void triggerJob(String name, String param) throws SchedulerException, ClassNotFoundException {
    JobConfigRecord record = jobConfigModel.findByNameOrException(name);
    if (StringUtils.isEmpty(param)) {
      param = "{}";
    }
    try {
      JsonUtil.fromOrException(param, Object.class);
    } catch (Exception e) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "param格式错误。");
    }
    tempTriggerJob(record, param);
  }


  /**
   * 停止任务
   * @param name
   */
  @CommonTransactional
  public void stopJob(String name) throws SchedulerException {
    JobConfigRecord record = jobConfigModel.findByNameOrException(name);
    unloadJob(name);
    jobConfigModel.updateStatus(record, JobStatus.STOPPED);
  }

  /**
   * 恢复任务
   * @param name
   * @throws SchedulerException
   */
  @CommonTransactional
  public void restartJob(String name) throws SchedulerException, ClassNotFoundException {
    JobConfigRecord record = jobConfigModel.findByNameOrException(name);
    reload(name);
    jobConfigModel.updateStatus(record, JobStatus.USING);
  }

  public void pauseJob(String name) throws SchedulerException {
    scheduler.pauseJob(JobKey.jobKey(name, DEFAULT_GROUP));
  }

  public void resumeJob(String name) throws SchedulerException {
    scheduler.resumeJob(JobKey.jobKey(name, DEFAULT_GROUP));
  }


  private void checkParam(String className, String cron, String param) {
    try {
      Class.forName(className).asSubclass(Job.class);
    } catch (ClassNotFoundException ce) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "类名不存在。");
    }
    if(!CronExpression.isValidExpression(cron)) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "cron表达式错误。");
    }
    try {
      JsonUtil.fromOrException(param, Object.class);
    } catch (Exception e) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "param格式错误。");
    }
  }

  private void tempTriggerJob(JobConfigRecord record, String param) throws ClassNotFoundException, SchedulerException {
    String jobCode = record.getName();
    JobDetail tempJobDetail = JobBuilder.newJob()
        .withIdentity(JobKey.jobKey(jobCode, TEMP_GROUP))
        .withDescription(record.getDescription())
        .usingJobData(new JobDataMap(JsonUtil.fromOrException(param, new TypeReference<Map<?, ?>>() {})))
        .ofType(Class.forName(record.getClassName()).asSubclass(Job.class)).build();
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobCode, TEMP_GROUP).build();
    scheduler.scheduleJob(tempJobDetail, trigger);
    scheduler.triggerJob(JobKey.jobKey(jobCode, TEMP_GROUP));
    scheduler.pauseTrigger(trigger.getKey());
    scheduler.unscheduleJob(trigger.getKey());
    scheduler.deleteJob(JobKey.jobKey(jobCode, TEMP_GROUP));
  }

  /**
   * 添加新的job
   * @param name
   * @throws SchedulerConfigException
   */
  private void loadJob(String name) throws SchedulerConfigException {
    JobConfigRecord record = jobConfigModel.findByNameOrException(name);
    try {
      JobDetail jobDetail = getJobDetail(record);
      CronTrigger cronTrigger = getCronTrigger(record);
      scheduler.scheduleJob(jobDetail, cronTrigger);
    } catch (Exception e) {
      log.error("加载定时任务异常",e);
      throw new SchedulerConfigException("加载定时任务异常", e);
    }
  }

  private void unloadJob(String name) throws SchedulerException {
    // 停止触发器
    scheduler.pauseTrigger(TriggerKey.triggerKey(name, DEFAULT_GROUP));
    // 卸载定时任务
    scheduler.unscheduleJob(TriggerKey.triggerKey(name, DEFAULT_GROUP));
    // 删除原来的job
    scheduler.deleteJob(JobKey.jobKey(name, DEFAULT_GROUP));
  }

  /**
   * 重新加载执行计划
   * @throws Exception
   */
  private void reload(String name) throws ClassNotFoundException, SchedulerException {
    JobConfigRecord record = jobConfigModel.findByNameOrException(name);
    String jobCode = record.getName();
    // 获取以前的触发器
    TriggerKey triggerKey = TriggerKey.triggerKey(jobCode, DEFAULT_GROUP);
    // 停止触发器
    scheduler.pauseTrigger(triggerKey);
    // 删除触发器
    scheduler.unscheduleJob(triggerKey);
    // 删除原来的job
    scheduler.deleteJob(JobKey.jobKey(jobCode));

    JobDetail jobDetail = getJobDetail(record);
    CronTrigger cronTrigger = getCronTrigger(record);
    // 重新加载job
    scheduler.scheduleJob(jobDetail, cronTrigger);
  }

  //组装JobDetail
  private JobDetail getJobDetail(JobConfigRecord jobConfigRecord) throws ClassNotFoundException {
    return JobBuilder.newJob()
        .withIdentity(JobKey.jobKey(jobConfigRecord.getName(), DEFAULT_GROUP))
        .withDescription(jobConfigRecord.getDescription())
        .usingJobData(new JobDataMap(JsonUtil.fromOrException(jobConfigRecord.getParam(), new TypeReference<Map<?, ?>>() {})))
        .ofType(Class.forName(jobConfigRecord.getClassName()).asSubclass(Job.class))
        .build();
  }

  //组装CronTrigger
  private CronTrigger getCronTrigger(JobConfigRecord jobConfigRecord){
    return TriggerBuilder.newTrigger()
        .withIdentity(TriggerKey.triggerKey(jobConfigRecord.getName(), DEFAULT_GROUP))
        .withSchedule(CronScheduleBuilder.cronSchedule(jobConfigRecord.getCron()))
        .build();
  }

}
