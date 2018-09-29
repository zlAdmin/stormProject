package com.zl.storm.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.redis.bolt.RedisStoreBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisDataTypeDescription;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.ITuple;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

public class RedisStormTopology {
    public static class DataSourceSpout extends BaseRichSpout{
        private SpoutOutputCollector collection;

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            // TODO Auto-generated method stub
            
        }
        public static final String[] words =new String[] {"apple","orange","pineapple"};

        @Override
        public void nextTuple() {
            Random random = new Random();
            String word = words[random.nextInt(words.length)];
            this.collection.emit(new Values(word));
            System.out.print("ccccccc");
            Utils.sleep(1000);
            
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("line"));
        }
        
    }
    public static class SpiltBolt extends BaseRichBolt{
        private OutputCollector collector;

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            this.collector = collector;
            
        }

        @Override
        public void execute(Tuple input) {
            String value = input.getStringByField("line");
            this.collector.emit(new Values(value));
            
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {

            declarer.declare(new Fields("word"));
        }
        
    }
    public static class CountBolt extends BaseRichBolt{
        private OutputCollector collector;
        Map<String ,Integer> map = new HashMap<String,Integer>();

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            this.collector = collector;
        }

        @Override
        public void execute(Tuple input) {
            String word = input.getStringByField("word");
            Integer count = map.get(word);
            if(count==null) {
                count = 0;
            }
            count++;
            map.put(word, count);
            this.collector.emit(new Values(word,map.get(word)));
            
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word","count"));
            
        }
        
    }
    
    public static class WordCountStoreMapper implements RedisStoreMapper{
        /**
         *Comment for <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 1L;
        private RedisDataTypeDescription description;
        private final String hashKey = "wc";
        
        public WordCountStoreMapper () {
            description = new RedisDataTypeDescription(RedisDataTypeDescription.RedisDataType.HASH,hashKey);
        }

        @Override
        public String getKeyFromTuple(ITuple tuple) {
            return tuple.getStringByField("word");
        }

        @Override
        public String getValueFromTuple(ITuple tuple) {
            return tuple.getIntegerByField("count").toString();
        }

        @Override
        public RedisDataTypeDescription getDataTypeDescription() {
            return description;
        }
        
    }
    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("dataSourceSpout", new DataSourceSpout());
        builder.setBolt("spiltBolt", new SpiltBolt()).shuffleGrouping("dataSourceSpout");
        builder.setBolt("countBolt", new CountBolt()).shuffleGrouping("spiltBolt");
        
        JedisPoolConfig pollConfig = new JedisPoolConfig.Builder().setHost("10.8.5.102").setPort(6379).setPassword("1234qwer").build();
        RedisStoreMapper storeMapper = new WordCountStoreMapper();
        RedisStoreBolt storeBolt = new RedisStoreBolt(pollConfig, storeMapper);
        
        builder.setBolt("reids", storeBolt).shuffleGrouping("countBolt");
        
        LocalCluster sluster = new LocalCluster();
        sluster.submitTopology("RedisStormTopology", new Config(), builder.createTopology());
        
    }

}
