package com.rubyit.transactions.api.test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.rubyit.transactions.business.BusinessService;
import com.rubyit.transactions.model.Transaction;

@RunWith(SpringRunner.class)
public class BusinessServiceTest {

    @Test
    public void shouldBeAbleToAddTransactionsAndCalculateStatistics(){
        BusinessService businessService = new BusinessService();

        assertEquals(businessService.getStatistics().getCount(), BigDecimal.ZERO);
        assertEquals(businessService.getStatistics().getAvg().doubleValue(), 0d, 0d);

        double amount = 456.789;
        long timestamp = System.currentTimeMillis();

        Transaction tx = new Transaction();
        tx.setAmount(BigDecimal.valueOf(amount));
        tx.setTimestamp(BigDecimal.valueOf(timestamp));

        businessService.addTransaction(tx);
        businessService.addTransaction(tx);

        assertEquals(businessService.getStatistics().getCount(), BigDecimal.valueOf(2));
        assertEquals(businessService.getStatistics().getSum().doubleValue(), (amount * 2), 0d);
        assertEquals(businessService.getStatistics().getAvg().doubleValue(), amount, 0d);
    }

    @Test
    public void shouldNotBeAbleToAddExpiredTransactions() {
        BusinessService transactionStatisticsService = new BusinessService();

        double amount = 123456.789;
        long timestamp = System.currentTimeMillis() - 70000;

        Transaction tx = new Transaction();
        tx.setAmount(BigDecimal.valueOf(amount));
        tx.setTimestamp(BigDecimal.valueOf(timestamp));
        
        transactionStatisticsService.addTransaction(tx);

        assertEquals(transactionStatisticsService.getStatistics().getCount(), BigDecimal.ZERO);
    }

    @Test
    public void shouldBeAbleToGetStatisticsOnlyForTransactionsOfLastMinute() {
        BusinessService transactionStatisticsService = new BusinessService();

        long timestamp = System.currentTimeMillis();
        
        Transaction tx01 = new Transaction();
        tx01.setAmount(BigDecimal.valueOf(456.789));
        tx01.setTimestamp(BigDecimal.valueOf(timestamp));
        
        Transaction tx02 = new Transaction();
        tx02.setAmount(BigDecimal.valueOf(123.45));
        tx02.setTimestamp(BigDecimal.valueOf(timestamp - 50000));
        
        transactionStatisticsService.addTransaction(tx01);

        transactionStatisticsService.addTransaction(tx02);

        assertEquals(transactionStatisticsService.getStatistics().getCount(), BigDecimal.valueOf(2));

        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(transactionStatisticsService.getStatistics().getCount(), BigDecimal.ONE);
    }

}
