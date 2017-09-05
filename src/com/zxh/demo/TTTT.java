package com.zxh.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TTTT {

	public static void main(String[] args) throws Exception {
		Map<String, Object> map=new HashMap<>();
		map.put("id", "007");
		map.put("name", "bangde");
		map.put("password", "xxx");
		
		
		Person p = new Person();
		//BeanUtils.populate(p, map);
		map2bean(p,map);
		
		System.out.println(p);
	}

	private static void map2bean(Person p, Map<String, Object> map) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class clazz = p.getClass();
		for (String  key:map.keySet()) {
			for(Method m:clazz.getMethods()){
				if(("set"+key).equalsIgnoreCase(m.getName())){
					m.invoke(p, map.get(key));
					break;
				}
			}
		}
	}

}
