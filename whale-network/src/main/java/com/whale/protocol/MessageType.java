package com.whale.protocol;

import io.netty.buffer.ByteBuf;

public enum MessageType implements Encodable {
  OneWayMessage(0);

  private final byte id;

  MessageType(int id) {
    assert id < 128 : "Cannot have more than 128 message types";
    this.id = (byte) id;
  }

  public byte id() {

    return id;
  }

  @Override
  public int encodedLength() {
    return 1;
  }
  @Override
  public void encode(ByteBuf buf) {
    buf.writeByte(id);
  }


  /**
   * 目前只有message需要解码
   */
  public static MessageType decode(ByteBuf buf) {
    byte id = buf.readByte();
    switch (id) {
      case 0: return OneWayMessage;
      case -1: throw new IllegalArgumentException("User type messages cannot be decoded.");
      default: throw new IllegalArgumentException("Unknown message type: " + id);
    }
  }
}
