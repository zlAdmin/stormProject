package com.lz.storm.bolt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleJoinPrintBolt extends BaseRichBolt {
        private static Logger LOG = LoggerFactory.getLogger(SingleJoinPrintBolt.class);
        OutputCollector _collector;
        
        private FileWriter fileWriter;

        public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
          _collector = collector;
          
        }

        public void execute(Tuple tuple) {
          try {
              if(fileWriter == null){
                  fileWriter = new FileWriter("F:\\test\\"+this);
              }
                fileWriter.write("address: " + tuple.getString(0) 
                        + " gender: " + tuple.getString(1)
                        + " age: " + tuple.getInteger(2));
                fileWriter.write("\r\n");
                fileWriter.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          _collector.ack(tuple);
        }

        public void declareOutputFields(OutputFieldsDeclarer declarer) {
        }
}