package com.sky.lp.rabbitmq.pool;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class MyRabbitMqPoolObject {

	private Channel channel = null;
	
	private Connection connection = null;

	public MyRabbitMqPoolObject(Channel channel, Connection connection) {
		super();
		this.channel = channel;
		this.connection = connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public Connection getConnection() {
		return connection;
	}
	
	
	
	
}
