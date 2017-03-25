package com.covilla.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Locale;

/**
 * @description 普通类在任意位置得到spring中的bean
 * 
 * @author xuys
 * 
 * @time 2015-06-09
 *
 */
public final class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext context;
	

	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	public static String getMessage(String key) {
		return context.getMessage(key, null, Locale.getDefault());
	}
	
	public static void cleanContext() {
		context = null;
    }
	
	/**
	 * 根据类型获取spring容器里的bean实例
	 * @param clazz 类型
	 * @return bean实例
	 */
	public static <T> T getBean(Class<T> clazz){
        return context.getAutowireCapableBeanFactory().getBean(clazz);
    }
}