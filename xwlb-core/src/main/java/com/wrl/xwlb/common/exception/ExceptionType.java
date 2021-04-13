package com.wrl.xwlb.common.exception;

public enum ExceptionType {
  COMMON_SERVER_ERROR(1001, "系统异常，请稍后再试", ExceptionLevel.ERROR),
  COMMON_ILLEGAL_PARAM(1002, "参数异常", ExceptionLevel.ERROR),
  COMMON_RESPONSE_TIMEOUT(1003, "响应超时", ExceptionLevel.ERROR),
  COMMON_ILLEGAL_CONFIGURATION(1004, "参数错误", ExceptionLevel.ERROR),
  COMMON_NULL_POINT(1005, "空指针错误", ExceptionLevel.ERROR),
  COMMON_ILLEGAL_STATE(1006, "断言错误", ExceptionLevel.ERROR),
  COMMON_THIRD_PARTY_TIMEOUT(1008, "第三方服务超时", ExceptionLevel.ERROR),
  COMMON_CUSTOM_MESSAGE(1009, "网络异常，请稍后重试", ExceptionLevel.WARNING),

  ;

  private final int value;
  private final String text;
  private final ExceptionLevel level;

  ExceptionType(int value, String text, ExceptionLevel level) {
    this.value = value;
    this.text = text;
    this.level = level;
  }

  public int getValue() {
    return this.value;
  }

  public String getText() {
    return this.text;
  }

  public ExceptionLevel getLevel() {
    return this.level;
  }

  public static ExceptionType fromValue(int value) {
    for (ExceptionType type : ExceptionType.values()) {
      if (type.getValue() == value) {
        return type;
      }
    }
    return null;
  }
}
