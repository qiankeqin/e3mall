package com.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3mall.common.pojo.E3Result;
import com.e3mall.common.pojo.SearchItem;
import com.e3mall.search.mapper.ItemMapper;
import com.e3mall.search.service.SearchItemService;

/**
 * 索引库维护Service
 * @author qiankeqin
 *
 */
@Service
public class SearchItemServiceImpl implements SearchItemService{

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public E3Result importAllItems() {
		try{
			//查询商品列表
			List<SearchItem> itemList = itemMapper.getItemList();
			//遍历商品列表
			for(SearchItem item : itemList){
				//创建文档对象
				SolrInputDocument document = new SolrInputDocument();
				//向文档对象中添加域
				document.addField("id", item.getId());
				document.addField("item_title", item.getTitle());
				document.addField("item_sell_point", item.getSell_point());
				document.addField("item_image", item.getImage());
				document.addField("item_price", item.getPrice());
				document.addField("item_category_name", item.getCategory_name());
				//把文档对象写入索引库
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
		}catch(Exception ex){
			ex.printStackTrace();
			return E3Result.build(E3Result.EXCEPTION_CODE,ex.getMessage());
		}
		//返回导入成功
		return E3Result.ok();
	}

}
