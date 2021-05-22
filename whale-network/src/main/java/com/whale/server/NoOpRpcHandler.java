package com.whale.server;

import com.whale.client.RpcClient;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoOpRpcHandler extends RpcHandler {
  private static final Logger logger = LoggerFactory.getLogger(NoOpRpcHandler.class);

  @Override
  public void receive(RpcClient client, ByteBuffer message) {
    logger.error("no op can not handler message");
    //    throw new UnsupportedOperationException("Cannot handle messages");
  }
}
