package com.whale.util;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.StringUtils;

public class MapRpcConfProvider extends RpcConfProvider {

  public static final MapRpcConfProvider EMPTY = new MapRpcConfProvider(Collections.emptyMap());

  private Map<String, String> config;

  MapRpcConfProvider(Map<String, String> config) {
    this.config = config;
  }

  @Override
  String get(String name) {
    String value = config.get(name);
    if (StringUtils.isEmpty(value)) {
      throw new NoSuchElementException(name);
    }
    return value;
  }
}
