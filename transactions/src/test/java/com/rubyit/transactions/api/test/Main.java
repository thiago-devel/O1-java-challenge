package com.rubyit.transactions.api.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Long currentTime = System.currentTimeMillis();
		
		
		//testTime(currentTime);
		Main m = new Main();
		
		m.insertAmount(12.3, currentTime);
		m.insertAmount(13.3, (currentTime - 5));
		m.insertAmount(44.7, (currentTime - 1000));
		m.insertAmount(52.1, (currentTime - 1001));
		m.insertAmount(77.9, (currentTime - 3000));
		m.insertAmount(1000.44, (currentTime - 61000));
		
		System.out.println(m.getStatictics());
		
	}

	private Statistics getStatictics() {
		Statistics stfinal = new Statistics();
		
		for (Entry<Long, Statistics> entry : statistics.entrySet()) {
			
			stfinal.count += entry.getValue().count;
			stfinal.sum += entry.getValue().sum;
			stfinal.max = Math.max(stfinal.max, entry.getValue().max);
			stfinal.min = Math.min(stfinal.min, entry.getValue().min);
			
			//System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		return stfinal;
	}
	
	Map<Long, Statistics> statistics = new TreeMap<>();
	long lastCurrentTime = 0;

	private void insertAmount(double amount, Long currentTime) {
		
		Long newTime = (currentTime / 1000);
		
		Statistics orDefault = statistics.getOrDefault(newTime, new Statistics());
		
		orDefault.count++;
		orDefault.sum += amount;
		orDefault.max = Math.max(orDefault.max, amount);
		orDefault.min = Math.min(orDefault.min, amount);
		
		lastCurrentTime = Math.max(lastCurrentTime, newTime);
		
		statistics.put(newTime, orDefault);
		
		List<Long> keySet = new ArrayList<>(statistics.keySet()); //<<----iterator?
		for (int i = 0; i < keySet.size(); i++) {
			 long position = keySet.get(i);
			 long test = position + 60L;
			 
			 if (test < lastCurrentTime) {
				 statistics.remove(position);
			 } else {
				 break;
			 }
			 
		}
				
	}

	public class Statistics {
		
		public double sum = 0;
		public double max = 0;
		public double min = Double.POSITIVE_INFINITY;
		public long count = 0;
		
		public double getAvg() {
			return sum / count;
		}

		@Override
		public String toString() {
			return "Statistics [sum=" + sum + ", max=" + max + ", min=" + min + ", count=" + count + ", avg="
					+ getAvg() + "]";
		}
		
		
	}
	
}
