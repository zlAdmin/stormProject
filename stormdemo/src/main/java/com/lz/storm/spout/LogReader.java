package com.lz.storm.spout;

import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.activemq.ActiveMQConnectionFactory;  

public class LogReader implements IRichSpout {

	/**
	 * 
	 */
	private static final String USERNAME="zhanglei"; 
    private static final String PASSWORD="1234qwer"; 
    private static final String BROKEURL="tcp://192.168.1.107:61616";
    
	private static final long serialVersionUID = -9189172248795985170L;
	@SuppressWarnings("unused")
	private TopologyContext context;
	private SpoutOutputCollector collector;
	private ConnectionFactory connectionFaction;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageConsumer consumer;

	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.context = context;
		this.collector = collector;
		this.connectionFaction = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);
		try{
			connection = connectionFaction.createConnection();
			connection.start();
			session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE); // 创建Session  
            destination=session.createQueue("FirstQueue1");  // 创建连接的消息队列  
            consumer=session.createConsumer(destination); // 创建消息消费者  
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void nextTuple() {
		try{
			TextMessage message = (TextMessage) consumer.receive(100000);
			String messageId = message.getJMSMessageID();
			this.collector.emit(new Values(message.getText()),messageId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("logline"));
		
	}
	
	@Override
	public void ack(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Object arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
