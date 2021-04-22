package com.wrl.xwlb.common.mvc.request.scheduler;

import lombok.Data;

@Data
public class AddJobRequest {
  private String name;
  private String className;
  private String cron;
  private String param;
  private String desc;
}
