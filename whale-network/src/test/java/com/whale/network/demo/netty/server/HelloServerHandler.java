package com.whale.network.demo.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    System.out.println("receive" + msg);

    ctx.writeAndFlush(" -----> I am fine.");

    //    if (msg.toString().startsWith(RpcClient.providerName)) {
    //      String result = new HelloServiceImpl()
    //          .hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
    //      ctx.writeAndFlush(result);
    //    }
  }
}
