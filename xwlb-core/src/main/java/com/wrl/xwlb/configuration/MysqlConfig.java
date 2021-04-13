package com.wrl.xwlb.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class MysqlConfig {

  private String url = "jdbc:mysql://localhost:3306/xwlb";

  private String userName = "root";

  private String password = "";

  private int initialSize = 10;

  private int minIdle = 2;

  private int maxActive = 10;

  private int maxWait = 10;
}
