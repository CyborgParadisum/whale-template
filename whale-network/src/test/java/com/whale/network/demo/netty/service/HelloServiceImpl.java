package com.whale.network.demo.netty.service;

public class HelloServiceImpl implements HelloService{
  @Override
  public String hello(String msg) {
    return msg != null ? msg + " -----> I am fine." : "I am fine.";
  }
}
