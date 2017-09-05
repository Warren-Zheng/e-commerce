package com.zxh.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zxh.domain.User;

public class PrivilegeFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		//1.寮鸿浆
		HttpServletRequest request=(HttpServletRequest) req;
		HttpServletResponse response=(HttpServletResponse) resp;
		
		//2.涓氬姟閫昏緫
		//浠巗ession涓幏鍙杣ser 鍒ゆ柇user鏄惁涓虹┖ 鑻ヤ负绌� 璇锋眰杞彂
		User user=(User) request.getSession().getAttribute("user");
		if(user==null){
			request.setAttribute("msg", "娌℃湁鏉冮檺,璇峰厛鐧诲綍!");
			request.getRequestDispatcher("/jsp/msg.jsp").forward(request, response);
			return;
		}
		
		//3.鏀捐
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
