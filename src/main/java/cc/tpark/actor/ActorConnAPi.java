package cc.tpark.actor;

import akka.actor.ActorRef;
import cc.tpark.ApplicationContext;
import cc.tpark.actor.manager.ConnectionManager;
import cc.tpark.api.ConnectionAPI;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static akka.pattern.PatternsCS.ask;

public class ActorConnAPi implements ConnectionAPI {
    private final ActorRef connManager;
    private static final int DEFAULT_TIMEOUT = 3000;

    public ActorConnAPi(ActorRef connManager) {
        this.connManager = connManager;
    }

    @Override
    public boolean createConn(String id, ChannelHandlerContext ctx) {
        if (isAlive(id)) {
            return true;
        }
        CompletableFuture<Object> feture = ask(connManager, ConnectionManager.AddConnection.getInstence(id, ctx), DEFAULT_TIMEOUT)
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
        CompletableFuture<Object> feture = ask(connManager, ConnectionManager.GetConnNum.getInstence(), DEFAULT_TIMEOUT)
                .toCompletableFuture();
        int num = -1;
        try {
            num = (int) feture.get();
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }
        return num;
    }

    @Override
    public void sendMesssage(String id, MqttMessage mqttMessage) {
        ask(connManager, ConnectionManager.SendMessage.getInstence(id, mqttMessage), DEFAULT_TIMEOUT);
    }

    @Override
    public boolean isAlive(String id) {
        CompletableFuture<Object> feture = ask(connManager, ConnectionManager.IsAlive.getInstence(id), DEFAULT_TIMEOUT)
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
