package com.lz.storm.drcp;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.StormSubmitter;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;

/**
 *        远程DRPC
 * @Filename: RemoteDrpcdemo.java
 * @Description: 
 * @Version: 1.0
 * @Author: 张磊
 * @Email: zhanglei@acmtc.com
 * @History:<br>
 *<li>Date: 2018年8月17日</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public class RemoteDrpcdemo {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        
        LinearDRPCTopologyBuilder build = new LinearDRPCTopologyBuilder("exclamation");
        build.addBolt(new ExclaimBolt());

        Config conf = new Config();
        conf.setNumWorkers(2);

        StormSubmitter.submitTopology("remote_drcp", conf, build.createRemoteTopology());
       
    }

}
