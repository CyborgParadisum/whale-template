package com.whale.client;

import com.whale.protocol.MessageHandler;
import com.whale.protocol.ResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcResponseHandler implements MessageHandler<ResponseMessage> {

  private static final Logger logger = LoggerFactory.getLogger(RpcResponseHandler.class);
  private final Channel channel;

  RpcResponseHandler(Channel channel) {
    this.channel = channel;
  }

  @Override
  public void handle(ResponseMessage message) {

  }
}
