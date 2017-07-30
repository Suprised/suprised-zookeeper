package com.suprised.zookeeper;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 * zookeeper仲裁模式连接
 */
public class ZKClientCluster {

	public static void main(String[] args) {
		
		/**
		 * 必须两个zk服务器启动才能正常运行
		 */
		ZkConnection zkConnection = new ZkConnection(Conts.IP_CLUSTERS, 3000);
		
		ZkClient client = new ZkClient(zkConnection, 3000);// 连接超时3s
		
		List<String> paths = client.getChildren("/");
		for (String path : paths) {
			System.out.println("root: " + path);
		}
	}
	
}
