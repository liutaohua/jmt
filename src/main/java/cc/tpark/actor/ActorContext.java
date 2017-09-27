package cc.tpark.actor;

import akka.actor.ActorSystem;

public enum ActorContext {
    instence;
    private final ActorSystem jmt;

    ActorContext() {
        jmt = ActorSystem.create("jmt");
    }

    public ActorSystem getJmt() {
        return jmt;
    }
}
