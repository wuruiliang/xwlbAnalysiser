package com.wrl.xwlb.enums;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionType;

public enum JobStatus {
  USING("U", "启用"),
  STOPPED("S", "停用");

  public String code;
  public String desc;

  JobStatus(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static JobStatus fromCode(String code) {
    for (JobStatus status : JobStatus.values()) {
      if (status.code.equals(code)) {
        return status;
      }
    }
    throw new CommonException(ExceptionType.COMMON_SERVER_ERROR, "未知的code = " + code);
  }
}
