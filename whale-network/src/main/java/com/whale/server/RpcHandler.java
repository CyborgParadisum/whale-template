package com.whale.server;

import com.whale.client.RpcClient;
import java.nio.ByteBuffer;

public abstract class RpcHandler {
  public abstract void receive(
      RpcClient client,
      ByteBuffer message
  );
}
