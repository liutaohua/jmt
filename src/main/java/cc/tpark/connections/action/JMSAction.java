package cc.tpark.connections.action;

import cc.tpark.connections.SimpleConnections;
import cc.tpark.router.Router;
import cc.tpark.router.SimpleRouter;

public abstract class JMSAction {

    protected Router router = new SimpleRouter(SimpleConnections.INSTENCE);

}
