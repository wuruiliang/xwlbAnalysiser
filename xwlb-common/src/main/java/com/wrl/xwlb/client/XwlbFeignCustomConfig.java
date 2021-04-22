package com.wrl.xwlb.client;

import feign.Client;

public class XwlbFeignCustomConfig {
  public boolean useHystrix;
  public Client delegateClient;

  public XwlbFeignCustomConfig() {
    this(false, null);
  }

  public XwlbFeignCustomConfig(boolean useHystrix, Client delegateClient) {
    this.useHystrix = useHystrix;
    this.delegateClient = delegateClient;
  }
}
