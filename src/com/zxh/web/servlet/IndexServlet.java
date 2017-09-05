package com.zxh.web.servlet;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxh.domain.Category;
import com.zxh.domain.Product;
import com.zxh.service.CategoryService;
import com.zxh.service.ProductService;
import com.zxh.service.impl.CategoryServiceImpl;
import com.zxh.service.impl.ProductServiceImpl;
import com.zxh.utils.BeanFactory;

/**
 * 鍜岄椤电浉鍏崇殑servlet
 */
public class IndexServlet extends BaseServlet {
	public String index(HttpServletRequest request, HttpServletResponse response) {
		//鍘绘暟鎹簱涓煡璇㈡渶鏂板晢鍝佸拰鐑棬鍟嗗搧  灏嗕粬浠斁鍏equest鍩熶腑 璇锋眰杞彂
		ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
		
		//鏈�鏂板晢鍝�
		List<Product> newList=null;
		List<Product> hotList=null;
		try {
			newList = ps.findNew();
			hotList=ps.findHot();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//鐑棬鍟嗗搧
		
		//灏嗕咯涓猯ist鏀惧叆鍩熶腑
		request.setAttribute("nList", newList);
		request.setAttribute("hList", hotList);
		
		return "/jsp/index.jsp";
	}

}