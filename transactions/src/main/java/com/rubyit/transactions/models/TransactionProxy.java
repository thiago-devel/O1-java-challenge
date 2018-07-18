package com.rubyit.transactions.models;

import com.rubyit.transactions.model.Transaction;

public class TransactionProxy implements Comparable<TransactionProxy>{
	
	final Transaction transaction;
	
	public TransactionProxy(final Transaction transaction) {
		
		this.transaction = transaction;
	}
	
	public Transaction getTransaction() {
		
		return transaction;
	}

	@Override
    public int compareTo(TransactionProxy obj) {
		
        if (obj.transaction.getTimestamp().compareTo(transaction.getTimestamp()) == 0) {
        	
            return 0;
        } else if(obj.transaction.getTimestamp().compareTo(transaction.getTimestamp()) > 0) {
        	
            return -1;
        } else {
        	
            return 1;
        }
    }

}
