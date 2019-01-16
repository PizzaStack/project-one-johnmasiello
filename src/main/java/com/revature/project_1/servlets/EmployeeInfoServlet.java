package com.revature.project_1.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project_1.dao.EmployeeDao;
import com.revature.project_1.model.EmployeeInfoModel;

@WebServlet(urlPatterns="/employee-info")
public class EmployeeInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO extract employeeId out of the session
		String employeeId = req.getParameter("employee_id");
		EmployeeInfoModel model = new EmployeeDao().queryEmployeeInfo(Integer.parseInt(employeeId));
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
		// TODO get the employeeId from the session
		int employeeId = 1;
		ObjectMapper mapper = new ObjectMapper();
		boolean success = false;
		try (Reader src = req.getReader()){
			EmployeeInfoModel model = mapper.readValue(src, EmployeeInfoModel.class);
			System.out.println(model);
			success = new EmployeeDao().upsertInfo(model, employeeId);
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
