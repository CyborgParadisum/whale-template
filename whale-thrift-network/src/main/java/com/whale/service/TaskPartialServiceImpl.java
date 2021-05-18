package com.whale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whale.partial.TaskPartial;
import com.whale.partial.TaskPartialService;
import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.TException;

public class TaskPartialServiceImpl implements TaskPartialService.Iface {

  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override
  public String executeTask(TaskPartial executedTask) throws TException {
    try {
      ArrayList<String> list = mapper.readValue(executedTask.getResources(), ArrayList.class);
      return null;
    } catch (JsonProcessingException e) {
      return null;
    }
  }
}
