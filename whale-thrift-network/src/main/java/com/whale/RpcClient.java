package com.whale;

import com.whale.service.HelloService.Client;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClient {

  private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

  private int port;
  private String hostName;

  public RpcClient(int port, String hostName) throws TTransportException {
    this.port = port;
    this.hostName = hostName;
    init();
  }

  private TSocket transport;

  private void init() throws TTransportException {
    transport = new TSocket(hostName, port);
    logger.info("transport init");
    transport.open();
    logger.info("transport opened");
  }

  String sendMessage(String request) throws TException {
    Client client = new Client(new TBinaryProtocol(transport));
    logger.info("client send message: " + request);
    String responseMessage = client.hello(request);
    logger.info("responseMessage -- " + responseMessage);
    return responseMessage;
  }

  void close() {
    if (transport != null && transport.isOpen()) {
      transport.close();
    }
  }

}
