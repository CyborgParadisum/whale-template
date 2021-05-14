package com.whale.server;

import io.netty.channel.Channel;

/**
 * A bootstrap which is executed on a TransportServer's client channel once a client connects
 * to the server. This allows customizing the client channel to allow for things such as SASL
 * authentication.
 */
public interface RpcServerBootstrap {

  /**
   * customizes the channel to include new features, if need
   *
   * @param channel    the connected channel opened by the client
   * @param rpcHandler the RPC handler for the server
   * @return the rpc handler to use for the channel
   */
  RpcHandler doBootstrap(Channel channel, RpcHandler rpcHandler);
}
