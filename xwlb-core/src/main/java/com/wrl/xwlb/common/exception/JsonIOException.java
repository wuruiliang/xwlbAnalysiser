package com.wrl.xwlb.common.exception;

public class JsonIOException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public JsonIOException(String msg) {
    super(msg);
  }

  public JsonIOException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public JsonIOException(Throwable cause) {
    super(cause);
  }
}
