package com.sky.lp.rabbitmq.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.InitializingBean;

import com.rabbitmq.client.Channel;

public class MyRabbitMqPool implements InitializingBean {

	private GenericObjectPool<MyRabbitMqPoolObject> objectPool;

	private ThreadLocal<MyRabbitMqPoolObject> threadLocal = new InheritableThreadLocal<MyRabbitMqPoolObject>();
	
	private MyRabbitMqFactory factory;
	
	public MyRabbitMqFactory getFactory() {
		return factory;
	}

	public void setFactory(MyRabbitMqFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * 
	 * @return
	 */
	public Channel getRabbitChannel() {
		try {
			MyRabbitMqPoolObject poolObject = objectPool.borrowObject();
			threadLocal.set(poolObject);
			Channel channel = poolObject.getChannel();
			return channel;
		} catch (Exception e) {
			throw new RuntimeException("Could not get a resource from the pool", e);
		}
	}
	
	public void releaseRabbitChannel(Channel channel) {
		MyRabbitMqPoolObject poolObject = threadLocal.get();
		objectPool.returnObject(poolObject);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.objectPool = new GenericObjectPool<MyRabbitMqPoolObject>(factory, factory.getConfigPoolConfig());
	}
	
}
