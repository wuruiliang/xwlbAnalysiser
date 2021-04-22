package com.wrl.xwlb.common.mvc;

import com.wrl.xwlb.common.exception.ExceptionType;

public class ResponseStatus {
  public int code;
  public String message;
  public Long responseTime;

  public ResponseStatus() {}

  public ResponseStatus(int code, String detail) {
    this.code = code;
    this.message = detail;
  }

  public ResponseStatus(ExceptionType code) {
    this(code.getValue(), code.getText());
  }
}
