package com.huihui.test;

import com.huihui.domain.User;
import com.huihui.util.HibernateUtil;

public class Test {
	
	public static void main(String[] args) {
		User user = new User();
		user.setName("xiaohuihui");
		user.setPassword("123");
		user.setAge(23);
		user.setSex("ÄÐ");
		HibernateUtil.save(user);
	}

}
