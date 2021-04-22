package com.wrl.xwlb.configuration;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public class EurekaServiceReadyListener {

  @Qualifier("eurekaClient")
  @Autowired
  private EurekaClient eurekaClient;

  public EurekaServiceReadyListener() {
  }

  @EventListener
  public void onApplicationReady(ApplicationReadyEvent event) {
    this.eurekaClient.getApplicationInfoManager().setInstanceStatus(InstanceInfo.InstanceStatus.UP);
  }
}
