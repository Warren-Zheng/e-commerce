package com.zxh.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart implements Serializable{
	//瀛樻斁璐墿杞﹂」鐨勯泦鍚�  key:鍟嗗搧鐨刬d  cartitem:璐墿杞﹂」   浣跨敤map闆嗗悎渚夸簬鍒犻櫎鍗曚釜璐墿杞﹂」
	private Map<String, CartItem> map=new LinkedHashMap<>();
	
	//鎬婚噾棰�
	private Double total=0.0;
	
	/**
	 * 鑾峰彇鎵�鏈夌殑璐墿杞﹂」
	 * @return
	 */
	public Collection<CartItem> getItmes(){
		return  map.values();
	}
	/**
	 * 娣诲姞鍒拌喘鐗╄溅
	 * @param item 璐墿杞﹂」
	 */
	public void add2Cart(CartItem item){
		//1.鍏堝垽鏂喘鐗╄溅涓湁鏃犺鍟嗗搧
		//1.1鍏堣幏鍙栧晢鍝佺殑id
		String pid = item.getProduct().getPid();
		if(map.containsKey(pid)){
			//鏈�
			//璁剧疆璐拱鏁伴噺 闇�瑕佽幏鍙栬鍟嗗搧涔嬪墠鐨勮喘涔版暟閲�+鐜板湪鐨勮喘涔版暟閲�(item.getCount)
			//鑾峰彇璐墿杞︿腑璐墿杞﹂」
			CartItem oItem = map.get(pid);
			oItem.setCount(oItem.getCount()+item.getCount());
		}else{
			//娌℃湁 灏嗚喘鐗╄溅椤规坊鍔犺繘鍘�
			map.put(pid, item);
		}
		
		//2.娣诲姞瀹屾垚涔嬪悗 淇敼閲戦
		total+=item.getSubtotal();
	}
	
	/**
	 *  浠庤喘鐗╄溅鍒犻櫎鎸囧畾璐墿杞﹂」
	 * @param pid 鍟嗗搧鐨刬d
	 */
	public void removeFromCart(String pid){
		//1.浠庨泦鍚堜腑鍒犻櫎
		CartItem item = map.remove(pid);
		
		//2.淇敼閲戦
		total-=item.getSubtotal();
	}
	
	/**
	 * 娓呯┖璐墿杞�
	 */
	public void clearCart(){
		//1.map缃┖
		map.clear();
		
		//2.閲戦褰掗浂
		total=0.0;
	}
	
	

	public Map<String, CartItem> getMap() {
		return map;
	}

	public void setMap(Map<String, CartItem> map) {
		this.map = map;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	
}
