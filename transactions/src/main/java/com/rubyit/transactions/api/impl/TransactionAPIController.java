package com.rubyit.transactions.api.impl;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubyit.transactions.api.TransactionsApi;
import com.rubyit.transactions.business.BusinessService;
import com.rubyit.transactions.model.Transaction;

import io.swagger.annotations.ApiParam;

@Controller
public class TransactionAPIController implements TransactionsApi {
	
	private static Logger log = LoggerFactory.getLogger(TransactionAPIController.class);
	private static final String HTTP_HEADER_ACCEPT = "Accept";
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private HttpServletRequest httpServletRequest;
	@Autowired
	private BusinessService businessService;
	
	public Optional<ObjectMapper> getObjectMapper() {
        
		return Optional.of(objectMapper);
    }

    public Optional<HttpServletRequest> getRequest() {
        
    	return Optional.of(httpServletRequest);
    }

    public Optional<String> getAcceptHeader() {

    	return getRequest().map(r -> r.getHeader(HTTP_HEADER_ACCEPT));
    }
	
	public ResponseEntity<Void> pOSTTransactions(@ApiParam(value = "" ,required=true ) @RequestBody Transaction transaction) {
		
		if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
			
			if (businessService.isTransactionExpired(transaction)) {
				
	            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	        } else {
	        	
	        	businessService.addTransaction(transaction);
	            return new ResponseEntity<Void>(HttpStatus.CREATED);
	        }
        } else {
        	
            log.warn("ObjectMapper or HttpServletRequest not configured in default TransactionsApi interface so no example is generated");
        }
		
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
