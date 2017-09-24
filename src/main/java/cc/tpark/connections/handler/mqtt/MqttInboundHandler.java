package cc.tpark.connections.handler.mqtt;

import cc.tpark.connections.action.mqtt.MqttAction;
import cc.tpark.connections.handler.JMTHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

public class MqttInboundHandler extends JMTHandler<MqttMessage> {

    private MqttAction mqttAction;

    MqttInboundHandler() {
        mqttAction = new MqttAction();
    }

    //    @Override
    //    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    //        super.userEventTriggered(ctx, evt);
    //        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
    //            IdleStateEvent event = (IdleStateEvent) evt;
    //            if (event.state() == IdleState.WRITER_IDLE) {
    //                MqttMessage mqttMessage = new MqttMessage(
    //                        new MqttFixedHeader(MqttMessageType.PINGRESP, false,
    //                                MqttQoS.EXACTLY_ONCE, false, 0));
    //
    //                ctx.writeAndFlush(mqttMessage);
    //            }
    ////            if (event.state() == IdleState.READER_IDLE)
    ////                System.out.println("read idle");
    ////            else if (event.state() == IdleState.WRITER_IDLE)
    ////                System.out.println("write idle");
    ////            else if (event.state() == IdleState.ALL_IDLE)
    ////                System.out.println("all idle");
    //        }
    //    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {

        MqttMessageType mqttMessageType = msg.fixedHeader().messageType();
        switch (mqttMessageType) {
            case CONNECT:
                mqttAction.connection(ctx, (MqttConnectMessage) msg);
                System.out.println("发送回复");
                break;
            case SUBSCRIBE:
                mqttAction.subscribe(ctx, (MqttSubscribeMessage) msg);
                System.out.println("成功订阅");
                break;
            case UNSUBSCRIBE:
                mqttAction.unsubscribe(ctx, (MqttUnsubscribeMessage) msg);
                System.out.println("退订方法");
                break;
            case PUBLISH:
                mqttAction.publish(ctx, (MqttPublishMessage) msg);
                System.out.println("发布信息方法");
                break;
            case DISCONNECT:
                mqttAction.disconnect(ctx, (MqttConnectMessage) msg);
                System.out.println("发布信息方法");
                break;
            case PINGREQ:
                MqttMessage mqttMessage = new MqttMessage(
                        new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.EXACTLY_ONCE,
                                false, 0));
                ctx.writeAndFlush(mqttMessage);
                System.out.println("回复心跳");
                break;
            default:
                System.out.println(mqttMessageType);
                break;
        }
    }

}
