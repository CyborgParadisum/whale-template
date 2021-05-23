package com.whale.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

/**
 * Rpc message decoder
 */
public class RpcMessageDecoder extends MessageToMessageDecoder<Message> {

  public static final RpcMessageDecoder INSTANCE = new RpcMessageDecoder();


  @Override
  protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
    // todo
    out.add(msg);
  }
}
