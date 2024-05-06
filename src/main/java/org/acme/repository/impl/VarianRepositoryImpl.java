package org.acme.repository.impl;

import java.util.Map;

import org.acme.dto.VarianDto;
import org.acme.entity.Varian;
import org.acme.repository.VarianRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VarianRepositoryImpl implements VarianRepository {

    @Override
    public Map<String, Object> createVarian(VarianDto req) {
        Varian varian = new Varian();
        varian.setName(req.name());
        Varian.persist(varian);
        return Map.of("id", varian.getId(), "name", varian.getName());

    }

}
