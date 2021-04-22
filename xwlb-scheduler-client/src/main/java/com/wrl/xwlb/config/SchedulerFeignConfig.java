package com.wrl.xwlb.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.wrl.xwlb.client.XwlbExceptionDecoder;
import com.wrl.xwlb.client.XwlbFeignCustomConfig;
import com.wrl.xwlb.client.XwlbHystrixExceptionDecoder;
import feign.Client;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;


public class SchedulerFeignConfig {

  private MappingJackson2HttpMessageConverter jsonConverter;

  private static final String SERVICE_NAME = "xwlb-spider";
  private static final SetterFactory SETTER_FACTORY = new SetterFactory.Default();
  private static final int CUSTOM_CONNECT_TIMEOUT_MILLIS = 10000;
  private static final int CUSTOM_READ_TIMEOUT_MILLIS = 300000;

  @Autowired(required = false)
  private XwlbFeignCustomConfig config = new XwlbFeignCustomConfig();

  @PostConstruct
  public void init() {
    jsonConverter = new MappingJackson2HttpMessageConverter();
    jsonConverter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    jsonConverter.getObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
    jsonConverter.getObjectMapper().registerModule(new JavaTimeModule());
    jsonConverter.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Bean
  @Scope("prototype")
  public Feign.Builder feignBuilder() {
    return config.useHystrix ?
        HystrixFeign.builder().setterFactory(SETTER_FACTORY) :
        Feign.builder();
  }

  @Bean
  public Decoder feignDecoder() {
    ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(false,
        Lists.newArrayList(jsonConverter));
    return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
  }

  @Bean
  public Encoder feignEncoder() {
    ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(false,
        Lists.newArrayList(jsonConverter));
    return new SpringEncoder(objectFactory);
  }

  @Bean
  public ErrorDecoder getErrorDecoder() {
    return config.useHystrix ? new XwlbHystrixExceptionDecoder() : new XwlbExceptionDecoder();
  }

  @Bean
  public Request.Options getOptions() {
    return new Request.Options(CUSTOM_CONNECT_TIMEOUT_MILLIS, CUSTOM_READ_TIMEOUT_MILLIS);
  }

  @Bean
  public Retryer getRetryer() {
    return Retryer.NEVER_RETRY;
  }

  @Bean
  public Client getRibbonFeignClient(CachingSpringLoadBalancerFactory cachingFactory, SpringClientFactory clientFactory) {
    return new LoadBalancerFeignClient(new Client.Default(null, null), cachingFactory, clientFactory);
  }

}
