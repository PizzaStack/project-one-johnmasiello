package com.revature.project_1.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class ReimbursementRequestModel {
	private int id;
	private String description;
	private double expense;
	private LocalDate date;
	private int employeeId;
	private boolean approved;
	private boolean resolved;
	private int managerId;
	private byte[] receiptScan;
	private String receiptName;
	
//	public ReimbursementRequestModel() {}
	
	@JsonCreator
	private ReimbursementRequestModel(@JsonProperty("id")int id, @JsonProperty("description")String description, 
			@JsonProperty("expense")double expense, @JsonProperty("date")LocalDate date, 
			@JsonProperty("employeeId")int employeeId, @JsonProperty("approved")boolean approved, 
			@JsonProperty("resolved")boolean resolved, @JsonProperty("managerId")int managerId, 
			@JsonProperty("receiptScan")byte[] receiptScan, @JsonProperty("receiptName")String receiptName) {
		this.id = id;
		this.description = description;
		this.expense = expense;
		this.date = date;
		this.employeeId = employeeId;
		this.approved = approved;
		this.resolved = resolved;
		this.managerId = managerId;
		this.receiptScan = receiptScan;
		this.receiptName = receiptName;
	}
	
	public static class Builder {
		private int id;
		private String description;
		private double expense;
		private LocalDate date;
		private int employeeId;
		private boolean approved;
		private boolean resolved;
		private int managerId;
		private byte[] receiptScan;
		private String receiptName;
		
		public Builder withId(int id) {
			this.id = id;
			return this; 
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withExpense(double expense) {
			this.expense = expense;
			return this;
		}

		public Builder withDate(LocalDate date) {
			this.date = date;
			return this;
		}

		public Builder withEmployeeId(int employeeId) {
			this.employeeId = employeeId;
			return this;
		}

		public Builder withApproved(boolean approved) {
			this.approved = approved;
			return this;
		}

		public Builder withResolved(boolean resolved) {
			this.resolved = resolved;
			return this;
		}

		public Builder withManagerId(int managerId) {
			this.managerId = managerId;
			return this;
		}
		
		public Builder withReceiptScan(byte[] receiptScan) {
			this.receiptScan = receiptScan;
			return this;
		}

		public Builder withReceiptName(String receiptName) {
			this.receiptName = receiptName;
			return this;
		}
		
		public ReimbursementRequestModel build() {
			return new ReimbursementRequestModel(id, description, expense, date, employeeId, approved, resolved, managerId, receiptScan, receiptName);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getExpense() {
		return expense;
	}

	public void setExpense(double expense) {
		this.expense = expense;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public byte[] getReceiptScan() {
		return receiptScan;
	}

	public void setReceiptScan(byte[] receiptScan) {
		this.receiptScan = receiptScan;
	}

	public String getReceiptName() {
		return receiptName;
	}

	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ Objects.hash(approved, date, description, employeeId, expense, id, managerId, receiptName, resolved);
		result = prime * result + Arrays.hashCode(receiptScan);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReimbursementRequestModel))
			return false;
		ReimbursementRequestModel other = (ReimbursementRequestModel) obj;
		return approved == other.approved && Objects.equals(date, other.date)
				&& Objects.equals(description, other.description) && employeeId == other.employeeId
				&& Double.doubleToLongBits(expense) == Double.doubleToLongBits(other.expense) && id == other.id
				&& managerId == other.managerId && Objects.equals(receiptName, other.receiptName)
				&& Arrays.equals(receiptScan, other.receiptScan) && resolved == other.resolved;
	}

	@Override
	public String toString() {
		return "ReimbursementRequestModel [id=" + id + ", description=" + description + ", expense=" + expense
				+ ", date=" + date + ", employeeId=" + employeeId + ", approved=" + approved + ", resolved=" + resolved
				+ ", managerId=" + managerId + ", receiptScan=" + Arrays.toString(receiptScan) + ", receiptName="
				+ receiptName + "]";
	}
}
