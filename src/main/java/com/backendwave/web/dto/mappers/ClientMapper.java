package com.backendwave.web.dto.mappers;

import com.backendwave.data.entities.Utilisateur;
import com.backendwave.web.dto.request.users.CreateClientDto;
import com.backendwave.web.dto.response.users.CreateClientDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ClientMapper extends com.backendwave.web.dto.mappers.Mapper<CreateClientDtoResponse, Utilisateur> {
    
    @Mapping(target = "roleNom", source = "role.nom")
    CreateClientDtoResponse toDto(Utilisateur entity);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codeQr", ignore = true)
    @Mapping(target = "estActif", ignore = true)
    @Mapping(target = "password", ignore = true)  // Ignorer le mot de passe 
    @Mapping(target = "solde", ignore = true)
    Utilisateur toEntity(CreateClientDtoResponse dto);

    @Named("fromCreateClientDto")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "codeQr", ignore = true)
    @Mapping(target = "estActif", constant = "true")
    @Mapping(target = "solde", expression = "java(java.math.BigDecimal.ZERO)")
    Utilisateur createClientDtoToEntity(CreateClientDto createClientDto);
}