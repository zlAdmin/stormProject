package com.lz.storm.drcp;

import java.util.Map;

import org.apache.storm.Config;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.DRPCExecutionException;
import org.apache.storm.thrift.TException;
import org.apache.storm.thrift.transport.TTransportException;
import org.apache.storm.utils.DRPCClient;
import org.apache.storm.utils.Utils;

public class RemoteDrpcClient {
    public static void main(String[] args) {

        Map<String, Object> conf = Utils.readDefaultConfig();
        DRPCClient client;
        try {
            client = new DRPCClient(conf,"10.8.3.111", 3772);
            String result = client.execute("exclamation", "test");
            System.out.println(result);
        } catch (TTransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (DRPCExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AuthorizationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
