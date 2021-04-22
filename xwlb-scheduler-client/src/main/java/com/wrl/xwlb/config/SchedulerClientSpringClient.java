package com.wrl.xwlb.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients("com.wrl.xwlb.client")
@Configuration
public class SchedulerClientSpringClient {
}
