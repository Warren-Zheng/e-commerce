package com.zxh.demo;

import com.zxh.utils.JsonUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Jsondemo {
	public static void main(String[] args) {
		Person p=new Person("qwe", "tom", "123");
		//涓嶅寘鍚偅浜涘唴瀹�
		JsonConfig config = JsonUtil.configJson(new String[]{"password","class","id"});
		JSONObject json = JSONObject.fromObject(p, config);
		//String j = JsonUtil.object2json(p);
		System.out.println(json);
	}
}
