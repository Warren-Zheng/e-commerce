package com.zxh.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 瀹炰綋宸ュ巶绫�
 * @author Administrator
 *
 */
public class BeanFactory {
	/**
	 * 閫氳繃缁欏畾涓�涓猧d杩斿洖涓�涓寚瀹氱殑瀹炵幇绫�
	 * @param id
	 * @return
	 */
	public static Object getBean(String id){
		//閫氳繃id鑾峰彇涓�涓寚瀹氱殑瀹炵幇绫�
		
		try {
			//1.鑾峰彇document瀵硅薄
			Document doc=new SAXReader().read(BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml"));
			//2.鑾峰彇鎸囧畾鐨刡ean瀵硅薄 xpath
			Element ele=(Element) doc.selectSingleNode("//bean[@id='"+id+"']");
			
			//3.鑾峰彇bean瀵硅薄鐨刢lass灞炴��
			String value = ele.attributeValue("class");
			
			//4.鍙嶅皠 浠ュ墠鐨勯�昏緫鐩存帴杩斿洖鐨勬槸瀹炰緥	
			//return Class.forName(value).newInstance();
			
			//5.鐜板湪瀵箂ervice涓璦dd鏂规硶杩涜鍔犲己 杩斿洖鍊肩殑鏄唬鐞嗗璞�
			final Object obj=Class.forName(value).newInstance();
			//鏄痵ervice鐨勫疄鐜扮被
			if(id.endsWith("Service")){
				Object proxyObj = Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						//缁х画鍒ゆ柇鏄惁璋冪敤鐨刟dd鎴栬�卹egist
						if("add".equals(method.getName()) || "regist".equals(method.getName())){
							System.out.println("娣诲姞鎿嶄綔");
							return method.invoke(obj, args);
						}
						
						return method.invoke(obj, args);
					}
				});
				
				//鑻ユ槸service鏂规硶杩斿洖鐨勬槸浠ｇ悊瀵硅薄
				return proxyObj;
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getBean("ProductDao"));;
	}
}
