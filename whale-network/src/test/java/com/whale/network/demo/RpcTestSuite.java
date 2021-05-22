package com.whale.network.demo;

import com.whale.network.demo.time.example.TimeClient;
import com.whale.network.demo.time.example.TimeServer;
import org.junit.Before;
import org.junit.Test;

public class RpcTestSuite {
  //  private RpcContext context;
  //
  //  private RpcServer serverFirst;
  //  private RpcServer serverSecond;

  private String hostName;
  private int port;

  @Before
  public void setUp() {
    //    RpcConf rpcConf = new RpcConf(MapRpcConfProvider.EMPTY);
    //
    //    serverFirst = context.createRpcServer();
    //    serverSecond = context.createRpcServer();

    hostName = "192.168.56.1";
    port = 1234;

  }

//  @Test
//  public void testHello() throws InterruptedException {
//    RpcServer rpcServer = new RpcServer(hostName, port);
//    RpcClient rpcClient = new RpcClient();
//
//    rpcServer.startServer();
//    Channel channel = rpcClient.connect(hostName, port);
//    String res = (String) RpcClient.send(channel, "你好");
//    System.out.println(res);
//  }


  // time server

  @Test
  public void testTimeServer() throws InterruptedException {
    TimeServer s = new TimeServer(hostName, port);
    s.start();
  }

  // time client

  @Test
  public void testTimeClient() throws InterruptedException {
    TimeClient c = new TimeClient(hostName, port);
    c.start();
  }

}
