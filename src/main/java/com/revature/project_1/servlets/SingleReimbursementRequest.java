package com.revature.project_1.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.project_1.service.ExpenseReimbursementRequestService;

@WebServlet(urlPatterns="/view-single-reimbursement-request")
public class SingleReimbursementRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestId = req.getParameter("request_id");
		if (requestId == null)
			return;
		
		try (PrintWriter writer = resp.getWriter()) {
			int iRequestId = Integer.parseInt(requestId);
			new ExpenseReimbursementRequestService().writeFetchedReimbursementById(writer, iRequestId);
		} finally {}
	}
}
