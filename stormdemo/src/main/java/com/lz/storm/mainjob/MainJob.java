package com.lz.storm.mainjob;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.trident.TridentTopology;

import com.lz.storm.bolt.LogAnalysis;
import com.lz.storm.bolt.PageViewCounter;
import com.lz.storm.spout.LogReader;

public class MainJob {
	public static void main(String[] args) throws Exception{
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("log-reader", new LogReader());
		builder.setBolt("log-analysis", new LogAnalysis()).shuffleGrouping("log-reader");
		builder.setBolt("pageview-counter", new PageViewCounter(),2).shuffleGrouping("log-analysis");
		
		Config conf = new Config();
		conf.setDebug(false);
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("log-process-topogie", conf, builder.createTopology());
		//TridentTopology topology = new TridentTopology();
	}

}
