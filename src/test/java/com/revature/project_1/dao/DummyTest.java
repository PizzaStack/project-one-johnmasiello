package com.revature.project_1.dao;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project_1.model.EmployeeInfoModel;
import com.revature.project_1.model.ReimbursementRequestModel;

public class DummyTest {
	private ReimbursementRequestModel modelDefined;
	private ReimbursementRequestModel modelDummy;
	@Before
	public void init() {
		modelDefined = new ReimbursementRequestModel.Builder()
				.withDescription("Travel")
				.withExpense(250.00)
				.withApproved(false)
				.withResolved(false)
				.withReceiptName("receipt 1")
				.build();
	}
	@Test
	public void testJSon() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(modelDefined);
		System.out.println(json);
		modelDummy = mapper.readValue(json, ReimbursementRequestModel.class);
		System.out.println(modelDummy);
		Assert.assertEquals(modelDefined, modelDummy);
	}
	@Test
	public void testJSonEmployeeInfo() throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"email\": \"jmasgcc@gmail.com\", \"firstname\": \"John\", \"lastname\": \"Masiello\", \"residentAddress\": "
				+ "{\"streetAddress\": \"3300 South Cooper St\", \"city\": \"Dallas\", \"zipcode\": \"76022\", \"state\": \"TX\"}}";
		EmployeeInfoModel dummy = new ObjectMapper().readValue(json, EmployeeInfoModel.class);
	}
}
