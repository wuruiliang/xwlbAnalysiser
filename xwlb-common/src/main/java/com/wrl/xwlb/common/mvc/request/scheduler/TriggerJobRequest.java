package com.wrl.xwlb.common.mvc.request.scheduler;

import lombok.Data;

@Data
public class TriggerJobRequest {
  private String name;
  private String param;
}
