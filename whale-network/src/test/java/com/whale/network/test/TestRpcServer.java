package com.whale.network.test;

import com.whale.RpcContext;
import com.whale.client.RpcClient;
import com.whale.server.NoOpRpcHandler;
import com.whale.server.RpcHandler;
import com.whale.server.RpcServer;
import com.whale.util.MapRpcConfProvider;
import com.whale.util.RpcConf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestRpcServer {

  String hostName = "192.168.56.1";
  int port = 22222;

  RpcServer server;
  RpcContext context;

  RpcHandler rpcHandler;

  @Before
  public void init() {
    MapRpcConfProvider p = new MapRpcConfProvider(new HashMap<>());
    context = new RpcContext(new RpcConf(p), new NoOpRpcHandler());

    rpcHandler = new RpcHandler() {
      @Override
      public void receive(RpcClient client, ByteBuffer message) {
        String msg = Unpooled.wrappedBuffer(message).toString(StandardCharsets.UTF_8);

      }
    };
  }

  @Test
  public void testRpcServer() throws InterruptedException {
    server = context.createRpcServer(hostName, port);
//    server.syncCloseFuture(); /* this will sync close future */
  }

//  @After
//  public void destroy() throws IOException {
//    Optional<RpcServer> s = Optional.ofNullable(this.server);
//    if (s.isPresent()){
//      s.get().close();
//    }
//  }

}
