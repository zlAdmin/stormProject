package com.lz.trident.example;

import java.util.List;

import org.apache.storm.trident.state.State;

public class LocationDB implements State{

    @Override
    public void beginCommit(Long txid) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void commit(Long txid) {
        // TODO Auto-generated method stub
        
    }
    
    public void setLocationsBulk(List<Long> userIds, List<String> locations) {
        // set locations in bulk
     
    }

    public List<String> bulkGetLocations(List<Long> userIds) {
        // get locations in bulk
        return null;
     
    }

}
