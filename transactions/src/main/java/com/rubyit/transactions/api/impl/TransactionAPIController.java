package com.rubyit.transactions.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.rubyit.transactions.api.TransactionsApi;
import com.rubyit.transactions.model.Transaction;

import io.swagger.annotations.ApiParam;

@Controller
public class TransactionAPIController implements TransactionsApi {
	
	public ResponseEntity<Void> pOSTTransactions(@ApiParam(value = "" ,required=true ) @RequestBody Transaction body) {
        // do some magic!
		
		System.out.println("PASSEI NO POST");
		
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
