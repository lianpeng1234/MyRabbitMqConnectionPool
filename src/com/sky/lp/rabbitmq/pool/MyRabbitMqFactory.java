package com.sky.lp.rabbitmq.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MyRabbitMqFactory implements PooledObjectFactory<MyRabbitMqPoolObject> {
	
	private MyRabbitMqPoolConfig configPoolConfig;
	
	/**
	 * 判断字符是否 空
	 * @param arg0
	 * @return true：null 或者 ""，否则 false
	 */
	private boolean strIsEmpty(String arg0) {
		if(arg0 == null || arg0.length() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检查参数是否有效
	 * @return true 有效，false 无效
	 */
	private boolean checkParamIsValid() {
		if(configPoolConfig == null) {
			return false;
		}
		if(strIsEmpty(configPoolConfig.getHost()) || configPoolConfig.getPort() == 0 || 
				strIsEmpty(configPoolConfig.getPassword()) || strIsEmpty(configPoolConfig.getUsername()) ) {
			return false;
		}
		return true;
	}
	
	@Override
	public void activateObject(PooledObject<MyRabbitMqPoolObject> arg0) throws Exception {
		
	}

	/**
	 * 2016-1-15 10:50:14
	 * 销毁对象
	 */
	@Override
	public void destroyObject(PooledObject<MyRabbitMqPoolObject> arg0) throws Exception {
		MyRabbitMqPoolObject rabbitMqPoolObject = arg0.getObject();
		Channel channel = rabbitMqPoolObject.getChannel();
		if(channel.isOpen()) {
			channel.close();//关闭通道
		}
		Connection connection = rabbitMqPoolObject.getConnection();
		if(connection.isOpen()) {
			connection.close();//关闭连接
		}
	}

	/**
	 * 2016-1-15 10:50:33
	 * 创建对象
	 */
	@Override
	public PooledObject<MyRabbitMqPoolObject> makeObject() throws Exception {
		if(!checkParamIsValid()) {
			throw new RuntimeException("无效参数");
		}
		//创建连接工厂
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost(configPoolConfig.getHost());
	    factory.setPort(configPoolConfig.getPort());
	    factory.setUsername(configPoolConfig.getUsername());
	    factory.setPassword(configPoolConfig.getPassword());
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
		//创建 资源 对象
		MyRabbitMqPoolObject rabbitMqPoolObject = new MyRabbitMqPoolObject(channel, connection);
		
		return new DefaultPooledObject<MyRabbitMqPoolObject>(rabbitMqPoolObject);
	}

	@Override
	public void passivateObject(PooledObject<MyRabbitMqPoolObject> arg0) throws Exception {
		
	}

	/**
	 * 检验 资源 对象是否有效
	 * @return true 有效 false 无效
	 */
	@Override
	public boolean validateObject(PooledObject<MyRabbitMqPoolObject> arg0) {
		MyRabbitMqPoolObject rabbitMqPoolObject = arg0.getObject();
		Channel channel = rabbitMqPoolObject.getChannel();
		Connection connection = rabbitMqPoolObject.getConnection();
		if(channel.isOpen() && connection.isOpen()) {
			return true;
		}
		return false;
	}

	public void setConfigPoolConfig(MyRabbitMqPoolConfig configPoolConfig) {
		this.configPoolConfig = configPoolConfig;
	}

	public MyRabbitMqPoolConfig getConfigPoolConfig() {
		return configPoolConfig;
	}

	
	
}
