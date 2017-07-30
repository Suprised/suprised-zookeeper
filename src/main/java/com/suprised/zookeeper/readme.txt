zk 的节点包括：
1，持久节点persistent
2，临时节点（不能拥有子节点）ephemeral，客户端会话断开后，创建的临时节点全部消失。
3，有序节点sequential:zk保持节点的唯一，包括持久有序和临时有序

监听通知/类似与redis的发布订阅
监视点(watch)是单次触发，客户端触发通知后，需要再次设置新的监视点;这个问题在zk客户端api(例如：I0Itec)已经可以解决。

每个节点都有一个版本(version),每次操作版本号都会自增，类似于hibernate的乐观锁。

zk服务器运行模式：
1，独立模式：一个zk服务器；
2，仲裁模式：一组zk服务器，zookeeper集合。
仲裁模式下，所有zk服务器数据状态都是同步了，然而这种状态的同步会导致客户端的延迟问题，所以就出现zookeeper的仲裁。
选择法定人数，只要zk数据同步个数达到法定人数，则客户端就可以继续，而不需要等到所有服务器都同步完。
法定人数的大小一般的多数方案：例如，有五台服务器，法定人数为3，则最多允许2台服务器崩溃，服务器大小一般为奇数。

zookeeper会话的状态KeeperState：Disconnected、SyncConnected、AuthFailed、ConnectedReadOnly、
SaslAuthenticated、Expired.

事件类型EventType：NodeCreated、NodeDeleted、NodeDataChanged、NodeChildrenChanged和None.