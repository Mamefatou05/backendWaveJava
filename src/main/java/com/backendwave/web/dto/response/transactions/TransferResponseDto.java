package com.backendwave.web.dto.response.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.backendwave.data.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDto {
    private Long transactionId;
    private TransactionStatus status;
    private BigDecimal transferFee;
    private String senderFullName;
    private String receiverFullName;
    private BigDecimal totalAmount;
    private String message;
    private LocalDateTime timestamp;
}