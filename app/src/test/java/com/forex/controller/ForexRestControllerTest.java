package com.forex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.forex.entity.Account;
import com.forex.entity.Amount;
import com.forex.repository.ForexRepository;

@ExtendWith(MockitoExtension.class)
public class ForexRestControllerTest {

	@Mock
	private ForexRepository forexRepository;

	@InjectMocks
	private ForexRestController forexRestController;

	@BeforeEach
	void init() {

	}

	@Test
	void test_getAccountStatus_succes() {
		// prepare
		String accountnum = "accountNumber";

		Account account = new Account();
		account.setStatus("status");
		when(forexRepository.findByAccountnum(accountnum)).thenReturn(account);

		// act
		ResponseEntity<String> response = forexRestController.getAccountStatus(accountnum);

		// verify
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), "status");

	}
	
	@Test
	void test_getAccountBalance_succes() {
		// prepare
		String accountnum = "accountNumber";

		Account account = new Account();
		BigDecimal balance = new BigDecimal(100.00);
		account.setBalance(balance);
		when(forexRepository.findByAccountnum(accountnum)).thenReturn(account);

		// act
		ResponseEntity<BigDecimal> response = forexRestController.getAccountBalance(accountnum);

		// verify
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), balance);

	}
	
	@Test
	void test_performOperation_debitsucces() {
		// prepare
		
		String accountnum = "accountNumber";
		Amount amount = new Amount();
		amount.setAccountnum(accountnum);
		amount.setAmount(new BigDecimal(100.00));
		amount.setCurrency("111");
		amount.setOperation("DEBIT");
	

		Account account = new Account();
		BigDecimal balance = new BigDecimal(1000.00);
		account.setBalance(balance);
		account.setAccountnum(accountnum);
		account.setCurrency("111");
		account.setStatus("OPEN");
		when(forexRepository.findByAccountnum(accountnum)).thenReturn(account);
		when(forexRepository.save(account)).thenReturn(account);

		// act
		ResponseEntity<Account> response = forexRestController.performOperation(amount);

		// verify
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getBalance(), new BigDecimal(1100.00));
		verify(forexRepository).save(account);
	}

	@Test
	void test_performOperation_creditsucces() {
		// prepare
		
		String accountnum = "accountNumber";
		Amount amount = new Amount();
		amount.setAccountnum(accountnum);
		amount.setAmount(new BigDecimal(100.00));
		amount.setCurrency("111");
		amount.setOperation("CREDIT");
	

		Account account = new Account();
		BigDecimal balance = new BigDecimal(1000.00);
		account.setBalance(balance);
		account.setAccountnum(accountnum);
		account.setCurrency("111");
		account.setStatus("OPEN");
		when(forexRepository.findByAccountnum(accountnum)).thenReturn(account);
		when(forexRepository.save(account)).thenReturn(account);

		// act
		ResponseEntity<Account> response = forexRestController.performOperation(amount);

		// verify
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getBalance(), new BigDecimal(900.00));
		verify(forexRepository).save(account);
	}

	@Test
	void test_performOperation_creditfailure() {
		// prepare
		
		String accountnum = "accountNumber";
		Amount amount = new Amount();
		amount.setAccountnum(accountnum);
		amount.setAmount(new BigDecimal(1000.00));
		amount.setCurrency("111");
		amount.setOperation("CREDIT");
	

		Account account = new Account();
		BigDecimal balance = new BigDecimal(100.00);
		account.setBalance(balance);
		account.setAccountnum(accountnum);
		account.setCurrency("111");
		account.setStatus("OPEN");
		when(forexRepository.findByAccountnum(accountnum)).thenReturn(account);


		// act
		ResponseEntity<Account> response = forexRestController.performOperation(amount);

		// verify
		assertEquals(response.getStatusCode(), HttpStatus.PRECONDITION_FAILED);
		verify(forexRepository, times(0)).save(account);
	}

}
