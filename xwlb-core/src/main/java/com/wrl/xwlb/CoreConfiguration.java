package com.wrl.xwlb;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan(basePackages = {"com.wrl.xwlb"})
@EnableAsync
@EnableAspectJAutoProxy
public class CoreConfiguration {
}
