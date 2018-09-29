package com.lz.storm.filter;

import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;

public class TestFilter extends BaseFilter {

    @Override
    public boolean isKeep(TridentTuple tuple) {
        // TODO Auto-generated method stub
        return false;
    }

}
