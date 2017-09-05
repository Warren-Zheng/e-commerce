package com.zxh.service.impl;

import com.zxh.dao.UserDao;
import com.zxh.dao.impl.UserDaoImpl;
import com.zxh.domain.User;
import com.zxh.service.UserService;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.MailUtils;

public class UserServiceImpl implements UserService{

	/**
	 * 鐢ㄦ埛娉ㄥ唽
	 */
	@Override
	public void regist(User user)  throws Exception{
		UserDao dao=(UserDao) BeanFactory.getBean("UserDao");
		dao.add(user);
		//鍙戦�侀偖浠�
		
		//email:鏀朵欢浜哄湴鍧�
		//emailMsg:閭欢鐨勫唴瀹�
		String emailMsg="娆㈣繋鎮ㄦ敞鍐屾垚鎴戜滑鐨勪竴鍛�,<a href='http://localhost/store/user?method=active&code="+user.getCode()+"'>鐐规婵�娲�</a>";
		MailUtils.sendMail(user.getEmail(), emailMsg);
	}

	/**
	 * 鐢ㄦ埛婵�娲�
	 * @throws Exception 
	 */
	@Override
	public User active(String code) throws Exception {
		UserDao dao=(UserDao) BeanFactory.getBean("UserDao");
		//1.閫氳繃code鑾峰彇涓�涓敤鎴�
		User user=dao.getByCode(code);
		
		//2.鍒ゆ柇鐢ㄦ埛鏄惁涓虹┖
		if(user==null){
			return null;
		}
		
		//3.淇敼鐢ㄦ埛鐘舵��
		//灏嗙敤鎴风殑鐘舵�佽缃负1
		user.setState(1);
		dao.update(user);
		
		return user;
	}

	/**
	 * 鐢ㄦ埛鐧诲綍
	 */
	@Override
	public User login(String username, String password) throws Exception {
		UserDao dao=(UserDao) BeanFactory.getBean("UserDao");
		return dao.getByUsernameAndPwd(username,password);
	}

}
