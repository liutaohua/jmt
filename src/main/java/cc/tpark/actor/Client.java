package cc.tpark.actor;

import akka.actor.AbstractActorWithTimers;
import akka.actor.Props;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Client extends AbstractActorWithTimers {
    private final ChannelHandlerContext ctx;

    public static Props props(ChannelHandlerContext ctx) {
        return Props.create(Client.class, () -> new Client(ctx));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(MqttMessage.class, (s) -> {
            if (MqttMessageType.PINGRESP == s.fixedHeader().messageType()) {
                getTimers().cancelAll();
                getTimers().startSingleTimer("test", new FirstTick(), Duration.create(30000, TimeUnit.MILLISECONDS));
            }
            ctx.writeAndFlush(s);
//            System.out.println("xuming");
//            getTimers().cancelAll();
//            getTimers().startSingleTimer("test", new FirstTick(), Duration.create(5, TimeUnit.SECONDS));
        }).match(FirstTick.class, firstTick -> {
            getContext().parent().tell(ConnectionManager.DelConnection.getInstence(getSelf().path().name()), getSender());
        }).match(ByteBuf.class, byteBuf -> {

            ctx.writeAndFlush(byteBuf);
        }).build();
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("i will die..." + getSelf().path().name());
        super.postStop();
        ctx.close();
    }

    private static final class FirstTick {
    }

    public Client(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        getTimers().startSingleTimer("test", new FirstTick(), Duration.create(30000, TimeUnit.MILLISECONDS));
    }
}
