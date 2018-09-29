package com.lz.storm.drcp;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.drpc.DRPCSpout;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.drpc.ReturnResults;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.trident.spout.ITridentSpout;

public class DrcpDemo {
    public static void main(String[] args) {
        LocalDRPC drpc = new LocalDRPC();
        LocalCluster cluster = new LocalCluster();
        
        LinearDRPCTopologyBuilder build = new LinearDRPCTopologyBuilder("exclamation");
        build.addBolt(new ExclaimBolt());

        Config conf = new Config();
        conf.setNumWorkers(2);

       cluster.submitTopology("drpc-demo", conf, build.createLocalTopology(drpc));
       String result = drpc.execute("exclamation", "hello");
       System.err.println(result);
       
       cluster.shutdown();
       drpc.shutdown();
    }

}
