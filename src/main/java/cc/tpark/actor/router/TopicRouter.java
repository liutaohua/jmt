package cc.tpark.actor.router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import io.netty.handler.codec.mqtt.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class TopicRouter extends AbstractActor {

    Router router;

    {
        List<Routee> routees = new ArrayList<>();
        router = new Router(new BroadcastRoutingLogic(), routees);
    }


    public static Props props() {
        return Props.create(TopicRouter.class, () -> new TopicRouter());
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AddClient.class, (client) -> {
            router = router.addRoutee(new ActorRefRoutee(client.r));
        }).match(MqttMessage.class, (s) -> {
            router.route(s, getSender());
        }).match(RemoveClient.class, (client) -> {
            router = router.removeRoutee(new ActorRefRoutee(client.r));
        }).build();
    }


    public static class AddClient {
        private ActorRef r;

        public static AddClient getInstence(ActorRef ref) {
            return new AddClient(ref);
        }

        public AddClient(ActorRef r) {
            this.r = r;
        }
    }

    public static class RemoveClient {
        private ActorRef r;

        public static RemoveClient getInstence(ActorRef ref) {
            return new RemoveClient(ref);
        }

        public RemoveClient(ActorRef r) {
            this.r = r;
        }
    }
}
