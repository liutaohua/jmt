package cc.tpark.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

public class Topic extends AbstractActor {

    Router router;

    {
        List<Routee> routees = new ArrayList<>();
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().match(AddClient.class, (client) -> {
            getContext().watch(client.r);
            router = router.addRoutee(new ActorRefRoutee(client.r));
        }).match(String.class, (s) -> {
            router.route(s, getSelf());
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
