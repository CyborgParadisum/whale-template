package com.whale.network;

/**
 * 先实现 tcp 长连接
 */
abstract public class Client {

    String host;
    int port;

    abstract void init();
    abstract void connect();
    abstract void send();
    abstract void close();
}
