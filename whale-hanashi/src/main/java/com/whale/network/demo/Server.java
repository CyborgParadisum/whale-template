package com.whale.network.demo;

import java.io.IOException;
import java.io.InputStream;

abstract public class Server {
    abstract public  void init() throws IOException;
    abstract public  InputStream receive() throws IOException;
    abstract public  void send(Object object) throws IOException ;
    abstract public  void close();

}
