package com.wrl.xwlb.common.mvc;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionLevel;
import com.wrl.xwlb.common.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public CommonResponse methodArgumentNotValidExceptionHandler(
      HttpServletRequest request, MethodArgumentNotValidException exception) {
    String parameterError = exception.getBindingResult().getFieldError().getDefaultMessage();
    CommonException commonException =
        new CommonException(ExceptionType.COMMON_ILLEGAL_PARAM, parameterError);
    logException(commonException, getExceptionLogMsg(request, commonException));
    return new CommonResponse(commonException);
  }

  @ExceptionHandler(value = CommonException.class)
  public CommonResponse commonExceptionHandler(
      HttpServletRequest request, CommonException commonException) {
    logException(commonException, getExceptionLogMsg(request, commonException));
    return new CommonResponse(commonException);
  }

  @ExceptionHandler(value = Exception.class)
  public CommonResponse defaultExceptionHandler(HttpServletRequest request, Exception exception) {
    CommonException commonException =
        CommonException.wrap(exception.getCause() == null ? exception : exception.getCause());
    logException(commonException, getExceptionLogMsg(request, commonException));
    return new CommonResponse(commonException);
  }

  public void logException(CommonException e, String msg) {
    if (e.getErrorCode().getLevel() == ExceptionLevel.ERROR) {
      log.error(msg, e);
    } else if (e.getErrorCode().getLevel() == ExceptionLevel.WARNING) {
      log.warn(msg, e);
    } else {
      log.error(msg, e);
    }
  }

  public String getExceptionLogMsg(HttpServletRequest request, CommonException e) {
    return String.format(
        "[ExceptionType] %s [Caught exception] %s %s {%s %s %s}",
        e.getErrorCode().getValue(),
        request.getMethod(),
        request.getRequestURI(),
        e.getErrorCode().getValue(),
        e.getErrorCode().name(),
        e.getMessage());
  }
}
