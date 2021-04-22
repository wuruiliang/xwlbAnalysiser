package com.wrl.xwlb.configuration;

import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

public class EurekaServiceGracefulShutdownListener {
  private static final Logger log = LoggerFactory.getLogger(EurekaServiceGracefulShutdownListener.class);

  @Qualifier("eurekaClient")
  @Autowired
  private EurekaClient eurekaClient;
  private ApplicationContext context;
  private int waitSeconds;
  private int waitSecondsBeforeUnregister;

  public EurekaServiceGracefulShutdownListener(ApplicationContext context) {
    this(context, 60);
  }

  public EurekaServiceGracefulShutdownListener(ApplicationContext context, int waitSeconds) {
    this(context, waitSeconds, 6);
  }

  public EurekaServiceGracefulShutdownListener(ApplicationContext context, int waitSeconds, int waitSecondsBeforeUnregister) {
    this.context = context;
    this.waitSeconds = waitSeconds;
    this.waitSecondsBeforeUnregister = waitSecondsBeforeUnregister;
  }

  @EventListener
  public void onContextClosed(ContextClosedEvent event) {
    if (event.getApplicationContext().equals(this.context)) {
      this.waitBeforeUnregister();
      this.eurekaClient.shutdown();
      this.waitBeforeClosing();
    }

  }

  private void waitBeforeUnregister() {
    log.info("Wait for " + this.waitSecondsBeforeUnregister + "s before unregister.");
    this.sleep((long)this.waitSecondsBeforeUnregister);
  }

  private void waitBeforeClosing() {
    log.info("Wait for " + this.waitSeconds + "s before closing.");
    this.sleep((long)this.waitSeconds);
  }

  private void sleep(long seconds) {
    try {
      Thread.sleep(seconds * 1000L);
    } catch (InterruptedException var4) {
      log.error("Error while sleeping.", var4);
      Thread.currentThread().interrupt();
    }

  }
}
