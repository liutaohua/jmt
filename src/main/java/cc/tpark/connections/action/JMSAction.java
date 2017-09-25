package cc.tpark.connections.action;

import cc.tpark.connections.SimpleConnections;
import cc.tpark.router.Router;
import cc.tpark.router.SimpleRouter;

import java.util.concurrent.ConcurrentHashMap;

public abstract class JMSAction {

    protected ConcurrentHashMap<Object, Object> msgMap = new ConcurrentHashMap();

    protected Router router = new SimpleRouter(SimpleConnections.INSTENCE);

}
