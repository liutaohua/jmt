package cc.tpark.connections.initializer;

import java.util.concurrent.ConcurrentHashMap;

public class JMSServerInitializerFactory {

    private static ConcurrentHashMap<Object, JMSServerInitializer> initializermap =
            new ConcurrentHashMap();

    public static JMSServerInitializer getServerInitializer(Object initializerId) {
        return initializermap.get(initializerId);
    }

    public static void addServerInitializer(Object initializerId, JMSServerInitializer jmsServerInitializer) {
        initializermap.put(initializerId, jmsServerInitializer);
    }
}
