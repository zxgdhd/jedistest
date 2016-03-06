package com.cssiot.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer implements Serializable {
	private String cusId;
	private String cusName;
	private String cusNum;
	private int cusAge;
	private List<Card> cards;
	private Map<String, String> cusFeatures;
	
	
	public Customer(String cusId, String cusName, String cusNum, int cusAge, List<Card> cards,
			Map<String, String> cusFeatures) {
		super();
		this.cusId = cusId;
		this.cusName = cusName;
		this.cusNum = cusNum;
		this.cusAge = cusAge;
		this.cards = cards;
		this.cusFeatures = cusFeatures;
	}
	
	
	public Customer() {
		this.cusId = System.currentTimeMillis()%1e6+"";
		this.cusName = "嘿嘿嘿";
		this.cusNum = "nameTime="+System.nanoTime();
		this.cusAge = (int) (System.currentTimeMillis()%100);
		this.cards = new ArrayList<Card>();
		cards.add(new Card("card:"+cusId,"中国银行",1.9849148665e6));
		cards.add(new Card("card:"+cusId,"中国工商银行",2.9849148665e6));
		cards.add(new Card("card:"+cusId,"中国交通银行",3.9849148665e6));
		cards.add(new Card("card:"+cusId,"中国农业银行",4.9849148665e6));
		cards.add(new Card("card:"+cusId,"中国招商银行",5.9849148665e6));
		this.cusFeatures = new HashMap<String,String>();
		cusFeatures.put("hair", "black");
		cusFeatures.put("eyes", "blue");
		cusFeatures.put("height", "180cm");
	}


	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getCusNum() {
		return cusNum;
	}
	public void setCusNum(String cusNum) {
		this.cusNum = cusNum;
	}
	public int getCusAge() {
		return cusAge;
	}
	public void setCusAge(int cusAge) {
		this.cusAge = cusAge;
	}
	public List<Card> getCards() {
		return cards;
	}
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	public Map<String, String> getCusFeatures() {
		return cusFeatures;
	}
	public void setCusFeatures(Map<String, String> cusFeatures) {
		this.cusFeatures = cusFeatures;
	}
	
	
}
