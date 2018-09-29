package com.lz.trident.example;

import java.util.Properties;

import org.apache.storm.jdbc.trident.state.JdbcStateFactory;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.trident.state.map.CachedMap;
import org.apache.storm.trident.state.map.IBackingMap;
import org.apache.storm.trident.state.map.SnapshottableMap;
import org.apache.storm.trident.testing.MemoryMapState;
import org.apache.storm.tuple.Fields;

public class Topology {
    public static void main(String[] args) {
        
        
        Properties props = System.getProperties(); 
        props.list(System.out);
    }

}
