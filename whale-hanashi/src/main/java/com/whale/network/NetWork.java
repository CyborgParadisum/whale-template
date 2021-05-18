package com.whale.network;

abstract public class NetWork {


    abstract void init();
    abstract void connect();
    abstract void receive();
    abstract void send();
    abstract void close();

}
