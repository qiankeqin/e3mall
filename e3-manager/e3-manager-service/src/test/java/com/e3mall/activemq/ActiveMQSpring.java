package com.e3mall.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActiveMQSpring {
	
	@Test
	public void sendMessage() throws Exception{
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//从容器中获得JmsTemplate对象
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		//从容器中获得一个Destination对象
//		Destination destination = applicationContext.getBean(Destination.class);
		Destination destination = (Destination) applicationContext.getBean("queueDestination");
		//发送消息
		jmsTemplate.send(destination, new MessageCreator() {
			//创建消息，把消息发送到destination上
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage("send activemq message");
			}
		});
	}
	
	
}
