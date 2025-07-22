package com.courier.payment;

import com.courier.payment.dto.PaymentConfirmRequest;
import com.courier.payment.dto.PaymentConfirmResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<PaymentConfirmResponse> confirm(@RequestBody PaymentConfirmRequest request) {
        paymentService.confirm(request);
        log.info("paymentConfirmDTO: {}", request);
        return ResponseEntity.ok(PaymentConfirmResponse.builder()
                .result(true)
                .build());
    }

    @GetMapping("/status")
    public void status() {

    }
}
