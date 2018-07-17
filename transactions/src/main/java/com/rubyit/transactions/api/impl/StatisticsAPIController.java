package com.rubyit.transactions.api.impl;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubyit.transactions.api.StatisticsApi;
import com.rubyit.transactions.model.Statistic;

@Controller
public class StatisticsAPIController implements StatisticsApi {
	
	private static Logger log = LoggerFactory.getLogger(StatisticsAPIController.class);
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
	
	public ResponseEntity<Statistic> gETStatistics() {
        // do some magic!
		
		log.info("GET OP WAS CALLED");
		
        //return new ResponseEntity<Statistic>(HttpStatus.OK);
		if(getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
            if (getAcceptHeader().get().contains("application/json")) {
                try {
                    return new ResponseEntity<>(getObjectMapper().get().readValue("{  \"sum\" : 1000,  \"avg\" : 100,  \"max\" : 200,  \"min\" : 50,  \"count\" : 10}", Statistic.class), HttpStatus.NOT_IMPLEMENTED);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            log.warn("ObjectMapper or HttpServletRequest not configured in default StatisticsApi interface so no example is generated");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
