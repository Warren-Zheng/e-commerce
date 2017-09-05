package com.zxh.web.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import com.zxh.constant.Constant;
import com.zxh.domain.User;
import com.zxh.myconventer.MyConventer;
import com.zxh.service.UserService;
import com.zxh.service.impl.UserServiceImpl;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.MD5Utils;
import com.zxh.utils.UUIDUtils;

/**
 * 鍜岀敤鎴风浉鍏崇殑servlet
 */
public class UserServlet extends BaseServlet {

	/**
	 * 璺宠浆鍒� 娉ㄥ唽椤甸潰
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String registUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "/jsp/register.jsp";
	}
	
	/**
	 * 鐢ㄦ埛娉ㄥ唽
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.灏佽鏁版嵁
		User user = new User();
		
		//娉ㄥ唽鑷畾涔夎浆鍖栧櫒
		ConvertUtils.register(new MyConventer(), Date.class);
		BeanUtils.populate(user, request.getParameterMap());
		
		//1.1 璁剧疆鐢ㄦ埛id
		user.setUid(UUIDUtils.getId());
		
		//1.2 璁剧疆婵�娲荤爜
		user.setCode(UUIDUtils.getCode());
		
		//1.3鍔犲瘑瀵嗙爜
		user.setPassword(MD5Utils.md5(user.getPassword()));
		
		//2.璋冪敤service瀹屾垚娉ㄥ唽
		UserService s=(UserService) BeanFactory.getBean("UserService");
		s.regist(user);
		
		//3.椤甸潰璇锋眰杞彂
		request.setAttribute("msg", "鐢ㄦ埛娉ㄥ唽宸叉垚鍔�,璇峰幓閭婵�娲粇~");
		
		return "/jsp/msg.jsp";
	}
	
	/**
	 * 鐢ㄦ埛婵�娲�
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String active(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇婵�娲荤爜
		String code = request.getParameter("code");
		
		//2.璋冪敤service瀹屾垚婵�娲�
		UserService s=(UserService) BeanFactory.getBean("UserService");
		User user=s.active(code);
		
		if(user==null){
			//閫氳繃婵�娲荤爜娌℃湁鎵惧埌鐢ㄦ埛
			request.setAttribute("msg", "璇烽噸鏂版縺娲�");
		}else{
			//娣诲姞淇℃伅
			request.setAttribute("msg", "婵�娲绘垚鍔�");
		}
		//3.璇锋眰杞彂鍒癿sg.jsp
		
		return "/jsp/msg.jsp";
	}
	
	/**
	 * 璺宠浆鍒扮櫥褰曢〉闈�
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loginUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return "/jsp/login.jsp";
	}
	
	/**
	 * 鐧诲綍
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//1.鑾峰彇鐢ㄦ埛鍚嶅拰瀵嗙爜
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		password=MD5Utils.md5(password);
		
		//2.璋冪敤serive瀹屾垚鐧诲綍鎿嶄綔 杩斿洖user
		UserService s=(UserService) BeanFactory.getBean("UserService");
		User user=s.login(username,password);
		
		//3.鍒ゆ柇鐢ㄦ埛
		if(user==null){
			//鐢ㄦ埛鍚嶅瘑鐮佷笉鍖归厤
			request.setAttribute("msg", "鐢ㄦ埛鍚嶅瘑鐮佷笉鍖归厤");
			return "/jsp/login.jsp";
		}else{
			//缁х画鍒ゆ柇鐢ㄦ埛鐨勭姸鎬佹槸鍚︽縺娲�
			if(Constant.USER_IS_ACTIVE!=user.getState()){
				request.setAttribute("msg", "鐢ㄦ埛鏈縺娲�");
				return "/jsp/login.jsp";
			}
		}
		
		//4.灏唘ser鏀惧叆session涓� 閲嶅畾鍚�
		request.getSession().setAttribute("user", user);
		response.sendRedirect(request.getContextPath()+"/");//  /store
		
		return null;
	}

	
	public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//骞叉帀session
		request.getSession().invalidate();
		
		//閲嶅畾鍚�
		response.sendRedirect(request.getContextPath());
		
		//澶勭悊鑷姩鐧诲綍
		
		return null;
	}
}
