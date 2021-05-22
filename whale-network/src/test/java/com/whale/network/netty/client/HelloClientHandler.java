package com.whale.network.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HelloClientHandler extends ChannelInboundHandlerAdapter {

  private ChannelHandlerContext context;
  private String result;

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    context = ctx;
  }

  /**
   * 收到服务端数据，唤醒等待线程
   */
  @Override
  public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
    result = msg.toString();
    System.out.println("result -- " + result);
  }

  public String getResult() {
    return result;
  }
}
