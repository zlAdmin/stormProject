package com.lz.storm.example;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import com.google.common.collect.Maps;

import java.util.Map;

import static org.apache.storm.utils.Utils.tuple;

public class WordCounter implements IBasicBolt {
    private Map<String, Integer> wordCounter = Maps.newHashMap();

    public void prepare(Map stormConf, TopologyContext context) {
        
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        String word = input.getStringByField("word");
        int count;
        if (wordCounter.containsKey(word)) {
            count = wordCounter.get(word) + 1;
            wordCounter.put(word, wordCounter.get(word) + 1);
        } else {
            count = 1;
        }

        wordCounter.put(word, count);
        collector.emit(new Values(word, String.valueOf(count)));
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}
