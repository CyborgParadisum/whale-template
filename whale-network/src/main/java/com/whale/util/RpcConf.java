package com.whale.util;

import java.util.NoSuchElementException;

/**
 * expose all rpc conf to users
 */
public class RpcConf {

  private RpcConfProvider conf;

  public RpcConf(RpcConfProvider conf) {
    this.conf = conf;
  }

  public String get(String name) throws NoSuchElementException {
    return conf.get(name);
  }

  public String get(String name, String defaultValue) {
    return conf.get(name, defaultValue);
  }
}
