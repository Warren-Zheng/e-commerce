package com.zxh.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxh.domain.Category;
import com.zxh.service.CategoryService;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.JsonUtil;

/**
 * Servlet implementation class CategoryServlet
 */
public class CategoryServlet extends BaseServlet {

	/**
	 * 鏌ヨ鎵�鏈夌殑鍒嗙被
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.璋冪敤categoryservice 鏌ヨ鎵�鏈夌殑鍒嗙被 杩斿洖鍊糽ist
		CategoryService cs = (CategoryService) BeanFactory.getBean("CategoryService");
		List<Category> clist = null;
		try {
			clist = cs.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 2.灏嗚繑鍥炲�艰浆鎴恓son鏍煎紡 杩斿洖鍒伴〉闈笂
		//request.setAttribute("clist", clist);
		String json = JsonUtil.list2json(clist);
		
		//3.鍐欏洖鍘�
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(json);
		
		return null;
	}

}
