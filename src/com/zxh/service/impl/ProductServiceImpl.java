package com.zxh.service.impl;

import java.util.List;

import com.zxh.dao.ProductDao;
import com.zxh.dao.impl.ProductDaoImpl;
import com.zxh.domain.PageBean;
import com.zxh.domain.Product;
import com.zxh.service.ProductService;
import com.zxh.utils.BeanFactory;

public class ProductServiceImpl implements ProductService{

	/**
	 * 鏌ヨ鏈�鏂�
	 */
	@Override
	public List<Product> findNew() throws Exception {
		ProductDao pdao=(ProductDao) BeanFactory.getBean("ProductDao");
		return pdao.findNew();
	}

	/**
	 * 鏌ヨ鐑棬
	 */
	@Override
	public List<Product> findHot() throws Exception {
		ProductDao pdao=(ProductDao) BeanFactory.getBean("ProductDao");
		return pdao.findHot();
	}

	/**
	 * 鏌ヨ鍗曚釜鍟嗗搧
	 */
	@Override
	public Product getByPid(String pid) throws Exception {
		ProductDao pdao=(ProductDao) BeanFactory.getBean("ProductDao");
		return pdao.getByPid(pid);
	}

	/**
	 * 鎸夌被鍒垎椤垫煡璇㈠晢鍝�
	 */
	@Override
	public PageBean<Product> findByPage(int currPage, int pageSize, String cid) throws Exception {
		ProductDao pdao=(ProductDao) BeanFactory.getBean("ProductDao");
		//褰撳墠椤垫暟鎹�
		List<Product> list=pdao.findByPage(currPage,pageSize,cid);
		
		//鎬绘潯鏁�
		int totalCount = pdao.getTotalCount(cid);
		
		return new PageBean<>(list, currPage, pageSize, totalCount);
	}

	/**
	 * 鏌ヨ鎵�鏈�
	 */
	@Override
	public List<Product> findAll() throws Exception {
		ProductDao pdao=(ProductDao) BeanFactory.getBean("ProductDao");
		
		return pdao.findAll();
	}

	/**
	 * 娣诲姞鍟嗗搧
	 */
	@Override
	public void add(Product p) throws Exception {
		ProductDao pdao=(ProductDao) BeanFactory.getBean("ProductDao");
		pdao.add(p);
	}

}
