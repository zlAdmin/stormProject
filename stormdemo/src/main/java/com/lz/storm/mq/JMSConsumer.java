package com.lz.storm.mq;
import javax.jms.Connection;  
import javax.jms.ConnectionFactory;  
import javax.jms.Destination;  
import javax.jms.JMSException;  
import javax.jms.MessageConsumer;  
import javax.jms.Session;  
import javax.jms.TextMessage;  
  
import org.apache.activemq.ActiveMQConnectionFactory;  
  
/** 
 * 消息消费者 
 * @author Administrator 
 * 
 */  
public class JMSConsumer {  
  
    private static final String USERNAME="zhanglei";
    private static final String PASSWORD="1234qwer"; 
    private static final String BROKEURL="tcp://10.8.3.111:61616";
      
    public static void main(String[] args) {  
        ConnectionFactory connectionFactory; 
        Connection connection = null; 
        Session session; // 会话 接受或者发送消息的线程  
        Destination destination; // 消息的目的地  
        MessageConsumer messageConsumer; // 消息的消费者  
          
        
        connectionFactory=new ActiveMQConnectionFactory(JMSConsumer.USERNAME, JMSConsumer.PASSWORD, JMSConsumer.BROKEURL);  
                  
        try {  
            connection=connectionFactory.createConnection();  // 通过连接工厂获取连接  
            connection.start(); // 启动连接  
            session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session  
            destination=session.createQueue("FirstQueue1");  // 创建连接的消息队列  
            messageConsumer=session.createConsumer(destination); // 创建消息消费者  
            while(true){  
                TextMessage textMessage=(TextMessage)messageConsumer.receive(100000);  
                if(textMessage!=null){  
                    System.out.println("收到的消息："+textMessage.getText());  
                }else{  
                    break;  
                }  
            }  
        } catch (JMSException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }   
    }  
}
