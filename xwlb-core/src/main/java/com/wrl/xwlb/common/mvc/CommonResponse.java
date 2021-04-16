package com.wrl.xwlb.common.mvc;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionLevel;
import com.wrl.xwlb.common.exception.ExceptionType;
import com.wrl.xwlb.util.ClockUtil;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommonResponse<T> {
  public ResponseStatus status;
  public T body;

  public CommonResponse(T t) {
    status = new ResponseStatus(0, "");
    status.responseTime = ClockUtil.now();
    body = t;
  }

  public static <T> CommonResponse<T> of(T body) {
    return new CommonResponse<>(body);
  }

  public CommonResponse(CommonException e) {
    if (e.getErrorCode().getLevel() == ExceptionLevel.ERROR) {
      status = new ResponseStatus(ExceptionType.COMMON_SERVER_ERROR);
    } else {
      if (e.getErrorMessage() != null) {
        status = new ResponseStatus(e.getErrorCode().getValue(), e.getErrorMessage());
      } else if (e.getCause() != null) {
        status = new ResponseStatus(e.getErrorCode().getValue(), e.getCause().getMessage());
      } else {
          status = new ResponseStatus(e.getErrorCode());
      }
    }
    status.responseTime = ClockUtil.now();
  }
}
