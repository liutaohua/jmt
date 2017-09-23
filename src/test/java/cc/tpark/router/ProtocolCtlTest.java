package cc.tpark.router;

import cc.tpark.commons.InnerMsg;
import cc.tpark.connections.Connections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ProtocolCtlTest {

    private class testProClass implements Connections {

        @Override
        public void sendMsg(String ip, InnerMsg msg) {
            System.out.println("send msg [ ip: " + ip + " msg:" + msg.getMsg() + "]");
        }
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testAddMembers() {
        final String topic = "aaa", ip = "xxxxx";

        ProtocolCtl pctl = new ProtocolCtl(new testProClass());
        pctl.addMember(topic, ip);


        assertEquals(pctl.getMembers(topic).get(0), ip);

        InnerMsg innerMsg = new InnerMsg();
        innerMsg.setTopic(topic);
        innerMsg.setMsg("test send msg...");
        pctl.publish(innerMsg);
        assertEquals(outContent.toString(), "send msg [ ip: " + ip + " msg:test send msg...]\r\n");


        pctl.delMember(topic, ip);
        assertEquals(pctl.getMembers(topic), null);
    }


}
