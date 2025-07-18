package com.courier.payment;

import com.courier.payment.dto.PaymentConfirmRequest;
import com.courier.payment.dto.PaymentConfirmResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/payment")
@RestController
public class PaymentController {

    @PostMapping("/confirm")
    public ResponseEntity<PaymentConfirmResponse> confirm(@RequestBody PaymentConfirmRequest dto) {
        log.info("paymentConfirmDTO: {}", dto);
        return ResponseEntity.ok(PaymentConfirmResponse.builder()
                .result(true)
                .build());
    }

    @GetMapping("/status")
    public void status() {

    }
}
