package com.wrl.xwlb.common.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class CommonException extends RuntimeException {
  private static final long serialVersionUID = 1085169334843179185L;
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonException.class);
  private String message;
  private ExceptionType errorCode;

  public CommonException(ExceptionType errorCode) {
    super(errorCode.getText());
    this.errorCode = errorCode;
  }

  public CommonException(ExceptionType errorCode, String message) {
    super(message);
    initBubbleException(errorCode, message);
  }

  public CommonException(ExceptionType errorCode, Throwable cause) {
    super(cause);
    this.errorCode = errorCode;
  }

  public CommonException(ExceptionType errorCode, String message, Throwable cause) {
    super(message, cause);
    initBubbleException(errorCode, message);
  }

  public CommonException(String message, ExceptionType errorCode, Object... args) {
    super(message);
    try {
      this.errorCode = errorCode;
      if (StringUtils.isNoneBlank(errorCode.getText())) {
        this.message = String.format(errorCode.getText(), args);
      }
    } catch (Exception e) {
      LOGGER.error("format text err, " + errorCode + ", args: " + Arrays.toString(args), e);
      this.errorCode = ExceptionType.COMMON_SERVER_ERROR;
    }
  }

  private void initBubbleException(ExceptionType errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public static CommonException wrap(Throwable e) {
    return wrap(e, ExceptionType.COMMON_SERVER_ERROR);
  }

  public static CommonException wrap(Throwable e, ExceptionType errorCode) {
    if (e instanceof CommonException) {
      return (CommonException) e;
    } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
      return new CommonException(ExceptionType.COMMON_THIRD_PARTY_TIMEOUT, e);
    } else {
      return new CommonException(errorCode, e);
    }
  }

  public ExceptionType getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return message;
  }

}
