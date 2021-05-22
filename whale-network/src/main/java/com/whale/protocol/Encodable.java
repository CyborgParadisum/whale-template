package com.whale.protocol;

import io.netty.buffer.ByteBuf;

public interface Encodable {

  int encodedLength();
  void encode(ByteBuf buf);
}
