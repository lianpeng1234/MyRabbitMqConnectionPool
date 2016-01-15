package com.sky.lp.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sky.lp.rabbitmq.pool.MyRabbitMqPool;

/**
 * 2016-1-15 13:47:23
 * @author liangpeng
 *
 */
public class Test {

	private static String queue_name = "lp";
	
	private static MyRabbitMqPool pool = new ClassPathXmlApplicationContext("classpath:spring-servlet.xml").getBean("myRabbitMqPool", MyRabbitMqPool.class);
	
	public static void main(String[] args) throws Exception {
		consumer.start();
		System.out.println("consumer is started");
		Thread.sleep(1000);
		produce.start();
		System.out.println("produce is started");
		System.out.println();
	}
	
	//生产者
	private static Thread produce = new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				//get rabbitmq channel
				Channel channel = pool.getRabbitChannel();
				channel.queueDeclare(queue_name, false, false, false, null);
				channel.basicPublish("", queue_name, null, "hello rabbimq i am produce".getBytes());
				//return channel to pool
				pool.releaseRabbitChannel(channel);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	
	//消费者
	private static Thread consumer = new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				final Channel channel = pool.getRabbitChannel();
				
				Consumer consumer = new DefaultConsumer(channel) {
					@Override
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
						String message = new String(body, "UTF-8");
						System.out.println("received message:" + message + "'");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						channel.basicAck(envelope.getDeliveryTag(), false);
					}
				};
				channel.basicConsume(queue_name, false, consumer);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	
}
