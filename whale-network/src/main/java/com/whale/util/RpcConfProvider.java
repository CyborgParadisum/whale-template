package com.whale.util;

import java.util.NoSuchElementException;

public abstract class RpcConfProvider {

  protected RpcConfProvider() {
  }

  /**
   * @param name conf name
   * @return value of `name`
   */
  abstract String get(String name) throws NoSuchElementException;

  /**
   * @param name         conf name
   * @param defaultValue defaultValue
   * @return value of `name`
   */
  public String get(String name, String defaultValue) {
    try {
      return get(name);
    } catch (NoSuchElementException ignore) {
      return defaultValue;
    }
  }
}
