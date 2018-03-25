package com.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.e3mall.item.pojo.Item;
import com.e3mall.pojo.TbItem;
import com.e3mall.pojo.TbItemDesc;
import com.e3mall.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态模板生成Listener
 * 监听商品生成消息，生成对应的静态页面
 * @author qiankeqin
 *
 */
public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${FREEMARKER_GEN_DIR}")
	private String FREEMARKER_GEN_DIR;
	
	@Override
	public void onMessage(Message message) {
		//创建一个模板ftl文件
		//从消息中取商品id
		TextMessage textMessage = (TextMessage) message;
		String text = null;
		try {
			text = textMessage.getText();
			Long itemId = new Long(text);
			//等待商品事务提交
			Thread.sleep(2000);
			//根据商品id查询商品信息，商品基本信息和商品描述信息
			System.out.println(itemId);
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			//创建数据集，封装到Map中
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			//加载模板对象，
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl","utf-8");
			//创建一个输出流，指定输出目录及文件名（id.html）
			Writer writer = new FileWriter(new File(FREEMARKER_GEN_DIR+item.getId()+".html"));
			//生成静态文件
			template.process(data, writer);
			//关闭流
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
