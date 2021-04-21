package com.wrl.xwlb.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "xwlb.db")
public class MysqlProperties {

  private String url;

  private String user;

  private String password;

  private String name;

  private int initialSize = 20;

  private int minIdle = 20;

  private int maxActive = 100;

  private int maxWait = 10000;

  public String getFullUrl() {
    return this.url + "/" + this.name;
  }
}
