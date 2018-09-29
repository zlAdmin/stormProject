package com.lz.storm.drcp;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class ExclaimBolt extends BaseRichBolt {
    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private OutputCollector   outputCollector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

        this.outputCollector = collector;
    }

    @Override
    public void execute(Tuple input) {
        Object requestId = input.getValue(0);
        String name = input.getString(1);
        
        String  result =  "result:"+name;
        this.outputCollector.emit(new Values(requestId,result));

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "result"));
    }

}
