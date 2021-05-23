package com.whale;

import com.whale.client.RpcClient;
import com.whale.protocol.RpcMessageDecoder;
import com.whale.protocol.RpcMessageEncoder;
import com.whale.server.RpcChannelHandler;
import com.whale.server.RpcHandler;
import com.whale.server.RpcServer;
import com.whale.util.RpcConf;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

/**
 * Rpc context. for create rpc clientFactory、rpc server
 */
public class RpcContext {

  private RpcConf conf;
  private RpcHandler rpcHandler;

  private static final RpcMessageEncoder encoder = RpcMessageEncoder.INSTANCE;
  private static final RpcMessageDecoder decoder = RpcMessageDecoder.INSTANCE;

  public RpcContext(RpcConf conf, RpcHandler rpcHandler) {
    this.conf = conf;
    this.rpcHandler = rpcHandler;
  }

  public RpcServer createRpcServer(String hostName, int port) {
    return new RpcServer(this, hostName, port, rpcHandler);
  }

  public RpcClient createRpcClient() {
    // todo
    //    ServerBootstrap
    return null;
  }

  public RpcConf getConf() {
    return conf;
  }

  // ---------------------------- utilities ---------------------------------------

  /**
   * 初始化 channel管道， 为后期添加 handler 做准备 如：编码，解码，校验。fetch chunk等
   */
  public RpcChannelHandler initializePipeline(
      SocketChannel channel,
      RpcHandler rpcHandler) {
    RpcChannelHandler channelHandler = createChannelHandler(channel, rpcHandler);
    channel.pipeline()
        //        .addLast("encoder", encoder)
        //        .addLast("decoder", decoder)
        .addLast("handler", channelHandler);

    return channelHandler;
  }
  public RpcChannelHandler initializePipeline(SocketChannel channel) {
    return initializePipeline(channel, rpcHandler);
  }


  private RpcChannelHandler createChannelHandler(Channel channel, RpcHandler rpcHandler) {
    // todo 等待细节的实现
    return new RpcChannelHandler(channel, rpcHandler);
  }
}
