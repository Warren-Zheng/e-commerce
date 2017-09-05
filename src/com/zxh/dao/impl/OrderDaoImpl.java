package com.zxh.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zxh.dao.OrderDao;
import com.zxh.domain.Order;
import com.zxh.domain.OrderItem;
import com.zxh.domain.Product;
import com.zxh.utils.DataSourceUtils;

public class OrderDaoImpl implements OrderDao{

	/**
	 * 娣诲姞涓�鏉¤鍗�
	 */
	@Override
	public void add(Order order) throws Exception {
		QueryRunner qr = new QueryRunner();
		
		/*
		 * `oid` varchar(32) NOT NULL,
		  `ordertime` datetime DEFAULT NULL,
		  `total` double DEFAULT NULL,
		  
		  `state` int(11) DEFAULT NULL,
		  `address` varchar(30) DEFAULT NULL,
		  `name` varchar(20) DEFAULT NULL,
		  
		  `telephone` varchar(20) DEFAULT NULL,
		  `uid` varchar(32) DEFAULT NULL,
		 */
		String sql="insert into orders values(?,?,?,?,?,?,?,?)";
		qr.update(DataSourceUtils.getConnection(),sql, order.getOid(),order.getOrdertime(),order.getTotal(),order.getState(),
				order.getAddress(),order.getName(),order.getTelephone(),order.getUser().getUid());
	}

	/**
	 * 娣诲姞涓�鏉¤鍗曢」
	 */
	@Override
	public void addItem(OrderItem oi) throws Exception {
		QueryRunner qr = new QueryRunner();
		 /**
		 * `itemid` varchar(32) NOT NULL,
		  `count` int(11) DEFAULT NULL,
		  `subtotal` double DEFAULT NULL,
		  `pid` varchar(32) DEFAULT NULL,
		  `oid` varchar(32) DEFAULT NULL,
		 */
		String sql="insert into orderitem values(?,?,?,?,?)";
		qr.update(DataSourceUtils.getConnection(),sql, oi.getItemid(),oi.getCount(),oi.getSubtotal(),oi.getProduct().getPid(),oi.getOrder().getOid());
	}

	/**
	 * 鏌ヨ鎴戠殑璁㈠崟 鍒嗛〉
	 */
	@Override
	public List<Order> findAllByPage(int currPage, int pageSize, String uid) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from orders where uid = ? order by ordertime desc limit ? , ?";
		List<Order> list = qr.query(sql, new BeanListHandler<>(Order.class), uid,(currPage-1)*pageSize,pageSize);
		
		//閬嶅巻璁㈠崟闆嗗悎 灏佽姣忎釜璁㈠崟鐨勮鍗曢」鍒楄〃
		sql="select * from orderitem oi,product p where oi.pid=p.pid and oi.oid = ?";
		for (Order order : list) {
			//褰撳墠璁㈠崟鍖呭惈鐨勬墍鏈夊唴瀹�
			List<Map<String, Object>> mList = qr.query(sql, new MapListHandler(), order.getOid());
			//map鐨刱ey:瀛楁鍚�  value:瀛楁鍊�
			for (Map<String, Object> map : mList) {
				//灏佽product
				Product p=new Product();
				BeanUtils.populate(p, map);
				
				//灏佽orderItem
				OrderItem oi = new OrderItem();
				BeanUtils.populate(oi, map);
				
				oi.setProduct(p);
				
				//灏唎rderItem瀵硅薄娣诲姞鍒板搴旂殑order瀵硅薄鐨刲ist闆嗗悎涓�
				order.getItems().add(oi);
			}
		}
		return list;
	}

	/**
	 * 鑾峰彇鎴戠殑璁㈠崟鐨勬�绘潯鏁�
	 */
	@Override
	public int getTotalCount(String uid) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select count(*) from orders where uid = ?";
		return ((Long)qr.query(sql, new ScalarHandler(), uid)).intValue();
	}

	/**
	 * 閫氳繃oid 鏌ヨ璁㈠崟璇︽儏
	 */
	@Override
	public Order getById(String oid) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from orders where oid = ?";
		Order order = qr.query(sql, new BeanHandler<>(Order.class), oid);
		
		//灏佽orderitems
		sql="select * from orderitem oi,product p where oi.pid = p.pid and oi.oid = ?";
		List<Map<String, Object>> query = qr.query(sql, new MapListHandler(), oid);
		for (Map<String, Object> map : query) {
			//灏佽product
			Product product = new Product();
			BeanUtils.populate(product, map);
			
			//灏佽orderitem
			OrderItem oi = new OrderItem();
			BeanUtils.populate(oi, map);
			oi.setProduct(product);
			
			//灏唎rderitem鍋囧涓璷rder鐨刬tems涓�
			order.getItems().add(oi);
		}
		return order;
	}

	/**
	 * 淇敼璁㈠崟
	 */
	@Override
	public void update(Order order) throws Exception {
		/*
		  `state` int(11) DEFAULT NULL,
		  `address` varchar(30) DEFAULT NULL,
		  `name` varchar(20) DEFAULT NULL,
		  
		  `telephone` varchar(20) DEFAULT NULL,
		  `uid` varchar(32) DEFAULT NULL,
		 */
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update orders set state=?,address=?,name=?,telephone=? where oid=?";
		qr.update(sql, order.getState(),order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
	}

	@Override
	public List<Order> findAllByState(String state) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from orders where 1=1 ";
		if(state!=null && state.trim().length()>0){
			sql += "and state = ? order by ordertime desc";
			return qr.query(sql,new BeanListHandler<>(Order.class),state);
		}
		sql+=" order by ordertime desc";
		return qr.query(sql, new BeanListHandler<>(Order.class));
	}

}
