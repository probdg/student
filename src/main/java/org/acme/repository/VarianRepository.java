package org.acme.repository;

import java.util.Map;

import org.acme.dto.VarianDto;
import org.acme.entity.Varian;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface VarianRepository extends PanacheRepository<Varian> {
    Map<String, Object> createVarian(VarianDto VarianDto);

}
