package com.whale.server;

import com.whale.service.HelloService.Iface;
import com.whale.service.HelloService.Processor;
import com.whale.service.HelloServiceImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServer {
  private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

  private String hostName;
  private int port;

  public RpcServer(String hostName, int port) {
    this.hostName = hostName;
    this.port = port;
  }

  private TSimpleServer server;

  void startServer() throws TTransportException {
    logger.info("start server:{}", this);
    Processor<Iface> processor = new Processor<>(new HelloServiceImpl());

    TServerSocket serverTransport = new TServerSocket(port);
    TServer.Args tArgs = new TServer.Args(serverTransport);
    tArgs.processor(processor);
    tArgs.protocolFactory(new TBinaryProtocol.Factory());

    server = new TSimpleServer(tArgs);
    server.serve();
  }

  void close() {
    if (server != null) {
      server.stop();
    }
  }

  @Override
  public String toString() {
    return "RpcServer{" +
        "hostName='" + hostName + '\'' +
        ", port=" + port +
        '}';
  }
}
