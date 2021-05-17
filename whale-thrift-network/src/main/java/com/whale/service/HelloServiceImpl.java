package com.whale.service;

import org.apache.thrift.TException;

public class HelloServiceImpl implements HelloService.Iface{
  @Override
  public String hello(String request) throws TException {
    return request;
  }
}
