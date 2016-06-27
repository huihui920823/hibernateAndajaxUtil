package com.huihui.service;

import com.huihui.util.HibernateUtil;

public class UserService {
	
	/**
	 * 验证用户是否存在
	 */
	public boolean checkHaveUser(String username){
		boolean flag = false;
		String hql = "from User where name=?";
		String[] parameters = {username}; 
		Object o = HibernateUtil.executeQueryForOne(hql, parameters);
		if(o!=null){
			flag = true;
		}
		return flag;
	}

}
