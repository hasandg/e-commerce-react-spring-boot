package com.hasandag.ecommerce.payment.controller;

import com.hasandag.ecommerce.payment.dto.PaymentRequest;
import com.hasandag.ecommerce.payment.dto.PaymentResponse;
import com.hasandag.ecommerce.payment.model.PaymentStatus;
import com.hasandag.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Creating payment for order: {}", paymentRequest.getOrderId());
        PaymentResponse createdPayment = paymentService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }
    
    @PostMapping("/{id}/process")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable String id) {
        log.info("Processing payment with ID: {}", id);
        PaymentResponse processedPayment = paymentService.processPayment(id);
        return ResponseEntity.ok(processedPayment);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String id) {
        log.info("Fetching payment with ID: {}", id);
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId) {
        log.info("Fetching payment for order: {}", orderId);
        PaymentResponse payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PaymentResponse>> getPaymentsByUserId(
            @PathVariable String userId,
            Pageable pageable) {
        log.info("Fetching payments for user: {}", userId);
        Page<PaymentResponse> payments = paymentService.getPaymentsByUserId(userId, pageable);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        log.info("Fetching payments with status: {}", status);
        List<PaymentResponse> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUserIdAndStatus(
            @PathVariable String userId,
            @PathVariable PaymentStatus status) {
        log.info("Fetching payments for user: {} with status: {}", userId, status);
        List<PaymentResponse> payments = paymentService.getPaymentsByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentResponse>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Fetching payments between dates: {} and {}", startDate, endDate);
        List<PaymentResponse> payments = paymentService.getPaymentsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(payments);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable String id,
            @RequestParam PaymentStatus status) {
        log.info("Updating status to {} for payment with ID: {}", status, id);
        PaymentResponse updatedPayment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updatedPayment);
    }
    
    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable String id) {
        log.info("Refunding payment with ID: {}", id);
        PaymentResponse refundedPayment = paymentService.refundPayment(id);
        return ResponseEntity.ok(refundedPayment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {
        log.info("Deleting payment with ID: {}", id);
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
} 