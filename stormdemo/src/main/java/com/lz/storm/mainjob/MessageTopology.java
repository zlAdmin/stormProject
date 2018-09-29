package com.lz.storm.mainjob;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import com.lz.storm.bolt.SpliterBolt;
import com.lz.storm.bolt.WriterBolt;
import com.lz.storm.spout.MessageSpout;

public class MessageTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("MessageSpout", new MessageSpout(), 1);
        builder.setBolt("SpilterBolt", new SpliterBolt(), 5).shuffleGrouping("MessageSpout");
        builder.setBolt("WriterBolt", new WriterBolt(), 1).shuffleGrouping("SpilterBolt");

        Config conf = new Config();
        conf.setDebug(true);


        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("messagetest", conf, builder.createTopology());
        Utils.sleep(20000);
        cluster.killTopology("messagetest");
        cluster.shutdown();
    }
}
