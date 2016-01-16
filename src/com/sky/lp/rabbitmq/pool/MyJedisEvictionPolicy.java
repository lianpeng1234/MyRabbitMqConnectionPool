package com.sky.lp.rabbitmq.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.EvictionConfig;
import org.apache.commons.pool2.impl.EvictionPolicy;

public class MyJedisEvictionPolicy implements EvictionPolicy {

	@Override
	public boolean evict(EvictionConfig config, PooledObject underTest, int idleCount) {
		boolean isEvict = ( config.getIdleSoftEvictTime() < underTest.getIdleTimeMillis() && config.getMinIdle() < idleCount ) || config.getIdleEvictTime() < underTest.getIdleTimeMillis();
		
		System.out.println("config.getIdleSoftEvictTime:" + config.getIdleSoftEvictTime() + "    underTest.getIdleTimeMillis:" + underTest.getIdleTimeMillis());
		System.out.println("config.getMinIdle():" + config.getMinIdle() + "   idleCount:" + idleCount);
		System.out.println("config.getIdleEvictTime():" + config.getIdleEvictTime() + "     underTest.getIdleTimeMillis():" + underTest.getIdleTimeMillis());
		System.out.println("isEvict:" + isEvict);
		System.out.println("========================================================================");
		return isEvict;
		
	}

}
