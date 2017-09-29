package cc.tpark;

import cc.tpark.actor.manager.ConnectionManager;
import cc.tpark.api.ConnectionAPI;
import cc.tpark.netty.NettyServer;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        NettyServer nettyServer = new NettyServer(1883);
        nettyServer.start();


        new Thread(new Runnable() {
            private final ConnectionAPI manager = ApplicationContext.instence.getConnectionAPI();

            @Override
            public void run() {
                while (true) {
                    System.out.println("current conn number :" + manager.getConnNum());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        nettyServer.join();


    }
}
