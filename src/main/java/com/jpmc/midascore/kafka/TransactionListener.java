package com.jpmc.midascore.kafka;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.service.TransactionService;

@Component
public class TransactionListener {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionListener.class);

    private final TransactionService transactionService;

    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-group")
    public void listen(Transaction transaction) {
        LOG.info("Received transaction: {}", transaction);
        transactionService.processTransaction(transaction);
    }
}
