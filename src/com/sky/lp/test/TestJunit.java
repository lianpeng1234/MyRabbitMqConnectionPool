package com.sky.lp.test;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sky.lp.rabbitmq.pool.MyRabbitMqPool;

public class TestJunit {
	
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-servlet.xml");
		MyRabbitMqPool pool = context.getBean("myRabbitMqPool", MyRabbitMqPool.class);
		final Channel channel = pool.getRabbitChannel();
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		channel.basicConsume("lp", false, consumer);
	}
	
	
	
	
	
	
	
	
	
	

}
