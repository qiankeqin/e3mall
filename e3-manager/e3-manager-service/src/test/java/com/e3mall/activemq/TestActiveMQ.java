package com.e3mall.activemq;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMQ {
	
	/**
	 * 点到点形式发送消息
	 * @throws Exception
	 */
	@Test
	public void testQueueProducer() throws Exception{
		//1.创建一个连接工厂对象，指定服务的ip及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
		//2.使用工厂对象创建Connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，调用connection对象的start方法
		connection.start();
		//4.创建session对象，
		//参数1：是否开启事务（和分布式事务挂钩，一般不开启事务），如果开启事务，第二个参数没有作用
		//参数2：应答模式，是自动应答还是手动应答，区别在于自动应答，客户端接收到消息后自动给服务端响应，手动应答需要自己写代码给服务端响应，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用session对象创建一个Destination对象。两种形式queue、topic，点到点形式是queue
		//queue继承Destination类
		Queue queue = session.createQueue("test-queue");
		//6.使用session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个Message对象，可以使用TextMessage
//		TextMessage textMessage = new ActiveMQTextMessage();
//		textMessage.setText("hello,activemq");
		TextMessage textMessage = session.createTextMessage("hello,activemq");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		session.close();
		connection.close();
	}
	
	/**
	 * 接收消息
	 * @throws JMSException 
	 * @throws IOException 
	 */
	@Test
	public void testReveiveMessage() throws JMSException, IOException{
		//1.创建Connection工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
		//2.使用Connection工厂对象创建Connection
		Connection connection = connectionFactory.createConnection();
		//3.开启连接
		connection.start();
		//4.使用连接对象创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建一个destination对象：queue
		Queue queue = new ActiveMQQueue("test-queue");
		//6.使用session对象创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		//7.接收消息
		consumer.setMessageListener(new MessageListener() {
			//当消息到达的时候接收消息
			@Override
			public void onMessage(Message message) {
				//8.打印结果
				TextMessage textMessage = (TextMessage) message;
				String text = "";
				try{
					text=textMessage.getText();
					System.out.println(text);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		//等待接收消息
		System.in.read();
		//9.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	
}
