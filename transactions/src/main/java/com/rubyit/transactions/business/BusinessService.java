package com.rubyit.transactions.business;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.rubyit.transactions.model.Statistic;
import com.rubyit.transactions.model.Transaction;
import com.rubyit.transactions.models.TransactionProxy;

@Service
public class BusinessService {

	private static final int BASE_MILLIS = 1000;
	private static final Long ONE_MINUTE_IN_MILLIS = 60 * 1000l;
    private PriorityQueue<TransactionProxy> transactions;
    private volatile Statistic statistic;

    private Lock lock = new ReentrantLock();

    public BusinessService(){
        transactions = new PriorityQueue<>();
        statistic = new Statistic();
        resetStatistic();
    }

    public void addTransaction(final Transaction transaction) {

        lock.lock();
        
        try {
        	
            transactions.add(new TransactionProxy(transaction));
            addTransactionToStatistics(transaction);
        } finally {
            lock.unlock();
        }
    }

    private void addTransactionToStatistics(final Transaction transaction) {
    	
    	final BigDecimal count = statistic.getCount().add(BigDecimal.ONE);
    	final BigDecimal sum = statistic.getSum().add(transaction.getAmount());
    	final BigDecimal avg = (statistic.getSum().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : statistic.getSum().divide(statistic.getCount(), java.math.MathContext.DECIMAL128);
    	final BigDecimal min = (statistic.getSum().compareTo(BigDecimal.ONE) > 0) ? BigDecimal.valueOf(Math.min(statistic.getMin().doubleValue(), transaction.getAmount().doubleValue())) : transaction.getAmount();
    	final BigDecimal max = (statistic.getSum().compareTo(BigDecimal.ONE) > 0) ? BigDecimal.valueOf(Math.max(statistic.getMax().doubleValue(), transaction.getAmount().doubleValue())) : transaction.getAmount();
    	
        statistic.setCount(count);
        statistic.setSum(sum);
        statistic.setAvg(avg);
        statistic.setMin(min);
        statistic.setMax(max);
    }
    
    private void resetStatistic() {
    	
    	statistic.setCount(BigDecimal.ZERO);
    	statistic.setSum(BigDecimal.ZERO);
    	statistic.setAvg(BigDecimal.ZERO);
    	statistic.setMin(BigDecimal.ZERO);
    	statistic.setMax(BigDecimal.ZERO);
    }

	private void removeTransactionFromStatistics(final TransactionProxy proxy) {

		if (statistic.getCount().compareTo(BigDecimal.ONE) == 0) {
			
			resetStatistic();
		} else {
			
			final BigDecimal count = statistic.getCount().subtract(BigDecimal.ONE);
			final BigDecimal sum = statistic.getSum().subtract(proxy.getTransaction().getAmount());
			final BigDecimal avg = statistic.getSum().divide(statistic.getCount(), java.math.MathContext.DECIMAL128);
			
			statistic.setCount(count);
			statistic.setSum(sum);
			statistic.setAvg(avg);
			statistic.setMin(getMin());
			statistic.setMax(getMax());
		}
	}

    private BigDecimal getMin() {
    	
        if (statistic.getCount().compareTo(BigDecimal.ZERO) == 0) {
        	
            return BigDecimal.ZERO;
        }
        
        return transactions
                .stream()
                .min(Comparator.comparing((TransactionProxy proxy) -> proxy.getTransaction().getAmount())).get().getTransaction().getAmount();
    }

    private BigDecimal getMax() {
    	
        if (statistic.getCount().compareTo(BigDecimal.ZERO) == 0) {
        	
            return BigDecimal.ZERO;
        }
        
        return transactions
                .stream()
                .max(Comparator.comparing((TransactionProxy proxy) -> proxy.getTransaction().getAmount())).get().getTransaction().getAmount();
    }

    public Statistic getStatistics() {
    	
        removeExpiredTransactions();
        
        return statistic;
    }

    private void removeExpiredTransactions() {
    	
        TransactionProxy proxy = transactions.peek();
        
        while (proxy != null && isTransactionExpired(proxy.getTransaction())) {
            
        	lock.lock();
            
        	try {
        		
                proxy = transactions.peek();
                
                if (isTransactionExpired(proxy.getTransaction())) {
                	
                    removeTransactionFromStatistics(transactions.poll());
                }
            } finally {
            	
                lock.unlock();
            }
            
            proxy = transactions.peek();
        }
    }
    
    public boolean isTransactionExpired(final Transaction transaction) {
    	
        final ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        final Long epochInMillis = utc.toEpochSecond() * BASE_MILLIS;
        final BigDecimal factor = BigDecimal.valueOf(epochInMillis - ONE_MINUTE_IN_MILLIS);
        final Boolean result = (transaction.getTimestamp().compareTo(factor) < 0);
        
        return result;
    }
}
