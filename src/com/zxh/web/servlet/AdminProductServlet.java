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
import com.zxh.utils.BeanFactory;

/**
 *鍚庡彴鍟嗗搧绠＄悊
 */
public class AdminProductServlet extends BaseServlet {
	
	/**
	 * 鏌ヨ鎵�鏈夊晢鍝�
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.璋冪敤service 鏌ヨ鎵�鏈� 杩斿洖涓�涓猯ist
		ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
		List<Product> list=ps.findAll();
		
		//2.灏唋ist鏀惧叆request鍩熶腑 璇锋眰杞彂
		request.setAttribute("list", list);
		
		return "/admin/product/list.jsp";
	}
	
	/**
	 * 璺宠浆鍒版坊鍔犲晢鍝佺殑椤甸潰
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//鏌ヨ鎵�鏈夌殑鍒嗙被 杩斿洖list
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		List<Category> clist = cs.findAll();
		
		//灏唋ist鏀惧叆request
		request.setAttribute("clist", clist);
		return "/admin/product/add.jsp";
	}

}
