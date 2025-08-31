package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Balance getBalance(@RequestParam Long userId) {
        // Find the user in the database by their ID
        Optional<UserRecord> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            // If the user exists, get their balance
            UserRecord user = userOpt.get();
            float balanceAmount = user.getBalance();
            return new Balance(balanceAmount);
        } else {
            // If the user does not exist, return a balance of 0 as required
            return new Balance(0.0f);
        }
    }
}
