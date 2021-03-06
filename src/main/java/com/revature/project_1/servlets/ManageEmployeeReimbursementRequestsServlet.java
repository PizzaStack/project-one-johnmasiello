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

@WebServlet(urlPatterns="/manage-employee-reimbursement-request")
public class ManageEmployeeReimbursementRequestsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String employeeId = req.getParameter("employee_id");
		String pending = req.getParameter("pending");
		
		try (PrintWriter writer = resp.getWriter()) {
			int iEmployeeId = Integer.parseInt(employeeId);
			ExpenseReimbursementRequestService service = new ExpenseReimbursementRequestService();
			if (pending == null)
				service.writeFetchedReimbursementsByEmployeeId(writer, iEmployeeId);
			else {
				boolean bPending = Boolean.parseBoolean(pending);
				service.writeFetchedReimbursementsByEmployeeId(writer, iEmployeeId, bPending);
			}
		} finally {}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		 if (session != null) {
			 LoginModel managerLogin = (LoginModel) session.getAttribute(EmployeeLoginServlet.LOGIN_ATTR);
			 int managerId = managerLogin.getId();
			 String requestId = req.getParameter("requestId");
			 String approved = req.getParameter("approved");
			 if (requestId != null && approved != null) {
				try (Reader src = req.getReader(); PrintWriter writer = resp.getWriter();){
					if (new ExpenseReimbursementRequestService().approveReimbursementRequest(
							src, 
							Integer.parseInt(requestId), 
							Boolean.parseBoolean(approved), 
							managerId))
						writer.write(String.valueOf(managerId));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				} finally {}
			 }
		 }
	}
}
