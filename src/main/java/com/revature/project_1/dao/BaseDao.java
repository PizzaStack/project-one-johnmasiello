package com.revature.project_1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;

import org.jetbrains.annotations.Nullable;

import com.revature.project_1.connection.ConnectionHelper;
import com.revature.project_1.model.LoginModel;
import com.revature.project_1.model.ReimbursementRequestModel;

public class BaseDao {
	public static final int NO_LOGIN_ID = -1;
	
	@Nullable
	protected LoginModel authenticateLogin(String username, String password, String tableName) {
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(new StringBuilder()
				.append("SELECT * FROM ")
				.append(tableName)
				.append(" WHERE username = ? AND password = ?").toString())) {
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())
				return null;
			if (rs.getInt("ID") == NO_LOGIN_ID)
				throw new Exception("Not a valid login key");
			return new LoginModel.Builder()
					.withId(rs.getInt("id"))
					.withUserName(username)
					.withPassword(password)
					.build();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return null;
	}
	
	@Nullable
	protected LoginModel createLogin(String username, String password, String tableName) {
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(new StringBuilder()
				.append("INSERT INTO ")
				.append(tableName)
				.append("(username, password) VALUES (?, ?)").toString())) {
			ps.setString(1, username);
			ps.setString(2, password);
			if (ps.executeUpdate() <= 0)
				return null;
			ResultSet keys = ps.getGeneratedKeys();
			if (!keys.next() || keys.getInt("ID") == NO_LOGIN_ID)
				throw new Exception("Not a valid login key");
			return new LoginModel.Builder()
					.withId(keys.getInt("id"))
					.withUserName(username)
					.withPassword(password)
					.build();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return null;
	}
	
	@Nullable
	public ReimbursementRequestModel queryReimbursementById(int id) {
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT * FROM expense_requests WHERE id = ?")) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			return !rs.next() ? null : readReimbursementRequest(rs);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return null;
	}
	
	@Nullable
	public Collection<ReimbursementRequestModel> queryAllReimbursementsByEmployeeId(int employee_id) {
		Collection<ReimbursementRequestModel> reis = new LinkedList<>();
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT * FROM expense_requests WHERE employee_id = ?")) {
			ps.setInt(1, employee_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				reis.add(readReimbursementRequest(rs));
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return reis;
	}
	
	@Nullable
	public Collection<ReimbursementRequestModel> queryReimbursementsByEmployeeId(int employee_id,
			boolean resolved) {
		Collection<ReimbursementRequestModel> reis = new LinkedList<>();
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(
				new StringBuilder("SELECT * FROM expense_requests WHERE employee_id = ?")
				.append(" AND resolved = ?").toString())) {
			ps.setInt(1, employee_id);
			ps.setBoolean(2, resolved);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				reis.add(readReimbursementRequest(rs));
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return reis;
	}
	
	@Nullable
	public Collection<ReimbursementRequestModel> queryAllResolvedReimbursements() {
		return queryAllReimbursementsByResolved(true);
	}
	
	@Nullable
	public Collection<ReimbursementRequestModel> queryAllPendingReimbursements() {
		return queryAllReimbursementsByResolved(false);
	}
	
	@Nullable
	private Collection<ReimbursementRequestModel> queryAllReimbursementsByResolved(boolean resolved) {
		Collection<ReimbursementRequestModel> reis = new LinkedList<>();
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT * FROM expense_requests WHERE resolved = ?")) {
			ps.setBoolean(1, resolved);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				reis.add(readReimbursementRequest(rs));
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return reis;
	}
	
	@Nullable
	public Collection<ReimbursementRequestModel> queryAllReimbursements() {
		Collection<ReimbursementRequestModel> reis = new LinkedList<>();
		Connection connection = ConnectionHelper.getinstance().getConnection();
		try (Statement statement = connection.createStatement()) {
			ResultSet rs = statement.executeQuery(
					"SELECT * FROM expense_requests");
			while (rs.next())
				reis.add(readReimbursementRequest(rs));
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			ConnectionHelper.getinstance().closeConnection();
		}
		return reis;
	}
	
	@Nullable
	private ReimbursementRequestModel readReimbursementRequest(ResultSet rs) {
		try {
			return new ReimbursementRequestModel.Builder()
					.withId(rs.getInt("id"))
					.withDescription(rs.getString("description"))
					.withExpense(rs.getDouble("expense"))
					.withDate(rs.getObject("date_created", LocalDate.class))
					.withEmployeeId(rs.getInt("employee_id"))
					.withApproved(rs.getBoolean("approved"))
					.withResolved(rs.getBoolean("resolved"))
					.withManagerId(rs.getInt("manager_id"))
					.withReceiptName(rs.getString("receipt_name"))
					.withReceiptScan(rs.getBytes("receipt_scan"))
					.build();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
