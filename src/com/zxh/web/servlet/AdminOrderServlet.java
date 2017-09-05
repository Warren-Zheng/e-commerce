package com.zxh.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxh.domain.Order;
import com.zxh.domain.OrderItem;
import com.zxh.service.OrderService;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * 鍚庡彴璁㈠崟妯″潡
 */
public class AdminOrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 *	鏌ヨ璁㈠崟
	 * @throws Exception 
	 */
	public  String findAllByState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鎺ュ彈state
		String state=request.getParameter("state");
		
		//2.璋冪敤service
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		List<Order> list=os.findAllByState(state);
		
		//3.灏唋ist鏀惧叆鍩熶腑 璇锋眰杞彂
		request.setAttribute("list", list);
		return "/admin/order/list.jsp";
	}
	
	/**
	 * 鏌ヨ璁㈠崟璇︽儏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public  String getDetailByOid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		
		//1.鎺ュ彈oid
		String oid = request.getParameter("oid");
		
		
		//2.璋冪敤serivce鏌ヨ璁㈠崟璇︽儏 杩斿洖鍊� list<OrderItem>
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		List<OrderItem> items = os.getById(oid).getItems();
		
		
		//3.灏唋ist杞垚json 鍐欏洖
		//鎺掗櫎涓嶇敤鍐欏洖鍘荤殑鏁版嵁
		JsonConfig config = JsonUtil.configJson(new String[]{"class","itemid","order"});
		JSONArray json = JSONArray.fromObject(items,config);
		//System.out.println(json);
		response.getWriter().println(json);
		return null;
	}	
	
	/*
	 * 淇敼璁㈠崟鐘舵��
	 */
	public  String updateState(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鎺ュ彈 oid state
		String oid = request.getParameter("oid");
		String state = request.getParameter("state");
		
		//2.璋冪敤service 
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		Order order = os.getById(oid);		
		order.setState(2);
		
		os.update(order);
		
		//3.椤甸潰閲嶅畾鍚�
		response.sendRedirect(request.getContextPath()+"/adminOrder?method=findAllByState&state=1");
		return null;
	}	
}
