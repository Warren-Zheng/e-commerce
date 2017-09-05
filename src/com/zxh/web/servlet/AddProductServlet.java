package com.zxh.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.zxh.domain.Category;
import com.zxh.domain.Product;
import com.zxh.service.ProductService;
import com.zxh.utils.BeanFactory;
import com.zxh.utils.UUIDUtils;
import com.zxh.utils.UploadUtils;

/**
 * Servlet implementation class AddProduct
 */
public class AddProductServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			//0.鍒涘缓map 鏀惧叆鍓嶅彴浼犻�掔殑鏁版嵁
			HashMap<String, Object> map = new HashMap<>();
			
			//鍒涘缓纾佺洏鏂囦欢椤�
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//鍒涘缓鏍稿績涓婁紶瀵硅薄
			ServletFileUpload upload = new ServletFileUpload(factory);
			//瑙ｆ瀽request
			List<FileItem> list = upload.parseRequest(request);
			//閬嶅巻闆嗗悎
			for (FileItem fi : list) {
				//鍒ゆ柇鏄惁鏄櫘閫氱殑涓婁紶缁勪欢
				if(fi.isFormField()){
					//鏅�氫笂浼犵粍浠�
					map.put(fi.getFieldName(),fi.getString("utf-8"));
				}else{
					//鏂囦欢涓婁紶缁勪欢
					//鑾峰彇鏂囦欢鍚嶇О
					String name = fi.getName();
					
					//鑾峰彇鏂囦欢鐨勭湡瀹炲悕绉�    xxxx.xx
					String realName = UploadUtils.getRealName(name);
					//鑾峰彇鏂囦欢鐨勯殢鏈哄悕绉�
					String uuidName = UploadUtils.getUUIDName(realName);
					
					//鑾峰彇鏂囦欢鐨勫瓨鏀捐矾寰�
					String path = this.getServletContext().getRealPath("/products/1");
					
					//鑾峰彇鏂囦欢娴�
					InputStream is = fi.getInputStream();
					//淇濆瓨鍥剧墖
					FileOutputStream os = new FileOutputStream(new File(path, uuidName));
					
					IOUtils.copy(is, os);
					os.close();
					is.close();
					
					//鍒犻櫎涓存椂鏂囦欢
					fi.delete();
					
					//鍦╩ap涓缃枃浠剁殑璺緞
					map.put(fi.getFieldName(), "products/1/"+uuidName);
					
				}
				
			}
			
			
			
			//1.灏佽鍙傛暟
			Product p = new Product();
			BeanUtils.populate(p, map);
			
			//1.1 鍟嗗搧id
			p.setPid(UUIDUtils.getId());
			
			//1.2 鍟嗗搧鏃堕棿
			p.setPdate(new Date());
			
			//1.3 灏佽cateogry
			Category c = new Category();
			c.setCid((String)map.get("cid"));
			
			p.setCategory(c);
			
			//2.璋冪敤service瀹屾垚娣诲姞
			ProductService ps=(ProductService) BeanFactory.getBean("ProductService");
			ps.add(p);
			
			//3.椤甸潰閲嶅畾鍚�
			response.sendRedirect(request.getContextPath()+"/adminProduct?method=findAll");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "鍟嗗搧娣诲姞澶辫触~");
			request.getRequestDispatcher("/jsp/msg.jsp").forward(request, response);
			return;
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
