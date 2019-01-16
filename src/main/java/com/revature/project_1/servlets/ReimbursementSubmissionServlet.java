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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project_1.dao.EmployeeDao;
import com.revature.project_1.model.ReimbursementRequestModel;

@WebServlet(urlPatterns="/submit-reimbursement_request")
public class ReimbursementSubmissionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		boolean success = false;
		try (Reader src = req.getReader()){
			ReimbursementRequestModel model = mapper.readValue(src, ReimbursementRequestModel.class);
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
