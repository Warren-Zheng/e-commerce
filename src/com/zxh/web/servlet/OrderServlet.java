package com.zxh.web.servlet;

import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxh.domain.Cart;
import com.zxh.domain.CartItem;
import com.zxh.domain.Order;
import com.zxh.domain.OrderItem;
import com.zxh.domain.PageBean;
import com.zxh.domain.User;
import com.zxh.service.OrderService;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.PaymentUtil;
import com.zxh.utils.UUIDUtils;

/**
 * 璁㈠崟妯″潡
 */
public class OrderServlet extends BaseServlet {

	/**
	 * 鐢熸垚璁㈠崟
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//0.鍒ゆ柇鐢ㄦ埛鏄惁鐧诲綍
		User user=(User) request.getSession().getAttribute("user");
		if(user == null){
			request.setAttribute("msg", "璇峰厛鐧诲綍~~");
			return "/jsp/msg.jsp";
		}
		
		//1.灏佽鏁版嵁
		Order order=new Order();
		//1.1 璁㈠崟id
		order.setOid(UUIDUtils.getId());
		
		//1.2 璁㈠崟鏃堕棿
		order.setOrdertime(new Date());
		
		//1.3 鎬婚噾棰�
		//鑾峰彇session涓璫art
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		
		order.setTotal(cart.getTotal());
		
		//1.4 璁㈠崟鐨勬墍鏈夎鍗曢」
		/*
		 * 鍏堣幏鍙朿art涓璱tmes
		 * 閬嶅巻itmes 缁勮鎴恛rderItem
		 * 灏唎rderItem娣诲姞鍒發ist(items)涓�
		 */
		for (CartItem cartItem : cart.getItmes()) {
			OrderItem oi = new OrderItem();
			
			//璁剧疆id
			oi.setItemid(UUIDUtils.getId());
			
			//璁剧疆璐拱鏁伴噺
			oi.setCount(cartItem.getCount());
			
			//璁剧疆灏忚
			oi.setSubtotal(cartItem.getSubtotal());
			
			//璁剧疆product
			oi.setProduct(cartItem.getProduct());
			
			//璁剧疆order
			oi.setOrder(order);
			
			//娣诲姞鍒發ist涓�
			order.getItems().add(oi);
		}
		
		//1.5 璁剧疆鐢ㄦ埛
		order.setUser(user);
		
		//2.璋冪敤service 娣诲姞璁㈠崟
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		os.add(order);
		
		//3.灏唎rder鏀惧叆request鍩熶腑,璇锋眰杞彂
		request.setAttribute("bean", order);
		
