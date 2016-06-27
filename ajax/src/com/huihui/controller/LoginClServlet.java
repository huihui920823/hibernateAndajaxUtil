package com.huihui.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huihui.domain.User;
import com.huihui.service.UserService;

public class LoginClServlet extends HttpServlet {

	public LoginClServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		// response.setContentType("text/xml;charset=utf-8");
		PrintWriter out = response.getWriter();

		String username = new String(request.getParameter("username").getBytes(
				"iso-8859-1"), "utf-8");
		System.out.println(username);
		UserService userService = new UserService();
		if (userService.checkHaveUser(username)) {
			// out.print("<res><mes>抱歉，用户名已经存在</mes></res>");
			out.print("{'res':'抱歉，用户名已经存在'}");
		} else {
			//out.print("<res><mes>该用户名可以使用</mes></res>");
			out.print("{'res':'该用户名可以使用'}");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

	public void init() throws ServletException {

	}

}
