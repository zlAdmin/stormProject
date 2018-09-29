package com.lz.trident.example;

import java.util.ArrayList;
import java.util.List;

import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.state.BaseStateUpdater;
import org.apache.storm.trident.tuple.TridentTuple;

public class LocationUpdater extends BaseStateUpdater<LocationDB>{

    @Override
    public void updateState(LocationDB state, List<TridentTuple> tuples,
                            TridentCollector collector) {
        List<Long> ids = new ArrayList<Long>();
        List<String> locations = new ArrayList<String>();
        for(TridentTuple t: tuples) {
            ids.add(t.getLong(0));
            locations.add(t.getString(1));
        }
        state.setLocationsBulk(ids, locations);
        
    }

}
