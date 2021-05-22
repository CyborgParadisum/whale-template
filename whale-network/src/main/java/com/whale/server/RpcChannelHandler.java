package com.whale.server;

import com.whale.protocol.Message;
import com.whale.util.NettyUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for sendRPC()
 */
public class RpcChannelHandler extends SimpleChannelInboundHandler<Message> {

  private static final Logger logger = LoggerFactory.getLogger(RpcChannelHandler.class);

  private Channel channel;
  private RpcHandler rpcHandler;

  public RpcChannelHandler(Channel channel, RpcHandler rpcHandler) {
    this.channel = channel;
    this.rpcHandler = rpcHandler;
  }

  @Override
  // 请不要直接 override channelRead
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    InetSocketAddress add = (InetSocketAddress) ctx.channel().localAddress();
    logger.info("channel connected " + add.toString());
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    InetSocketAddress add = (InetSocketAddress) ctx.channel().localAddress();
    logger.warn("channel disconnected " + add);
    super.channelInactive(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    logger.error("Exception in connection from " +
        NettyUtils.getRemoteAddress(ctx.channel()), cause);
    ctx.close();
  }
  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    // 当 channel handler context 注册到对应的 eventLoop 回调该函数
    super.channelRegistered(ctx);
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    super.channelUnregistered(ctx);
  }
}
