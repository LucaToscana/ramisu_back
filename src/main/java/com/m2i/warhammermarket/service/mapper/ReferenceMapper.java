package com.m2i.warhammermarket.service.mapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import lombok.NonNull;

@Component
public class ReferenceMapper {

    @PersistenceContext
    private EntityManager entityManager;

    @ObjectFactory
    public <T> T map(@NonNull final Long id,  @TargetType Class<T> type) {
        return entityManager.getReference(type, id);
    }
}