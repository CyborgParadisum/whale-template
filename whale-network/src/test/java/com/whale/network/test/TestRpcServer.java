package com.whale.network.test;

import com.whale.RpcContext;
import com.whale.server.NoOpRpcHandler;
import com.whale.server.RpcServer;
import com.whale.util.MapRpcConfProvider;
import com.whale.util.RpcConf;
import java.io.IOException;
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

  @Before
  public void init() {
    MapRpcConfProvider p = new MapRpcConfProvider(new HashMap<>());
    context = new RpcContext(new RpcConf(p), new NoOpRpcHandler());
  }

  @Test
  public void testRpcServer() {
    server = context.createRpcServer(hostName, port);
  }

//  @After
//  public void destroy() throws IOException {
//    Optional<RpcServer> s = Optional.ofNullable(this.server);
//    if (s.isPresent()){
//      s.get().close();
//    }
//  }

}
