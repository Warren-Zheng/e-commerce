package com.zxh.service.impl;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import com.zxh.dao.CategoryDao;
import com.zxh.dao.ProductDao;
import com.zxh.dao.impl.CategoryDaoImpl;
import com.zxh.domain.Category;
import com.zxh.service.CategoryService;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.DataSourceUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CategoryServiceImpl implements CategoryService {

	/**
	 * 鏌ヨ鎵�鏈夌殑鍒嗙被
	 */
	@Override
	public List<Category> findAll() throws Exception {
		// 1.鍒涘缓缂撳瓨绠＄悊鍣�
		CacheManager cm = CacheManager
				.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml"));

		// 2.鑾峰彇鎸囧畾鐨勭紦瀛�
		Cache cache = cm.getCache("categoryCache");

		// 3.閫氳繃缂撳瓨鑾峰彇鏁版嵁 灏哻ache鐪嬫垚涓�涓猰ap鍗冲彲
		Element element = cache.get("clist");

		List<Category> list = null;

		// 4.鍒ゆ柇鏁版嵁
		if (element == null) {
			// 浠庢暟鎹簱涓幏鍙�
			CategoryDao cd = (CategoryDao) BeanFactory.getBean("CategoryDao");
			list = cd.findAll();

			// 灏唋ist鏀惧叆缂撳瓨
			cache.put(new Element("clist", list));

			System.out.println("缂撳瓨涓病鏈夋暟鎹�,宸插幓鏁版嵁搴撲腑鑾峰彇");
		} else {
			// 鐩存帴杩斿洖
			list = (List<Category>) element.getObjectValue();

			System.out.println("缂撳瓨涓湁鏁版嵁");
		}

		return list;
	}

	/**
	 * 娣诲姞鍒嗙被
	 */
	@Override
	public void add(Category c) throws Exception {
		// 鏆傛椂 鑾峰彇dao
		CategoryDao cd = (CategoryDao) BeanFactory.getBean("CategoryDao");
		cd.add(c);

		// 鏇存柊缂撳瓨
		// 1.鍒涘缓缂撳瓨绠＄悊鍣�
		CacheManager cm = CacheManager
				.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml"));

		// 2.鑾峰彇鎸囧畾鐨勭紦瀛�
		Cache cache = cm.getCache("categoryCache");
		
		//3.娓呯┖
		cache.remove("clist");
	}

	/*
	 * 閫氳繃cid鑾峰彇涓�涓垎绫诲璞�
	 */
	@Override
	public Category getById(String cid) throws Exception {
		CategoryDao cd = (CategoryDao) BeanFactory.getBean("CategoryDao");
		return cd.getById(cid);
	}

	/**
	 * 鏇存柊鍒嗙被
	 */
	@Override
	public void update(Category c) throws Exception {
		//1.璋冪敤dao鏇存柊
		CategoryDao cd = (CategoryDao) BeanFactory.getBean("CategoryDao");
		cd.update(c);
		
		//2.娓呯┖缂撳瓨
		CacheManager cm = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml"));
		Cache cache = cm.getCache("categoryCache");
		cache.remove("clist");
	}

	@Override
	public void delete(String cid) throws Exception{
		try {
			//1.寮�鍚簨鍔�
			DataSourceUtils.startTransaction();

			//2.鏇存柊鍟嗗搧
			ProductDao pd=(ProductDao) BeanFactory.getBean("ProductDao");
			pd.updateCid(cid);
			
			//3.鍒犻櫎鍒嗙被
			CategoryDao cd=(CategoryDao) BeanFactory.getBean("CategoryDao");
			cd.delete(cid);
			
			//4.浜嬪姟鎺у埗
			DataSourceUtils.commitAndClose();
			
			//5.娓呯┖缂撳瓨
			CacheManager cm = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml"));
			Cache cache = cm.getCache("categoryCache");
			cache.remove("clist");
		} catch (Exception e) {
			e.printStackTrace();
			DataSourceUtils.rollbackAndClose();
			throw e;
		}
		
	}

}
