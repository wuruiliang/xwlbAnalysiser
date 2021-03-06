package com.wrl.xwlb.configuration;

import com.wrl.xwlb.common.exception.CommonException;
import com.wrl.xwlb.model.core.ThreadTransactionalModel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CommonTransactionalAspect {

  private ThreadTransactionalModel threadTransactionalModel;

  CommonTransactionalAspect(@Autowired ThreadTransactionalModel threadTransactionalModel) {
    this.threadTransactionalModel =threadTransactionalModel;
  }

  @Pointcut(value = "@annotation(com.wrl.xwlb.configuration.CommonTransactional)")
  public void annotationPointCut(){}

  @Around(value = "annotationPointCut()")
  public Object around(ProceedingJoinPoint joinPoint) {
    return threadTransactionalModel.transactionResult(configuration -> {
      try {
        return joinPoint.proceed(joinPoint.getArgs());
      } catch (Throwable throwable) {
        throw CommonException.wrap(throwable);
      }
    });
  }
}
