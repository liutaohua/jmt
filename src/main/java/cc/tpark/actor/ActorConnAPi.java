package cc.tpark.actor;

import akka.actor.ActorRef;
import cc.tpark.ApplicationContext;
import cc.tpark.actor.manager.ConnectionManager;
import cc.tpark.api.ConnectionAPI;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static akka.pattern.PatternsCS.ask;

public class ActorConnAPi implements ConnectionAPI {
    private final ActorRef connManager = ApplicationContext.instence.getConnectionManager();

    @Override
    public boolean createConn(String id, ChannelHandlerContext ctx) {
        if (isAlive(id)) {
            return true;
        }
        CompletableFuture<Object> feture = ask(connManager, ConnectionManager.AddConnection.getInstence(id, ctx), 3000)
                .toCompletableFuture();
        boolean isOk;
        try {
            isOk = (boolean) feture.get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
        return isOk;
    }

    @Override
    public int getConnNum() {
        return 0;
    }

    @Override
    public void sendMesssage(String id, MqttConnAckMessage mqttConnAckMessage) {
        CompletableFuture<Object> feture = ask(connManager, ConnectionManager.SendMessage.getInstence(id, mqttConnAckMessage), 3000)
                .toCompletableFuture();
    }

    @Override
    public boolean isAlive(String id) {
        CompletableFuture<Object> feture = ask(connManager, ConnectionManager.IsAlive.getInstence(id), 1000)
                .toCompletableFuture();
        boolean isAlive;
        try {
            isAlive = (boolean) feture.get();
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
        return isAlive;
    }

    @Override
    public void delConn(String id) {

    }

    @Override
    public boolean checkServerStatus() {
        return true;
    }
}
