package com.e3mall.item.pojo;

import com.e3mall.pojo.TbItem;

public class Item extends TbItem{
	
	public Item(TbItem item){
		this.setId(item.getId());
		this.setTitle(item.getTitle());
		this.setSellPoint(item.getSellPoint());
		this.setPrice(item.getPrice());
		this.setNum(item.getNum());
		this.setBarcode(item.getBarcode());
		this.setImage(item.getImage());
		this.setCid(item.getCid());
		this.setStatus(item.getStatus());
		this.setCreated(item.getCreated());
		this.setUpdated(item.getUpdated());
	}
	
	public String[] getImages(){
		String image2 = this.getImage();
		if(image2!=null && !"".equals(image2)){
			String[] strings = image2.split(",");
			return strings;
		}
		return null;
	}
	
}
