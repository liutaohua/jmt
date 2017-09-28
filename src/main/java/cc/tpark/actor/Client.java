package cc.tpark.actor;

import akka.actor.AbstractActor;

public class Client extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(String.class, (s) -> {
            System.out.println(s);
        }).build();
    }
}
