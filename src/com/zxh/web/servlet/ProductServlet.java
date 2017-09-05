package com.zxh.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxh.domain.PageBean;
import com.zxh.domain.Product;
import com.zxh.service.ProductService;
import com.zxh.service.impl.ProductServiceImpl;
import com.zxh.utils.BeanFactory;

/**
 * 鍟嗗搧servlet
 */
public class ProductServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 閫氳繃id鏌ヨ鍗曚釜鍟嗗搧璇︽儏
	 * @throws Exception 
	 */
	public String  getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇鍟嗗搧鐨刬d
		String pid=request.getParameter("pid");
		
		//2.璋冪敤service
		//ProductService ps=new ProductServiceImpl();
		ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
		Product p=ps.getByPid(pid);
		
		//3.灏嗙粨鏋滄斁鍏equest涓� 璇锋眰杞彂
		request.setAttribute("bean", p);
		return "/jsp/product_info.jsp";
	}
	
	/**
	 * 鍒嗛〉鏌ヨ鏁版嵁
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String  findByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇绫诲埆 褰撳墠椤�  璁剧疆涓�涓猵agesize
		String cid=request.getParameter("cid");
		int currPage=Integer.parseInt(request.getParameter("currPage"));
		int pageSize=12;
		
		
		//2.璋冪敤service 杩斿洖鍊紁agebean
		ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
		PageBean<Product> bean=ps.findByPage(currPage,pageSize,cid);
		
		//3.灏嗙粨鏋滄斁鍏equest涓� 璇锋眰杞彂
		request.setAttribute("pb", bean);
		return "/jsp/product_list.jsp";
	}

}
