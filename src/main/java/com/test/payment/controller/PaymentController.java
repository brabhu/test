package com.test.payment.controller;

import com.test.payment.model.Payment;
import com.test.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PaymentController {

    private static List<Payment> payments = new ArrayList<>();

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payments/{amount}/types/{type}/start/{startDate}/end/{endDate}")
    public Payment retrieveInvoices(@PathVariable String amount, @PathVariable String type,
    @PathVariable String startDate, @PathVariable String endDate){
        return paymentService.retrievePayments(amount, type, startDate, endDate);
    }

    @PostMapping("/payments/{amount}/types/{type}/start/{startDate}/end/{endDate}")
    public ResponseEntity<Void> registerStudentForCourse(@PathVariable String amount, @RequestBody String type,
                                                         @RequestBody String startDate, @RequestBody String endDate) {

        Payment payment = paymentService.retrievePayments(amount, type, startDate, endDate);

        if (payment == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(payment.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}

