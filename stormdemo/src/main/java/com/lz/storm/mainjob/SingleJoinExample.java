package com.lz.storm.mainjob;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.testing.FeederSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import com.lz.storm.bolt.SingleJoinBolt;
import com.lz.storm.bolt.SingleJoinPrintBolt;

/** Example of using a simple custom join bolt
 *  NOTE: Prefer to use the built-in JoinBolt wherever applicable
 */

public class SingleJoinExample {
  public static void main(String[] args) {
    FeederSpout genderSpout = new FeederSpout(new Fields("id","name","address","gender"));
    FeederSpout ageSpout = new FeederSpout(new Fields("id","name","age"));

    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("gender", genderSpout);
    builder.setSpout("age", ageSpout);
    builder.setBolt("join", new SingleJoinBolt(new Fields("address","gender","age"))).fieldsGrouping("gender", new Fields("id","name"))
        .fieldsGrouping("age", new Fields("id","name"));
    builder.setBolt("print", new SingleJoinPrintBolt()).shuffleGrouping("join");

    Config conf = new Config();
    conf.setDebug(false);

    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology("join-example", conf, builder.createTopology());

    for (int i = 0; i < 10; i++) {
      String gender;
      String name = "Tom" + i;
      String address = "Beijing " + i; 
      if (i % 2 == 0) {
        gender = "male";
      }
      else {
        gender = "female";
      }
      genderSpout.feed(new Values(i,name,address,gender));
    }

    for (int i = 9; i >= 0; i--) {
      ageSpout.feed(new Values(i, "Tom" + i , i + 20));
    }

    Utils.sleep(20000);
    cluster.shutdown();
  }
}