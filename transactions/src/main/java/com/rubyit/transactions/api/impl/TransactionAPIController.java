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
import com.rubyit.transactions.model.Transaction;

import io.swagger.annotations.ApiParam;

@Controller
public class TransactionAPIController implements TransactionsApi {
	
	private static Logger log = LoggerFactory.getLogger(TransactionAPIController.class);
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	public Optional<ObjectMapper> getObjectMapper() {
        
		return Optional.of(objectMapper);
    }

    public Optional<HttpServletRequest> getRequest() {
        
    	return Optional.of(httpServletRequest);
    }

    public Optional<String> getAcceptHeader() {

    	return getRequest().map(r -> r.getHeader("Accept"));
    }
	
	public ResponseEntity<Void> pOSTTransactions(@ApiParam(value = "" ,required=true ) @RequestBody Transaction body) {
        // do some magic!
		
		log.info("POST OP WAS CALLED");
		if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
			
			log.info("IS A VALID POST");
			return new ResponseEntity<>(HttpStatus.OK);
        } else {
        	
            log.warn("ObjectMapper or HttpServletRequest not configured in default TransactionsApi interface so no example is generated");
        }
		
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
