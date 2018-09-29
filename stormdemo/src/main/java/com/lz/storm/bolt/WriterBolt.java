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

public class WriterBolt extends BaseRichBolt {
    /**
    *Comment for <code>serialVersionUID</code>
    */
    private static final long serialVersionUID = 2486536959395076406L;
    private static Logger     LOG              = LoggerFactory.getLogger(WriterBolt.class);
    OutputCollector           _collector;

    private FileWriter        fileWriter;
    private boolean           flag             = false;

    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        _collector = collector;

        if (fileWriter == null) {
            try {
                fileWriter = new FileWriter("F:\\test\\" + "words.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void execute(Tuple tuple) {
        LOG.info("进入执行");
        try {
            String word = tuple.getStringByField("word");
            if(!flag && word.equals("Kafka")){
                LOG.info("进入异常，重新发送");
                flag = true;
                int a = 1/0;
            }
            fileWriter.write(word + "\r\n");
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
            //对tuple进行fail，使重发。
            _collector.fail(tuple);
        }
        //对tuple进行ack
        _collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
