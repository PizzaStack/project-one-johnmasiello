package service;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class TestService {
	@Test
	public void testWriteCollectionReimbursementRequests() throws IOException {
		PrintWriter writer = new PrintWriter(System.out);
		new ExpenseReimbursementRequestService().writeFetchedReimbursementsByEmployeeId(writer, 1);
	}
	@Test
	public void testWriteCollectionReimbursementPendingRequests() throws IOException {
		PrintWriter writer = new PrintWriter(System.out);
		new ExpenseReimbursementRequestService().writeFetchedReimbursementsByEmployeeId(writer, 1, false);
	}
}
