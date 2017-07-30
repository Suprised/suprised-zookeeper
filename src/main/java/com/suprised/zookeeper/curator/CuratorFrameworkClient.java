package com.suprised.zookeeper.curator;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;

import com.suprised.zookeeper.Conts;

/**
 * ʹ��curator�ͻ��˲���zk
 */
public class CuratorFrameworkClient {
	private static CuratorFramework client = null;
	static {
		client = CuratorFrameworkFactory.newClient(Conts.IP_CLUSTERS, new RetryPolicy() {
			
			@Override
			public boolean allowRetry(int arg0, long arg1, RetrySleeper retrySleeper) {
				System.out.println("allowRetry: " + retrySleeper);
				return true;
			}
		});
		client.start();
	}
	
	private static CuratorFramework getClient() {
		return client;
	}

	public static void main(String[] args) throws Exception {
		addListener();
		
		createPersistNode("/curatorNode_p", "p_data");
		createPersistNode("/curatorNode_p/child1", "p_child1_data");
		createTempNode("/curatorNode_e", "e_data");
		
		// ��ӡ�����еĽڵ�
		List<String> paths = client.getChildren().forPath("/");
		for (String t_path : paths) {
			System.out.println(t_path);
		}
	}
	
	/**
	 * ����һ���־û���path,������ڽڵ���ɾ�����ٴ���
	 * 
	 * @param path 
	 * @param data
	 * @throws Exception
	 */
	private static void createPersistNode(String path, String data) throws Exception {
		CuratorFramework client = getClient();
		
		if (client.checkExists().watched().forPath(path) != null) {
			client.delete().deletingChildrenIfNeeded().forPath(path);
		}
		// �����־û��ڵ�
		path = client.create().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes());
		System.out.println(new String(client.getData().forPath(path)));
	}
	
	/**
	 * ����һ����ʱ�Ľڵ�/��ʱ�ڵ㲻�������ӽڵ�
	 * 
	 * @param path
	 * @param data
	 * @throws Exception
	 */
	private static void createTempNode(String path, String data) throws Exception {
		CuratorFramework client = getClient();
		if (client.checkExists().watched().forPath(path) != null) {
			client.delete().forPath(path);
		}
		path = client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
		System.out.println(new String(client.getData().watched().forPath(path)));
	}
	
	/**
	 * ���
	 */
	private static void addListener() {
		CuratorFramework client = getClient();
		// zk �쳣������
		client.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
			
			@Override
			public void unhandledError(String message, Throwable e) {
				e.printStackTrace();
				System.out.println("unhandledError: " + message);
			}
		});
		// zk �¼�����
		client.getCuratorListenable().addListener(new CuratorListener() {
			
			@Override
			public void eventReceived(CuratorFramework client, CuratorEvent event)
					throws Exception {
				CuratorEventType type = event.getType();
				System.out.println("eventReceived : " + type.toString());
			}
		});
		
		// zk �Ự״̬����
		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
			
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState state) {
				System.out.println("stateChanged:" + state.toString());
			}
		});
	}
}
