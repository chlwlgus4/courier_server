package com.courier.payment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/payment")
@RestController
public class PaymentController {

    @PostMapping("/confirm")
    public void confirm() {

    }

    @GetMapping("/status")
    public void status() {

    }
}
