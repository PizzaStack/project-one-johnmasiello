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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project_1.dao.EmployeeDao;
import com.revature.project_1.model.LoginModel;
import com.revature.project_1.model.ReimbursementRequestModel;

@WebServlet(urlPatterns="/submit-reimbursement_request")
public class ReimbursementSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String receiptId = req.getParameter("receipt_id");
		ReimbursementRequestModel model = new EmployeeDao().queryReimbursementById(Integer.parseInt(receiptId));
		try (PrintWriter writer = resp.getWriter()) {
			if (model == null) 
				writer.write("{}");
			else {
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(writer, model);
			}
		} finally {}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		 if (session != null) {
			 LoginModel employeeLogin = (LoginModel) session.getAttribute(EmployeeLoginServlet.LOGIN_ATTR);
			 int employeeId = employeeLogin.getId();
			ObjectMapper mapper = new ObjectMapper();
			boolean success = false;
			try (Reader src = req.getReader()){
				ReimbursementRequestModel model = mapper.readValue(src, ReimbursementRequestModel.class);
				model.setEmployeeId(employeeId);
				System.out.println(model);
				success = new EmployeeDao().upsertReimbursementRequest(model) != null;
			} catch (JsonParseException e) {
				System.out.println(e.getMessage());
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			try (PrintWriter writer = resp.getWriter()) {
				writer.write(success ? "success" : "failure");
			} finally {}
		 }
	}
}
