package com.zxh.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxh.domain.Category;
import com.zxh.service.CategoryService;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.UUIDUtils;

/**
 * 鍚庡彴鍒嗙被绠＄悊
 */
public class AdminCategoryServlet extends BaseServlet {

	/**
	 * 灞曠ず鎵�鏈夊垎绫�
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.璋冪敤categoryservice 鏌ヨ鎵�鏈夌殑鍒嗙被淇℃伅 杩斿洖鍊� list
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		List<Category> list = cs.findAll();
		
		//2.灏唋ist鏀惧叆request鍩熶腑 璇锋眰杞彂鍗冲彲
		request.setAttribute("list", list);
		return "/admin/category/list.jsp";
	}
	
	/**
	 * 璺宠浆鍒版坊鍔犻〉闈笂
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return "/admin/category/add.jsp";
	}
	
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鎺ュ彈cname
		String cname = request.getParameter("cname");
		
		//2.灏佽category
		Category c = new Category();
		c.setCid(UUIDUtils.getId());
		c.setCname(cname);
		
		//3.璋冪敤service瀹屾垚 娣诲姞鎿嶄綔
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		cs.add(c);
		
		//4.閲嶅畾鍚� 鏌ヨ鎵�鏈夊垎绫�
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		
		
		return null;
	}
	
	/**
	 * 閫氳繃id鑾峰彇鍒嗙被淇℃伅
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鎺ュ彈cid
		String cid = request.getParameter("cid");
		
		//2 璋冪敤service瀹屾垚 鏌ヨ鎿嶄綔 杩斿洖鍊�:category
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		Category c=cs.getById(cid);
		
		//3.灏哻ategory鏀惧叆request鍩熶腑, 璇锋眰杞彂  /admin/category/edit.jsp
		request.setAttribute("bean", c);
		return "/admin/category/edit.jsp";
	}
	
	/**
	 * 鏇存柊鍒嗙被淇℃伅鏂规硶
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇cid cname
		//2.灏佽鍙傛暟
		Category c=new Category();
		c.setCid(request.getParameter("cid"));
		c.setCname(request.getParameter("cname"));
		
		//3.璋冪敤service 瀹屾垚鏇存柊鎿嶄綔
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		cs.update(c);
		
		//4.閲嶅畾鍚� 鏌ヨ鎵�鏈�
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		
		return null;
	}
	
	/**
	 * 鍒犻櫎鍒嗙被
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇cid
		String cid = request.getParameter("cid");
		
		//2.璋冪敤service 瀹屾垚鍒犻櫎
		CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");
		cs.delete(cid);
		
		//3.閲嶅畾鍚�
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		return null;
	}

}
