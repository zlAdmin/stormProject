package com.lz.storm.spout;

import org.apache.storm.topology.OutputFieldsDeclarer;

import java.util.Map;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSpout extends BaseRichSpout {
   public static Logger LOG = LoggerFactory.getLogger(MessageSpout.class);
   private SpoutOutputCollector _collector;
   
   private int index = 0;
   private String[] subjects = new String[]{
           "Java,Python",
           "Storm,Kafka",
           "Spring,Solr",
           "Zookeeper,FastDFS",
           "Dubbox,Redis"
   };
       
   public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
       _collector = collector;
   }
   
   public void nextTuple() {
       
       if(index < subjects.length){
           String sub = subjects[index];
           //使用messageid参数，使可靠性机制生效
           _collector.emit(new Values(sub), index);
           index++;
       }
   }
   
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
       declarer.declare(new Fields("subjects"));
   }
   
   @Override
   public void ack(Object msgId) {
       LOG.info("【消息发送成功！】(msgId = " + msgId + ")");
   }

   @Override
   public void fail(Object msgId) {
       LOG.info("【消息发送失败！】(msgId = " + msgId + ")");
       LOG.info("【重发进行中。。。】");
       _collector.emit(new Values(subjects[(Integer)msgId]), msgId);
       LOG.info("【重发成功！】");
   }
   
}
