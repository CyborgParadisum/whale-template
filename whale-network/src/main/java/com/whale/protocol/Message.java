package com.whale.protocol;

import io.netty.buffer.ByteBuf;

public interface Message extends Encodable {

  /**
   * 消息的类型，用以scala实现,为字段类型
   */
  MessageType type();

  /**
   * 消息体
   */
  ByteBuf body();

}
