package com.wrl.xwlb.service.vo;

import com.wrl.xwlb.common.mvc.request.scheduler.AddJobRequest;
import lombok.Data;

@Data
public class JobVO {
  private String name;
  private String className;
  private String cron;
  private String param;
  private String desc;

  public static JobVO fromRequest(AddJobRequest request) {
    JobVO jobVO = new JobVO();
    jobVO.setName(request.getName());
    jobVO.setClassName(request.getClassName());
    jobVO.setCron(request.getCron());
    jobVO.setParam(request.getParam());
    jobVO.setDesc(request.getDesc());
    return jobVO;
  }
}
