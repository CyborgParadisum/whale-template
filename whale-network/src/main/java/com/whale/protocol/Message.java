package com.whale.protocol;

import io.netty.buffer.ByteBuf;

public class Message implements Encodable {

  @Override
  public int encodedLength() {
    return 0;
  }
  @Override
  public void encode(ByteBuf buf) {

  }
}
