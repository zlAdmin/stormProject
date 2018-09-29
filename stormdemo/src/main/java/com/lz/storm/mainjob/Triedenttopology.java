package com.lz.storm.mainjob;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.testing.FixedBatchSpout;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.trident.testing.Split;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;


public class Triedenttopology {
    public static void main(String[] args) {
        @SuppressWarnings("unchecked")
        FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
            new Values("the cow jumped over the moon"),
            new Values("the man went to the store and bought some candy"),
            new Values("four score and seven years ago"),
            new Values("how many apples can you eat"));
            spout.setCycle(true);            
        
            TridentTopology topology = new TridentTopology();        
            TridentState wordCounts =
                 topology.newStream("spout1", spout)
                   .each(new Fields("sentence"), new Split(), new Fields("word"))
                   .groupBy(new Fields("word"))
                   .persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"))                
                   .parallelismHint(6);
            
            Config conf = new Config();
            conf.setDebug(false);
            conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("log-process-topogie", conf, topology.build());
            
    }

}
