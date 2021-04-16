package com.whale.network;

import com.whale.RpcContext;
import com.whale.server.RpcServer;
import com.whale.util.MapRpcConfProvider;
import com.whale.util.RpcConf;
import org.junit.Before;

public class RpcTestSuite {
  private RpcContext context;

  private RpcServer serverFirst;
  private RpcServer serverSecond;

  @Before
  public void setUp() {
    RpcConf rpcConf = new RpcConf(MapRpcConfProvider.EMPTY);

    serverFirst = context.createRpcServer();
    serverSecond = context.createRpcServer();
  }
}
