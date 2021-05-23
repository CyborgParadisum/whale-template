package com.whale.util;

import java.time.Duration;
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

  // arbitrary fuc here
  public int getNumsClientThread() {
    return 1;
  }

  public boolean preferDirectBufs() {
    return true;
  }

  public boolean sharedByteBufAllocators() {
    return true;
  }

  public boolean preferDirectBufForSharedByteBufAllocators() {
    return true;
  }

  public int serverThreads() {
    return 0;
  }

  /**
   * @param duration seconds of times
   */
  public int getClientConnectionTimeout(int duration) {
    return getSecondsDuration(duration);
  }
  public int getClientConnectionTimeout() {
    return getClientConnectionTimeout(120);
  }


  private static int getSecondsDuration(int duration) {
    Duration s = Duration.ofSeconds(duration);
    return (int) s.toMillis();
  }

}
