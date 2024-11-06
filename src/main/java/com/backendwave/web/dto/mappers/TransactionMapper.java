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

    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "statut", target = "status")
    @Mapping(source = "fraisTransfert", target = "transferFee")
    @Mapping(source = "expediteur.nomComplet", target = "senderFullName")
    @Mapping(source = "destinataire.nomComplet", target = "receiverFullName")
    @Mapping(source = "montant", target = "totalAmount")
    TransferResponseDto toDto(Transaction transaction);

    @Mapping(source = "transactionId", target = "id")
    @Mapping(source = "status", target = "statut")
    @Mapping(source = "transferFee", target = "fraisTransfert")
    @Mapping(source = "senderFullName", target = "expediteur.nomComplet")
    @Mapping(source = "receiverFullName", target = "destinataire.nomComplet")
    @Mapping(source = "totalAmount", target = "montant")
    Transaction toEntity(TransferResponseDto dto);

    List<TransferResponseDto> toDtoList(List<Transaction> entityList);

    List<Transaction> toEntityList(List<TransferResponseDto> dtoList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction updateEntityFromDto(TransferResponseDto dto, @MappingTarget Transaction entity);
}
