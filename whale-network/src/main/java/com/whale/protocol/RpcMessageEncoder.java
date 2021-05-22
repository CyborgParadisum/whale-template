package com.whale.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

/**
 * rpc message encoder
 */
public class RpcMessageEncoder extends MessageToMessageEncoder<Message> {

  public static final RpcMessageEncoder INSTANCE = new RpcMessageEncoder();

  @Override
  protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
    // todo
  }
}
