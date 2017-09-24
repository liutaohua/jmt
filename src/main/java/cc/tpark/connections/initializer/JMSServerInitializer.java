package cc.tpark.connections.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public abstract class JMSServerInitializer extends ChannelInitializer<SocketChannel> {

    protected abstract void initChannel(SocketChannel ch) throws Exception;
}
