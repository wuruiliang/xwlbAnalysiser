package com.wrl.xwlb.client;

import com.wrl.xwlb.config.SchedulerFeignConfig;
import org.springframework.cloud.netflix.feign.FeignClient;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@FeignClient(name = "xwlb-scheduler", configuration = SchedulerFeignConfig.class)
public @interface SchedulerFeignClient {
}
