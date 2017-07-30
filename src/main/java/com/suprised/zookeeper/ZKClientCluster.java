package com.suprised.zookeeper;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 * zookeeper�ٲ�ģʽ����
 */
public class ZKClientCluster {

	public static void main(String[] args) {
		
		/**
		 * ��������zk����������������������
		 */
		ZkConnection zkConnection = new ZkConnection(Conts.IP_CLUSTERS, 3000);
		
		ZkClient client = new ZkClient(zkConnection, 3000);// ���ӳ�ʱ3s
		
		List<String> paths = client.getChildren("/");
		for (String path : paths) {
			System.out.println("root: " + path);
		}
	}
	
}
