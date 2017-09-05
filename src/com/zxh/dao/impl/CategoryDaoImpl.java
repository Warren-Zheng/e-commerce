package com.zxh.dao.impl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.zxh.dao.CategoryDao;
import com.zxh.domain.Category;
import com.zxh.utils.DataSourceUtils;

public class CategoryDaoImpl implements CategoryDao {

	/**
	 * 鏌ヨ鎵�鏈�
	 */
	@Override
	public List<Category> findAll() throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from category";
		return qr.query(sql, new BeanListHandler<>(Category.class));
	}

	/**
	 * 娣诲姞鍒嗙被
	 */
	@Override
	public void add(Category c) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="insert into category values(?,?)";
		qr.update(sql, c.getCid(),c.getCname());
	}

	/**
	 * 閫氳繃id鑾峰彇涓�涓垎绫�
	 */
	@Override
	public Category getById(String cid) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="select * from category where cid = ? limit 1";
		return qr.query(sql, new BeanHandler<>(Category.class), cid);
	}

	/**
	 * 鏇存柊
	 */
	@Override
	public void update(Category c) throws Exception {
		QueryRunner qr = new QueryRunner(DataSourceUtils.getDataSource());
		String sql="update category set cname = ? where cid = ?";
		qr.update(sql, c.getCname(),c.getCid());
	}

	/**
	 * 鍒犻櫎鍒嗙被
	 */
	@Override
	public void delete(String cid) throws Exception {
		QueryRunner qr = new QueryRunner();
		String sql="delete from category where cid = ?";
		qr.update(DataSourceUtils.getConnection(), sql, cid);
		
	}

}
