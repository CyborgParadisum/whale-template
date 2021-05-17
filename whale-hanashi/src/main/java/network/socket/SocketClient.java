package network.socket;

import lombok.SneakyThrows;
import lombok.val;
import network.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class SocketClient extends Server {

    private ServerSocket serverSocket;
    private Socket socket;

    Map<String, String> config;

    @SneakyThrows
    public SocketClient(Map<String, String> config) {
        this.config = config;
        init();
    }

    @Override
    public void init() throws IOException {
        int port = Integer.parseInt(config.get("port"));
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    public Socket accept() {
        return serverSocket.accept();
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
