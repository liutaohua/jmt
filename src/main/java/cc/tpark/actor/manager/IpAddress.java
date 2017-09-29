package cc.tpark.actor.manager;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class IpAddress extends AbstractActor {

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(AddSession.class, addWatch -> {
//                    ActorRef r = getContext().actorOf(Props.create(IpAddrRouter.class), addWatch.ipAddr);
//                    getContext().watch(r);
//                    r.tell();
                })
                .build();
    }


    public static class AddSession {
        private String ipAddr;
        private ActorRef actorRef;

        public static AddSession getInstence(String ipAddr, ActorRef actorRef) {
            return new AddSession(ipAddr, actorRef);
        }

        private AddSession(String ipAddr, ActorRef actorRef) {
            this.actorRef = actorRef;
            this.ipAddr = ipAddr;
        }
    }
}
