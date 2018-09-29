package com.lz.storm.bolt;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpliterBolt extends BaseRichBolt {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3851924019068828136L;
    OutputCollector _collector;
    private boolean flag = false;
    private static Logger     LOG              = LoggerFactory.getLogger(SpliterBolt.class);
    
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
      _collector = collector;
    }

    public void execute(Tuple tuple) {
        
        try{
            String subjects = tuple.getStringByField("subjects");
            LOG.info("拆分为："+subjects);
            if(!flag && subjects.equals("Spring,Solr")){
                flag = true;
                int a = 1/0;
            }
            
            String[] words = subjects.split(",");
            for(String word : words){
                //注意：要携带tuple对象，用于处理异常时重发策略。
                _collector.emit(tuple, new Values(word));
            }
            
            //对tuple进行ack
            _collector.ack(tuple);
        }catch(Exception ex){
            ex.printStackTrace();
            //对tuple进行fail，使重发。
            _collector.fail(tuple);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("word"));
    }

  }