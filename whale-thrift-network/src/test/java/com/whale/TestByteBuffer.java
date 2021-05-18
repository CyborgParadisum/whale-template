package com.whale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TestByteBuffer {
  public static void main(String[] args) throws JsonProcessingException {
    ArrayList<String> list = new ArrayList<>();
    list.add("a");
    list.add("b");
    list.add("c");

    ByteBuffer buffer = ByteBuffer.allocate(32);

    ObjectMapper mapper = getMapper();
    String s = mapper.writeValueAsString(list);
    System.out.println(s);
  }

  private static ObjectMapper getMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return mapper;
  }
}
