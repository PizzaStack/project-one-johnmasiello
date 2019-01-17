package com.revature.project_1.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.ExpenseReimbursementRequestService;

@WebServlet(urlPatterns="/manage-all-reimbursement_request")
public class ManageAllReimbursementRequestsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pending = req.getParameter("pending");
		
		try (PrintWriter writer = resp.getWriter()) {
			ExpenseReimbursementRequestService service = new ExpenseReimbursementRequestService();
			if (pending == null)
				service.writeAllFetchedReimbursements(writer);
			else {
				boolean bPending = Boolean.parseBoolean(pending);
				service.writeAllFetchedReimbursements(writer, bPending);
			}
		} finally {}
	}
	
	
}
