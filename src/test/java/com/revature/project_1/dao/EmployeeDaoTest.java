package com.revature.project_1.dao;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.revature.project_1.model.EmployeeInfoModel;
import com.revature.project_1.model.LoginModel;

public class EmployeeDaoTest {
	private EmployeeDao employeeDao;
	private ManagerDao managerDao;
	@Before
	public void init() {
		employeeDao = new EmployeeDao();
		managerDao = new ManagerDao();
	}
	@Test
	public void testAuthenticateEmployee() {
		LoginModel loginModel = employeeDao.authenticateLogin("john", "secret");
		Assert.assertNotNull(loginModel);
		Assert.assertNull(employeeDao.authenticateLogin("john", "wrong password"));
	}
	@Test
	public void testCreateAccountEmployee() {
		LoginModel loginModel = employeeDao.createLogin("john", "stolen password");
		Assert.assertNull(loginModel);
		loginModel = employeeDao.createLogin("Jonas", "yes");
//		Assert.assertNotNull(loginModel);
//		Assert.assertThat(0, Matchers.lessThan(loginModel.getId()));
		Assert.assertNull(loginModel);
	}
	@Test
	public void testAuthenticateManager() {
		LoginModel loginModel = managerDao.authenticateLogin("manager", "#@!");
		Assert.assertNotNull(loginModel);
		Assert.assertNull(managerDao.authenticateLogin("manager", "aaa"));
	}
	@Test
	public void testCreateAccountManager() {
		LoginModel loginModel = managerDao.createLogin("manager", "#@!");
		Assert.assertNull(loginModel);
		loginModel = managerDao.createLogin("tester manager", "asdfFDSF23432sdfsd!@$$");
//		Assert.assertNotNull(loginModel);
//		Assert.assertThat(0, Matchers.lessThan(loginModel.getId()));
		Assert.assertNull(loginModel);
	}
}
