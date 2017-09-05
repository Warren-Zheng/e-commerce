package com.zxh.service.impl;

import java.util.List;

import com.zxh.dao.OrderDao;
import com.zxh.domain.Order;
import com.zxh.domain.OrderItem;
import com.zxh.domain.PageBean;
import com.zxh.domain.User;
import com.zxh.service.OrderService;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.DataSourceUtils;

public class OrderServiceImpl implements OrderService{

	@Override
	public void add(Order order) throws Exception{
		try {
			//1.寮�鍚簨鍔�
			DataSourceUtils.startTransaction();
			
			OrderDao od=(OrderDao) BeanFactory.getBean("OrderDao");
			//2.鍚憃rders琛ㄤ腑娣诲姞涓�涓暟鎹�
			od.add(order);
			
			//int i=1/0;
			
			//3.鍚憃rderitem涓坊鍔爊鏉℃暟鎹�
			for (OrderItem oi : order.getItems()) {
				od.addItem(oi);
			}
			
			//4.浜嬪姟澶勭悊
			DataSourceUtils.commitAndClose();
		} catch (Exception e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		
	}

	/**
	 * 鍒嗛〉鏌ヨ璁㈠崟
	 */
	@Override
	public PageBean<Order> findAllByPage(int currPage, int pageSize, User user) throws Exception {
		OrderDao od=(OrderDao) BeanFactory.getBean("OrderDao");
		
		// 鏌ヨ褰撳墠椤垫暟鎹�
		List<Order> list=od.findAllByPage(currPage,pageSize,user.getUid());
		
		// 鏌ヨ鎬绘潯鏁�
		int totalCount=od.getTotalCount(user.getUid());
		return new PageBean<>(list, currPage, pageSize, totalCount);
	}

	/**
	 * 鏌ョ湅璁㈠崟璇︽儏
	 */
	@Override
	public Order getById(String oid) throws Exception {
		OrderDao od=(OrderDao) BeanFactory.getBean("OrderDao");
		return od.getById(oid);
	}

	@Override
	public void update(Order order) throws Exception {
		OrderDao od=(OrderDao) BeanFactory.getBean("OrderDao");
		od.update(order);
	}

	/**
	 *鏍规嵁鐘舵�佹煡璇㈣鍗�
	 */
	@Override
	public List<Order> findAllByState(String state) throws Exception {
		OrderDao od=(OrderDao) BeanFactory.getBean("OrderDao");
		return od.findAllByState(state);
	}

}
