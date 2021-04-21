package com.wrl.xwlb.model;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionType;
import com.wrl.xwlb.enums.JobStatus;
import com.wrl.xwlb.model.core.BaseModel;
import com.wrl.xwlb.model.generated.Tables;
import com.wrl.xwlb.model.generated.tables.JobConfig;
import com.wrl.xwlb.model.generated.tables.records.JobConfigRecord;
import com.wrl.xwlb.util.ClockUtil;
import org.mockito.internal.verification.InOrderWrapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobConfigModel extends BaseModel {

  private final JobConfig table = Tables.JOB_CONFIG;

  public JobConfigRecord init(String name, String className, String cron, String param, String desc) {
    JobConfigRecord exist = findByName(name);
    if (exist != null) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "该名称的job已存在。name=" + name);
    }
    Long now = ClockUtil.now();
    exist = create().newRecord(table);
    exist.setName(name);
    exist.setClassName(className);
    exist.setCron(cron);
    exist.setParam(param);
    exist.setDescription(desc);
    exist.setStatus(JobStatus.USING.code);
    exist.setTimeCreated(now);
    exist.setTimeUpdated(now);
    exist.insert();
    return exist;
  }

  public JobConfigRecord findByName(String name) {
    return create().selectFrom(table).where(table.NAME.eq(name)).fetchOne();
  }

  public JobConfigRecord findByNameOrException(String name) {
    JobConfigRecord record = findByName(name);
    if (record == null) {
      throw new CommonException(ExceptionType.COMMON_CUSTOM_MESSAGE, "该job不存在。name=" + name);
    }
    return record;
  }

  public List<JobConfigRecord> findByStatus(JobStatus status) {
    return create().selectFrom(table).where(table.STATUS.eq(status.code)).fetch();
  }

  public JobConfigRecord findByNameAndStatus(String name, JobStatus status) {
    return create().selectFrom(table).where(table.NAME.eq(name)).and(table.STATUS.eq(status.code)).fetchOne();
  }

  public JobConfigRecord updateStatus(JobConfigRecord record, JobStatus status) {
    record.setStatus(status.code);
    record.setTimeUpdated(ClockUtil.now());
    record.update();
    return record;
  }
}
