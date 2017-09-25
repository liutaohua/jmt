package cc.tpark;

import cc.tpark.connections.NettyServer;
import cc.tpark.connections.initializer.JMSServerInitializerFactory;
import cc.tpark.connections.initializer.mqtt.MqttServerInitializer;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        JMSServerInitializerFactory.addServerInitializer("mqtt", new MqttServerInitializer());
        NettyServer nettyServer = new NettyServer(1883, "mqtt");
        nettyServer.start();
        nettyServer.join();
    }
}
