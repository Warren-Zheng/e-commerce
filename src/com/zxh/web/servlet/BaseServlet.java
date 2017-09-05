package com.zxh.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 閫氱敤鐨剆ervlet
 */
public class BaseServlet extends HttpServlet {
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1.鑾峰彇瀛愮被  鍒涘缓瀛愮被鎴栬�呰皟鐢ㄥ瓙绫荤殑鏃跺�� this浠ｈ〃鐨勬槸瀛愮被瀵硅薄
			@SuppressWarnings("rawtypes")
			Class clazz = this.getClass();
			//System.out.println(this);

			// 2.鑾峰彇璇锋眰鐨勬柟娉�
			String m = request.getParameter("method");
			if(m==null){
				m="index";
			}
			//System.out.println(m);

			// 3.鑾峰彇鏂规硶瀵硅薄
			Method method = clazz.getMethod(m, HttpServletRequest.class, HttpServletResponse.class);
			
			// 4.璁╂柟娉曟墽琛� 杩斿洖鍊间负璇锋眰杞彂鐨勮矾寰�
			String s=(String) method.invoke(this, request,response);//鐩稿綋浜� userservlet.add(request,response)
			
			// 5.鍒ゆ柇s鏄惁涓虹┖
			if(s!=null){
				request.getRequestDispatcher(s).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} 

	}
	
	
	public String index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return null;
	}
	
}
