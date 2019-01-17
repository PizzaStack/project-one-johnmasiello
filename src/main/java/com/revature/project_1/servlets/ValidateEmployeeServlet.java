package com.revature.project_1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.project_1.dao.EmployeeDao;
import com.revature.project_1.model.LoginModel;

@WebServlet(urlPatterns = "/validate-employee")
public class ValidateEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null) {
			resp.getWriter().write("failure");
			return;
		}
		LoginModel model = (LoginModel) session.getAttribute(EmployeeLoginServlet.LOGIN_ATTR);
		if (model != null) {
			if (new EmployeeDao().authenticateLogin(model.getUsername(), model.getPassword()) != null) {
				resp.getWriter().write("success");
				return;
			}
		}
		session.invalidate();
		resp.getWriter().write("failure");
	}

}
