package network.socket;

import lombok.SneakyThrows;
import lombok.val;
import network.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.util.Map;

public class SocketServer extends Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private Socket socketClient;

    Map<String, String> config;

    @SneakyThrows
    public SocketServer(Map<String, String> config) {
        this.config = config;
        init();
    }

    @Override
    public void init() throws IOException {
        int port = Integer.parseInt(config.get("port"));
        System.out.println(port);
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    public Socket accept() {
        socket = serverSocket.accept();
        return socket;
    }

    @Override
    public InputStream receive() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public void send(Object object) throws IOException {
        val output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(object);
        output.flush();
    }

    @Override
    public void close() {
    }
}
