package com.revature.project_1.dao;

import org.junit.Assert;
import org.junit.Test;

import com.revature.project_1.model.EmployeeInfoModel;
import com.revature.project_1.model.LoginModel;

public class EmployeeDaoTest {
	@Test
	public void testSomething() {
		EmployeeDao dao = new EmployeeDao();
		LoginModel loginModel = dao.authenticateLogin("john", "secure");
		Assert.assertNotNull(loginModel);
	}
}
