package com.revature.project_1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jetbrains.annotations.Nullable;

import com.revature.project_1.connection.ConnectionHelper;
import com.revature.project_1.model.AddressModel;
import com.revature.project_1.model.EmployeeInfoModel;
import com.revature.project_1.model.LoginModel;
import com.revature.project_1.model.ReimbursementRequestModel;

public class EmployeeDao extends BaseDao {
	public LoginModel authenticateLogin(String username, String password) {
		return super.authenticateLogin(username, password, "employee_login");
	}

	public LoginModel createLogin(String username, String password) {
		return super.createLogin(username, password, "employee_login");
	}
	
	public boolean upsertInfo(EmployeeInfoModel employeeInfo, int id) {
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(new StringBuilder()
				.append("INSERT INTO employee_info (id, email, firstname, lastname, ")
				.append("resident_streetaddress, resident_city, resident_zipcode, resident_state) ")
				.append("VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)")
				.append("ON CONFLICT ON CONSTRAINT employee_info_pkey ")
				.append("DO UPDATE SET email = ?, firstname = ?, lastname = ?, ")
				.append("resident_streetaddress = ?, resident_city = ?, resident_zipcode= ?, ")
				.append("resident_state = ? ")
				.append("WHERE employee_info.id = ?")
				.toString())) {
			
			ps.setInt(1, id);
			ps.setString(2, employeeInfo.getEmail());
			ps.setString(3, employeeInfo.getFirstname());
			ps.setString(4, employeeInfo.getLastname());
			ps.setString(5, employeeInfo.getResidentAddress().getStreetAddress());
			ps.setString(6, employeeInfo.getResidentAddress().getCity());
			ps.setString(7, employeeInfo.getResidentAddress().getZipcode());
			ps.setString(8, employeeInfo.getResidentAddress().getState());
			
			ps.setString(9, employeeInfo.getEmail());
			ps.setString(10, employeeInfo.getFirstname());
			ps.setString(11, employeeInfo.getLastname());
			ps.setString(12, employeeInfo.getResidentAddress().getStreetAddress());
			ps.setString(13, employeeInfo.getResidentAddress().getCity());
			ps.setString(14, employeeInfo.getResidentAddress().getZipcode());
			ps.setString(15, employeeInfo.getResidentAddress().getState());
			ps.setInt(16, id);

			if (ps.executeUpdate() >= 0)
				return true;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return false;
	}
	
	@Nullable
	public EmployeeInfoModel queryEmployeeInfo(int id) {
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT * FROM employee_info WHERE id = ?")) {
			ps.setInt(1,  id);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())
				return null;
			AddressModel residentAddress = new AddressModel.Builder()
					.withStreetAdress(rs.getString("resident_streetaddress"))
					.withCity(rs.getString("resident_city"))
					.withZipCode(rs.getString("resident_zipcode"))
					.withState(rs.getString("resident_state"))
					.build();
			return new EmployeeInfoModel.Builder()
					.withId(rs.getInt("id"))
					.withEmail(rs.getString("email"))
					.withFirstName(rs.getString("firstname"))
					.withLastName(rs.getString("lastname"))
					.withResidentAddress(residentAddress)
					.build();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return null;
	}
	
	@Nullable
	public ReimbursementRequestModel upsertReimbursementRequest(ReimbursementRequestModel req) {
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(new StringBuilder()
				.append("INSERT INTO expense_requests(description, expense, ")
				.append("employee_id, approved, resolved, manager_id, ")
				.append("receipt_name, receipt_scan) VALUES (?,?,?,?,?,?,?,?) ")
				.append("ON CONFLICT ON CONSTRAINT requests_pkey ")
				.append("DO UPDATE SET description=?, expense=?, ")
				.append("employee_id=?, approved=?, resolved=?, manager_id=?,")
				.append("receipt_name=?, receipt_scan=? WHERE expense_requests.id = ?")
				.toString(), Statement.RETURN_GENERATED_KEYS)) {
			
			ps.setString(1, req.getDescription());
			ps.setDouble(2, req.getExpense());
			ps.setInt(3, req.getEmployeeId());
			ps.setBoolean(4, req.isApproved());
			ps.setBoolean(5, req.isResolved());
			ps.setInt(6, req.getManagerId());
			ps.setString(7, req.getReceiptName());
			ps.setBytes(8, req.getReceiptScan());
			
			ps.setString(9, req.getDescription());
			ps.setDouble(10, req.getExpense());
			ps.setInt(11, req.getEmployeeId());
			ps.setBoolean(12, req.isApproved());
			ps.setBoolean(13, req.isResolved());
			ps.setInt(14, req.getManagerId());
			ps.setString(15, req.getReceiptName());
			ps.setBytes(16, req.getReceiptScan());
			ps.setInt(17, req.getId());
			
			if (ps.executeUpdate() <= 0)
				return null;
			ResultSet keys = ps.getGeneratedKeys();
			if (!keys.next() || keys.getInt("id") == NO_LOGIN_ID)
				return null;
			req.setId(keys.getInt("id"));
			return req;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return null;
	}
	
	public boolean updateReceiptScan(byte[] scan, String receiptName, int id) {
		if (scan == null || receiptName == null)
			return false;
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE expense_requests SET receipt_name=?, receipt_scan=? WHERE id = ?")) {
			
			ps.setString(1, receiptName);
			ps.setBytes(2, scan);
			ps.setInt(3, id);
			
			return ps.executeUpdate() > 0;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return false;
	}
}
