package com.jpmc.midascore.client;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.model.IncentiveResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveApiClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    public float getIncentive(Transaction transaction) {
        try {
            IncentiveResponse response = restTemplate.postForObject(INCENTIVE_API_URL, transaction,
                    IncentiveResponse.class);
            return (float) ((response != null) ? response.getAmount() : 0.0);
        } catch (Exception e) {
            return (float) 0.0;
        }
    }
}
