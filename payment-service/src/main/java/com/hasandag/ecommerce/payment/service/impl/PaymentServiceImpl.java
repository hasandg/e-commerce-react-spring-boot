package com.hasandag.ecommerce.payment.service.impl;

import com.hasandag.ecommerce.payment.dto.PaymentRequest;
import com.hasandag.ecommerce.payment.dto.PaymentResponse;
import com.hasandag.ecommerce.payment.exception.PaymentNotFoundException;
import com.hasandag.ecommerce.payment.exception.PaymentProcessingException;
import com.hasandag.ecommerce.payment.mapper.PaymentMapper;
import com.hasandag.ecommerce.payment.model.Payment;
import com.hasandag.ecommerce.payment.model.PaymentStatus;
import com.hasandag.ecommerce.payment.repository.PaymentRepository;
import com.hasandag.ecommerce.payment.service.PaymentService;
import com.hasandag.ecommerce.payment.service.processor.PaymentProcessor;
import com.hasandag.ecommerce.payment.service.processor.PaymentProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProcessorFactory paymentProcessorFactory;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        Payment payment = paymentMapper.toEntity(paymentRequest);
        
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Created payment with ID: {} for order: {}", savedPayment.getId(), paymentRequest.getOrderId());
        
        return paymentMapper.toDto(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(String paymentId) {
        Payment payment = findPaymentById(paymentId);
        
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentProcessingException("Payment cannot be processed because it is in " + payment.getStatus() + " state");
        }
        
        payment.setStatus(PaymentStatus.PROCESSING);
        paymentRepository.save(payment);
        
        try {
            PaymentProcessor processor = paymentProcessorFactory.getProcessor(payment.getPaymentMethod());
            String transactionId = processor.processPayment(payment);
            
            payment.setTransactionId(transactionId);
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setCompletedAt(LocalDateTime.now());
            
            Payment updatedPayment = paymentRepository.save(payment);
            log.info("Successfully processed payment with ID: {}, transaction ID: {}", paymentId, transactionId);
            
            return paymentMapper.toDto(updatedPayment);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setErrorMessage(e.getMessage());
            Payment updatedPayment = paymentRepository.save(payment);
            
            log.error("Failed to process payment with ID: {}", paymentId, e);
            throw new PaymentProcessingException("Payment processing failed: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentResponse getPaymentById(String id) {
        Payment payment = findPaymentById(id);
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order ID: " + orderId));
        return paymentMapper.toDto(payment);
    }

    @Override
    public Page<PaymentResponse> getPaymentsByUserId(String userId, Pageable pageable) {
        return paymentRepository.findByUserId(userId, pageable)
                .map(paymentMapper::toDto);
    }

    @Override
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getPaymentsByUserIdAndStatus(String userId, PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByUserIdAndStatus(userId, status);
        return payments.stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findByCreatedAtBetween(startDate, endDate);
        return payments.stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(String id, PaymentStatus status) {
        Payment payment = findPaymentById(id);
        payment.setStatus(status);
        
        if (status == PaymentStatus.COMPLETED) {
            payment.setCompletedAt(LocalDateTime.now());
        }
        
        Payment updatedPayment = paymentRepository.save(payment);
        log.info("Updated payment status to {} for payment ID: {}", status, id);
        
        return paymentMapper.toDto(updatedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(String id) {
        Payment payment = findPaymentById(id);
        
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new PaymentProcessingException("Can only refund completed payments");
        }
        
        try {
            PaymentProcessor processor = paymentProcessorFactory.getProcessor(payment.getPaymentMethod());
            processor.refundPayment(payment);
            
            payment.setStatus(PaymentStatus.REFUNDED);
            Payment updatedPayment = paymentRepository.save(payment);
            log.info("Successfully refunded payment with ID: {}", id);
            
            return paymentMapper.toDto(updatedPayment);
        } catch (Exception e) {
            log.error("Failed to refund payment with ID: {}", id, e);
            throw new PaymentProcessingException("Payment refund failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deletePayment(String id) {
        Payment payment = findPaymentById(id);
        paymentRepository.delete(payment);
        log.info("Deleted payment with ID: {}", id);
    }
    
    private Payment findPaymentById(String id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + id));
    }
} 