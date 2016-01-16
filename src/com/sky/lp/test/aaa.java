package com.sky.lp.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rabbitmq.client.Channel;
import com.sky.lp.rabbitmq.pool.MyRabbitMqPool;

public class aaa {

	private static String queue_name = "lp";

	private static MyRabbitMqPool pool = new ClassPathXmlApplicationContext("classpath:spring-servlet.xml").getBean("myRabbitMqPool", MyRabbitMqPool.class);

	private static int lp = 0;
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// get rabbitmq channel
						Channel channel = pool.getRabbitChannel();
						channel.queueDeclare(queue_name, false, false, false, null);
						channel.basicPublish("", queue_name, null, "hello rabbimq i am produce".getBytes());
						lp++;
						Thread.sleep(1000*lp);
						// return channel to pool
						pool.releaseRabbitChannel(channel);
						System.out.println(Thread.currentThread().getName() + " end");

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.setName("i am thred " + (i+1));
			t.start();
			System.out.println("i am thred " + (i+1) + " start");
		}
	}

}
