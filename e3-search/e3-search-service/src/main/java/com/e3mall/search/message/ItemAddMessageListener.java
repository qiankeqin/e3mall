package com.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.e3mall.common.pojo.SearchItem;
import com.e3mall.search.mapper.ItemMapper;

/**
 * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
 * @author qiankeqin
 *
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long id = new Long(text);
			//等待一下，等待后台提交事务
			Thread.sleep(100);
			//根据商品id，查询商品信息
			SearchItem searchItem = itemMapper.getItemById(id);
			//创建一个文档对象
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			//向文档对象中添加域
			solrInputDocument.addField("id", searchItem.getId());
			solrInputDocument.addField("item_title", searchItem.getTitle());
			solrInputDocument.addField("item_category_name", searchItem.getCategory_name());
			solrInputDocument.addField("item_sell_point", searchItem.getSell_point());
			solrInputDocument.addField("item_image", searchItem.getImage());
			solrInputDocument.addField("item_price", searchItem.getPrice());
			//把文档写入到索引库中
			solrServer.add(solrInputDocument);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
