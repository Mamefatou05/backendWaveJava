package com.backendwave.web.dto.mappers;

import com.backendwave.data.entities.Transaction;
import com.backendwave.web.dto.response.transactions.TransferResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "transaction.id", target = "transactionId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "fraisTransfert", target = "transferFee")
    TransferResponseDto toDto(TransferResponseDto transaction);

    @Mapping(source = "transactionId", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "transferFee", target = "fraisTransfert")
    Transaction toEntity(TransferResponseDto dto);

    List<TransferResponseDto> toDtoList(List<Transaction> entityList);

    List<Transaction> toEntityList(List<TransferResponseDto> dtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction updateEntityFromDto(TransferResponseDto dto, @MappingTarget Transaction entity);
}