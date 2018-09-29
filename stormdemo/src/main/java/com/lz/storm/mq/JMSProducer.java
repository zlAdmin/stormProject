package com.lz.storm.mq;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


/**
 *                       
 * @Filename: JMSProducer.java
 * @Description: 消息生生产者
 * @Version: 1.0
 * @Author: 张磊
 * @Email: zhanglei@acmtc.com
 * @History:<br>
 *<li>Date: 2018年9月10日</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class JMSProducer {

    private static final String USERNAME = "zhanglei";
    private static final String PASSWORD = "1234qwer";
    private static final String BROKEURL = "tcp://10.8.3.111:61616";
    private static final int    SENDNUM  = 100;                      // 发送的消息数量  
    private static final String[] INFOS = { "116.191031,39.988585", "116.389275,39.925818", "116.287444,39.810742",
                                            "l116.481707,39.940089", "116.410588,39.880172", "l116.394816,39.91181",
                                            "116.416002,39.952917" 
                                            };

    private static final String[] PHONES = { "13189888878", "13179888873", "13189588874", "13189848878",
                                             "13189885873", "13189808874", "13189848875", "13189688873",
                                             "13189898874", "13189888878", "13189888872", "13189888871" 
                                             };

    private static final ThreadLocal<DateFormat> simpleLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        
    };
    private static final Random  random = new Random();
    public static void main(String[] args) {

        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer messageProducer;
        

        connectionFactory = new ActiveMQConnectionFactory(JMSProducer.USERNAME,
            JMSProducer.PASSWORD, JMSProducer.BROKEURL);

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("backtype.storm.contrib.example.queue");
            messageProducer = session.createProducer(destination);
            sendMessage(session, messageProducer);
            session.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    // TODO Auto-generated catch block  
                    e.printStackTrace();
                }
            }
        }
    }

    /** 
     * 发送消息 
     * @param session 
     * @param messageProducer 
     * @throws Exception 
     */
    public static void sendMessage(Session session,
                                   MessageProducer messageProducer) throws Exception {
        StringBuilder build = null;
        for (int i = 0; i < JMSProducer.SENDNUM; i++) {
            build = new StringBuilder();
            Date date = new Date();
            String da = simpleLocal.get().format(date);
            String phone = PHONES[random.nextInt(PHONES.length)];
            String addre = INFOS[random.nextInt(INFOS.length)];
            build.append(phone).append("\t");
            build.append(addre).append(" \t");
            build.append(da);
            TextMessage message = session.createTextMessage(build.toString());
            System.out.println(build.toString());
            messageProducer.send(message);
        }
    }
}
