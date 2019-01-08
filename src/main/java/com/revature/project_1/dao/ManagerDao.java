package com.revature.project_1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revature.project_1.connection.ConnectionHelper;
import com.revature.project_1.model.LoginModel;

public class ManagerDao extends BaseDao {
	public LoginModel authenticateLogin(String username, String password) {
		return super.authenticateLogin(username, password, "manager_login");
	}
	public LoginModel createLogin(String username, String password) {
		return super.createLogin(username, password, "manager_login");
	}	
	
	public boolean updateReimbursementRequest(int id, boolean approved, int manager_id) {
		if (manager_id == BaseDao.NO_LOGIN_ID)
			return false;
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(new StringBuilder()
				.append("UPDATE expense_requests SET resolved = true, ")
				.append("approved = ?, manager_id = ? WHERE id = ?").toString())) {
			ps.setBoolean(1, approved);
			ps.setInt(2, manager_id);
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
