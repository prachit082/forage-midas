package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jpmc.midascore.client.IncentiveApiClient;
import java.util.Optional;

@Service
public class TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);
    private final IncentiveApiClient incentiveApiClient;
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public TransactionService(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository,
            IncentiveApiClient incentiveApiClient) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveApiClient = incentiveApiClient;
    }

    @Transactional
    public void processTransaction(Transaction transactionDto) {
        // 1. Fetch sender and recipient from the database
        Optional<UserRecord> senderOpt = userRepository.findById(transactionDto.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transactionDto.getRecipientId());

        // 2. Validate the transaction
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            LOG.warn("Invalid transaction: Sender or recipient not found. Discarding. Tx: {}", transactionDto);
            return;
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        if (sender.getBalance() < transactionDto.getAmount()) {
            LOG.warn("Invalid transaction: Insufficient funds. Discarding. Tx: {}", transactionDto);
            return;
        }

        float incentive = incentiveApiClient.getIncentive(transactionDto);
        LOG.info("Received incentive of {} for transaction", incentive);

        // Step 3: Update balances with new incentive logic
        sender.setBalance(sender.getBalance() - transactionDto.getAmount());
        recipient.setBalance(recipient.getBalance() + transactionDto.getAmount() + incentive); // Add incentive to
                                                                                               // recipient

        // Step 4: Create and save the transaction record with the incentive
        TransactionRecord record = new TransactionRecord(transactionDto.getAmount(), sender, recipient, incentive);
        transactionRecordRepository.save(record);

        LOG.info("Successfully processed transaction with incentive: {}", transactionDto);
    }
}
