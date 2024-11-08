package com.backendwave.web.dto.request.transactions;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MultipleTransferRequestDto {
    private String senderPhoneNumber;
    private List<String> recipientPhoneNumbers;
    private BigDecimal amount;
    private String groupReference;
}