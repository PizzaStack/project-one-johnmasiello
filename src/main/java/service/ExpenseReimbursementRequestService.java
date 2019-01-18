package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project_1.dao.EmployeeDao;
import com.revature.project_1.dao.ManagerDao;
import com.revature.project_1.model.ReimbursementRequestModel;

public class ExpenseReimbursementRequestService {
	public boolean createReimbursementRequest(Reader src, int employeeId) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ReimbursementRequestModel model = mapper.readValue(src, ReimbursementRequestModel.class);
		model.setEmployeeId(employeeId);
		return new EmployeeDao().upsertReimbursementRequest(model) != null;
	}
	
	public boolean approveReimbursementRequest(Reader src, int requestId, 
			boolean approved, int managerId) {
		return new ManagerDao().updateReimbursementRequest(requestId, approved, managerId);
	}
	
	public void writeFetchedReimbursementById(PrintWriter writer, int id) throws IOException {
		ReimbursementRequestModel model = new EmployeeDao().queryReimbursementById(id);
		new ObjectMapper().writeValue(writer, model);
	}
	
	public void writeFetchedReimbursementsByEmployeeId(PrintWriter writer, int id) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<ReimbursementRequestModel> models = new ArrayList<>(new EmployeeDao().queryAllReimbursementsByEmployeeId(id));
		models.forEach(($)->{
			System.out.println($);
		try {
			System.out.println(mapper.writeValueAsString($));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
		new ObjectMapper().writeValue(writer, models);
	}
	
	public void writeFetchedReimbursementsByEmployeeId(PrintWriter writer, int id, boolean pending) throws IOException {
		List<ReimbursementRequestModel> models = new ArrayList<>(new EmployeeDao().queryReimbursementsByEmployeeId(id, 
				!pending));
		new ObjectMapper().writeValue(writer, models);
	}
	
	public void writeAllFetchedReimbursements(PrintWriter writer) throws IOException {
		List<ReimbursementRequestModel> models = new ArrayList<>(new EmployeeDao().queryAllPendingReimbursements());
		new ObjectMapper().writeValue(writer, models);
	}
	
	public void writeAllFetchedReimbursements(PrintWriter writer, boolean pending) throws IOException {
		List<ReimbursementRequestModel> models = new ArrayList<>(pending ? new EmployeeDao().queryAllPendingReimbursements() : 
				new EmployeeDao().queryAllResolvedReimbursements());
		new ObjectMapper().writeValue(writer, models);
	}
}
