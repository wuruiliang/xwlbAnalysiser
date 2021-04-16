package com.wrl.xwlb.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class MysqlConfig {

  @Value("${xwlb.db.url}")
  private String url;

  @Value("${xwlb.db.user}")
  private String userName;

  @Value("${xwlb.db.password}")
  private String password;

  @Value("${xwlb.db.name}")
  private String db;

  private int initialSize = 10;

  private int minIdle = 2;

  private int maxActive = 10;

  private int maxWait = 10;

  public String getFullUrl() {
    return this.url + "/" + this.db;
  }
}
