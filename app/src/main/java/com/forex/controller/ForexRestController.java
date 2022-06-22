package com.forex.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.forex.entity.Account;
import com.forex.entity.Amount;
import com.forex.repository.ForexRepository;

@RestController
public class ForexRestController {
	@Autowired
	private ForexRepository forexRepository;

	@GetMapping("/find-status/{accountnum}")
	public ResponseEntity<String> getAccountStatus(@PathVariable String accountnum) {
		Account account = forexRepository.findByAccountnum(accountnum);
		
		if (account == null) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
		String status = account.getStatus();
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

	@GetMapping("/find-balance/{accountnum}")
	public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable String accountnum) {
		Account account = forexRepository.findByAccountnum(accountnum);
		
		if (account == null) {
			return new ResponseEntity<BigDecimal>(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal balance = account.getBalance();
		return new ResponseEntity<BigDecimal>(balance, HttpStatus.OK);
	}

	@PostMapping("/operation")
	public ResponseEntity<Account> performOperation(@RequestBody Amount amount) {

		Account account = forexRepository.findByAccountnum(amount.getAccountnum());

		if (amount.getAccountnum() == null || amount.getAmount() == null || amount.getCurrency() == null
				|| amount.getOperation() == null) {
			
			return new ResponseEntity<Account>(HttpStatus.PRECONDITION_FAILED);
		}

		if (account != null && account.getStatus().equalsIgnoreCase("OPEN") && account.getCurrency().equals(amount.getCurrency())
				&& amount.getAmount().compareTo(new BigDecimal(0)) == 1) {

			Account result = null;
			if (amount.getOperation().equalsIgnoreCase("DEBIT")) {
				BigDecimal finalBalance = account.getBalance().add(amount.getAmount());
				account.setBalance(finalBalance);
				result = forexRepository.save(account);
				
			} else if (amount.getOperation().equalsIgnoreCase("CREDIT")
					&& account.getBalance().compareTo(amount.getAmount()) != -1) {
				BigDecimal finalBalance = account.getBalance().subtract(amount.getAmount());
				account.setBalance(finalBalance);
				result = forexRepository.save(account);
			}
			if (result == null) {
				return new ResponseEntity<Account>(HttpStatus.PRECONDITION_FAILED);
			}

			return new ResponseEntity<Account>(result, HttpStatus.OK);
		}

		return new ResponseEntity<Account>(HttpStatus.FORBIDDEN);
	}
}
