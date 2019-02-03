package com.revature.project_1.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.project_1.model.LoginModel;
import com.revature.project_1.service.ExpenseReimbursementRequestService;

@WebServlet(urlPatterns="/employee-reimbursement-request")
public class EmployeeReimbursementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pending = req.getParameter("pending");
		try (PrintWriter writer = resp.getWriter()) {
			if (req.getSession(false) == null)
				return;
			LoginModel login = (LoginModel) req.getSession(false).getAttribute(EmployeeLoginServlet.LOGIN_ATTR);
			int employeeId = login.getId();
			ExpenseReimbursementRequestService service = new ExpenseReimbursementRequestService();
			if (pending == null)
				service.writeFetchedReimbursementsByEmployeeId(writer, employeeId);
			else {
				boolean bPending = Boolean.parseBoolean(pending);
				service.writeFetchedReimbursementsByEmployeeId(writer, employeeId, bPending);
			}
		} finally {}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		 if (session != null) {
			 LoginModel employeeLogin = (LoginModel) session.getAttribute(EmployeeLoginServlet.LOGIN_ATTR);
			 int employeeId = employeeLogin.getId();
			try (Reader src = req.getReader(); PrintWriter writer = resp.getWriter();){
				new ExpenseReimbursementRequestService().createReimbursementRequest(src, writer, employeeId);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {}
		 }
	}
}
