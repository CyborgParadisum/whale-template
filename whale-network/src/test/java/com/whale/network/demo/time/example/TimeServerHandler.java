package com.whale.network.demo.time.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
  @Override
  // 该方法将会在 connection 建立, 以及准备流量 的时候被调用
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channel active: " + ctx.channel());

    ByteBufAllocator alloc = ctx.alloc();
    final ByteBuf time = alloc.buffer(8);
    time.writeLong(System.currentTimeMillis());

    final ChannelFuture f = ctx.writeAndFlush(time);
    f.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        assert f == future;
        ctx.close();
      }
    });
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
