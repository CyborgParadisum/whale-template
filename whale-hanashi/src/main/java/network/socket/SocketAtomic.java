package network.socket;

import com.sun.tools.javac.util.Pair;
import lombok.SneakyThrows;
import lombok.val;
import network.Action;
import network.Atomic;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class SocketAtomic extends Atomic {

    Map<String, String> config;
    int port;

    private ServerSocket serverSocket;
    private Socket socket;
    List<Pair<Event, Action>> eventList;
    Map<Event, Action> eventMap;

    @SneakyThrows
    public SocketAtomic(Map<String, String> config) {
        this.config = config;
        this.port = Integer.parseInt(this.config.get("port"));
    }

    @SneakyThrows
    public void init() {
//        connect();
    }

    @SneakyThrows
    public void startServer() {
        serverSocket = new ServerSocket(port);
    }

    /**
     * as client connect to server
     */
    @SneakyThrows
    public void connect() {

    }

    @SneakyThrows
    public Socket accept() {
        return serverSocket.accept();
    }

    public void onConnect(Action action) {

    }

    //    public static class Event {
    public enum Event {
        /**
         *
         */
        INIT,
        CONNECT,
        RECEIVE,
        CLOSE
    }

    public SocketAtomic on(Event event, Action action) {
        eventList.add(new Pair<>(event, action));
        return this;
    }
}