		//4.娓呯┖璐墿杞�
		request.getSession().removeAttribute("cart");
		return "/jsp/order_info.jsp";
	}

	/**
	 * 鍒嗛〉鏌ヨ鎴戠殑璁㈠崟
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String findAllByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇褰撳墠椤�
		int currPage=Integer.parseInt(request.getParameter("currPage"));
		int pageSize=3;
		
		//2.鑾峰彇鐢ㄦ埛
		User user=(User) request.getSession().getAttribute("user");
		if(user == null){
			request.setAttribute("msg", "浣犺繕娌℃湁鐧诲綍,璇风櫥褰�!");
			return "/jsp/msg.jsp";
		}
		
		//3.璋冪敤service 鍒嗛〉鏌ヨ  鍙傛暟:currpage pagesize user  杩斿洖鍊�:PageBean
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		PageBean<Order> bean=os.findAllByPage(currPage,pageSize,user);
		
		//4.灏哖ageBean鏀惧叆request鍩熶腑
		request.setAttribute("pb", bean);
		
		return "/jsp/order_list.jsp";
	}
	
	public String getById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇oid
		String oid = request.getParameter("oid");
		
		//2.璋冪敤service 閫氳繃oid 杩斿洖鍊�:order
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		Order order=os.getById(oid);
		
		//3.灏唎rder鏀惧叆request鍩熶腑
		request.setAttribute("bean", order);
		
		return "/jsp/order_info.jsp";
	}
	
	public String pay(HttpServletRequest request,HttpServletResponse respone) throws Exception{
		//鎺ュ彈鍙傛暟
		String address=request.getParameter("address");
		String name=request.getParameter("name");
		String telephone=request.getParameter("telephone");
		String oid=request.getParameter("oid");
		
		
		//閫氳繃id鑾峰彇order
		OrderService s=(OrderService) BeanFactory.getBean("OrderService");
		Order order = s.getById(oid);
		
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		
		//鏇存柊order
		s.update(order);
		

		// 缁勭粐鍙戦�佹敮浠樺叕鍙搁渶瑕佸摢浜涙暟鎹�
		String pd_FrpId = request.getParameter("pd_FrpId");
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = oid;
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 鏀粯鎴愬姛鍥炶皟鍦板潃 ---- 绗笁鏂规敮浠樺叕鍙镐細璁块棶銆佺敤鎴疯闂�
		// 绗笁鏂规敮浠樺彲浠ヨ闂綉鍧�
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("responseURL");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 鍔犲瘑hmac 闇�瑕佸瘑閽�
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
	
		
		//鍙戦�佺粰绗笁鏂�
		StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=").append(p0_Cmd).append("&");
		sb.append("p1_MerId=").append(p1_MerId).append("&");
		sb.append("p2_Order=").append(p2_Order).append("&");
		sb.append("p3_Amt=").append(p3_Amt).append("&");
		sb.append("p4_Cur=").append(p4_Cur).append("&");
		sb.append("p5_Pid=").append(p5_Pid).append("&");
		sb.append("p6_Pcat=").append(p6_Pcat).append("&");
		sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		sb.append("p8_Url=").append(p8_Url).append("&");
		sb.append("p9_SAF=").append(p9_SAF).append("&");
		sb.append("pa_MP=").append(pa_MP).append("&");
		sb.append("pd_FrpId=").append(pd_FrpId).append("&");
		sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sb.append("hmac=").append(hmac);
		
		respone.sendRedirect(sb.toString());
		
		return null;
	}
	/**
	 * 鏀粯鎴愬姛涔嬪悗鐨勫洖璋�
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String callback(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");
		// 韬唤鏍￠獙 --- 鍒ゆ柇鏄笉鏄敮浠樺叕鍙搁�氱煡浣�
		String hmac = request.getParameter("hmac");
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");

		// 鑷繁瀵逛笂闈㈡暟鎹繘琛屽姞瀵� --- 姣旇緝鏀粯鍏徃鍙戣繃鏉amc
		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if (isValid) {
			// 鍝嶅簲鏁版嵁鏈夋晥
			if (r9_BType.equals("1")) {
				// 娴忚鍣ㄩ噸瀹氬悜
				System.out.println("111");
				request.setAttribute("msg", "鎮ㄧ殑璁㈠崟鍙蜂负:"+r6_Order+",閲戦涓�:"+r3_Amt+"宸茬粡鏀粯鎴愬姛,绛夊緟鍙戣揣~~");
				
			} else if (r9_BType.equals("2")) {
				// 鏈嶅姟鍣ㄧ偣瀵圭偣 --- 鏀粯鍏徃閫氱煡浣�
				System.out.println("浠樻鎴愬姛锛�222");
				// 淇敼璁㈠崟鐘舵�� 涓哄凡浠樻
				// 鍥炲鏀粯鍏徃
				response.getWriter().print("success");
			}
			
			//淇敼璁㈠崟鐘舵��
			OrderService s=(OrderService) BeanFactory.getBean("OrderService");
			Order order = s.getById(r6_Order);
			
			//淇敼璁㈠崟鐘舵�佷负 宸叉敮浠�
			order.setState(1);
			
			s.update(order);
			
		} else {
			// 鏁版嵁鏃犳晥
			System.out.println("鏁版嵁琚鏀癸紒");
		}
		
		
		return "/jsp/msg.jsp";
		
	}
	
	
	/*
	 * 纭鏀惰幏
	 */
	public String updateState(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//1.鑾峰彇 oid
		String oid = request.getParameter("oid");
		
		//2.璋冪敤service 淇敼璁㈠崟鐘舵��
		OrderService os=(OrderService) BeanFactory.getBean("OrderService");
		Order order = os.getById(oid);
		order.setState(3);
		os.update(order);
		
		//3.閲嶅畾鍚�
		response.sendRedirect(request.getContextPath()+"/order?method=findAllByPage&currPage=1");
		return null;
	}
}
