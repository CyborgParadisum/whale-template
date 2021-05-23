package com.whale.protocol;

public interface MessageHandler<T extends Message> {

  void handle(T message);
}
