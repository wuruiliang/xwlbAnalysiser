package com.wrl.xwlb.controller.request;

import lombok.Data;

@Data
public class AddJobRequest {
  private String name;
  private String className;
  private String cron;
  private String param;
  private String desc;
}
