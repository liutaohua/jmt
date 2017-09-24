package cc.tpark;

import cc.tpark.connections.NettyServer;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        NettyServer nettyServer = new NettyServer(1883);
        nettyServer.start();
        nettyServer.join();
    }
}
