package com.wrl.xwlb.client;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.common.exception.ExceptionType;
import com.wrl.xwlb.common.mvc.CommonResponse;
import com.wrl.xwlb.util.JsonUtil;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.util.Objects;

public class XwlbExceptionDecoder implements ErrorDecoder {
  @Override
  public Exception decode(String methodKey, Response response) {
    try {
      String body = Util.toString(response.body().asReader());
      return fromOrException(body);
    } catch (Exception e) {
      return FeignException.errorStatus(methodKey, response);
    }
  }

  private static CommonException fromOrException(String value) {
    CommonResponse response = JsonUtil.fromOrException(value, CommonResponse.class);
    return fromOrException(response);
  }

  private static CommonException fromOrException(CommonResponse response) {
    ExceptionType type = Objects.requireNonNull(ExceptionType.fromValue(response.status.code));
    return new CommonException(type, response.status.message);
  }

}
