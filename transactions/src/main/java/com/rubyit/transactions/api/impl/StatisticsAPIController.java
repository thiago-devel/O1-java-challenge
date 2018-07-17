package com.rubyit.transactions.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.rubyit.transactions.api.StatisticsApi;
import com.rubyit.transactions.model.Statistic;

@Controller
public class StatisticsAPIController implements StatisticsApi {
	
	public ResponseEntity<Statistic> gETStatistics() {
        // do some magic!
		
		System.out.println("PASSEI NO GET");
		
        return new ResponseEntity<Statistic>(HttpStatus.OK);
    }
}
