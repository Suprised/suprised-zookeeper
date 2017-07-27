package com.suprised.zookeeper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.Watcher.Event.KeeperState;

/**
 * zk�ͻ��˵�һЩ�����Ĳ��� 
 */
public class ZKClient {

    public static void main(String[] args) {
        
        ZkConnection zkConnection = new ZkConnection("192.168.1.109", 1000);
        
        // ��������ʱ��������watcher���
        /*zkConnection.connect(new Watcher() {
        
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getPath() + ", " + event.toString());
            }
        });*/
        
        // ZKClient �Ƕ�zkconnection�Ĵ���ͬʱҲ��һ��watcher���ṩ��Ӧ������������watcher��Ӧ�ı仯����IZkStateListener��IZkDataListener��_childListener��
        ZkClient client = new ZkClient(zkConnection, 1000);
        
        // ͨ�����Ľڵ㣬������ڵ�仯
        client.subscribeDataChanges("/temp", new IZkDataListener() {
            
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("handleDataDeleted()" + dataPath);
            }
            
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("handleDataChange(): " + dataPath + ", data=" + data.toString());
            }
        });
        
        client.subscribeChildChanges("/persist", new IZkChildListener() {
            
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("parentPath: " + parentPath + ", childs:" + Arrays.toString(currentChilds.toArray()));
            }
        });
        
        client.subscribeStateChanges(new IZkStateListener() {
            
            @Override
            public void handleStateChanged(KeeperState state) throws Exception {
                System.out.println(state.toString());
            }
            
            @Override
            public void handleSessionEstablishmentError(Throwable error) throws Exception {
            }
            
            @Override
            public void handleNewSession() throws Exception {
                System.out.println("handlerNewSession()..");
            }
        });
        
        if (client.exists("/temp")) {
            client.deleteRecursive("/temp");
        }
        if (client.exists("/persist")) {
            client.deleteRecursive("/persist");
        }
        
        // ��ʱĿ¼�ѵ�����������Ŀ¼��
        client.createEphemeral("/temp", "��ʱ�ڵ������");
        //client.createEphemeral("/temp/child1", "��Ŀ¼1");
        //client.createEphemeral("/temp/child2", "��Ŀ¼2");
        
        client.createPersistent("/persist", "�־û��ڵ������");
        client.createPersistent("/persist/child1", "��Ŀ¼1");
        client.createPersistent("/persist/child2", "��Ŀ¼2");
        
        List<String> childs = client.getChildren("/temp");
        for (String path : childs) {
            System.out.println(path);
        }
        
        System.out.println(client.readData("/temp"));
        
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
