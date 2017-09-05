package com.zxh.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxh.domain.Cart;
import com.zxh.domain.CartItem;
import com.zxh.domain.Product;
import com.zxh.service.ProductService;
import com.zxh.utils.BeanFactory;

/**
 * 璐墿杞︽ā鍧�
 */
public class CartServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 鑾峰彇璐墿杞�
	 * @param request
	 * @return
	 */
	public Cart getCart(HttpServletRequest request){
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		//鍒ゆ柇璐墿杞︽槸鍚︿负绌�
		if(cart == null){
			//鍒涘缓涓�涓猚art
			cart=new Cart();
			
			//娣诲姞鍒皊ession涓�
			request.getSession().setAttribute("cart", cart);
		}
		return cart;
	}
	
	/**
	 * 娣诲姞鍒拌喘鐗╄溅
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇pid鍜屾暟閲�
		String pid = request.getParameter("pid");
		int count = Integer.parseInt(request.getParameter("count"));
		
		//2.璋冪敤productservice 閫氳繃pid鑾峰彇涓�涓晢鍝�
		ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
		Product product = ps.getByPid(pid);
		
		//3.缁勮鎴怌artItem
		CartItem cartItem = new CartItem(product, count);
		
		//4.娣诲姞鍒拌喘鐗╄溅
		Cart cart = getCart(request);
		cart.add2Cart(cartItem);
		
		//5.閲嶅畾鍚�
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
	
	/**
	 * 浠庤喘鐗╄溅涓Щ闄よ喘鐗╄溅椤�
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇鍟嗗搧鐨刾id
		String pid = request.getParameter("pid");
		
		//2.璋冪敤璐墿杞︾殑remove鏂规硶
		getCart(request).removeFromCart(pid);
		
		//3.閲嶅畾鍚�
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
	
	/**
	 * 娓呯┖璐墿杞�
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String clear(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//鑾峰彇璐墿杞� 娓呯┖
		getCart(request).clearCart();
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}

}
