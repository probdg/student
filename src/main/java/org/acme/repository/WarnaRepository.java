package org.acme.repository;

import java.util.Map;

import org.acme.command.WarnaCmd;
import org.acme.dto.WarnaDto;
import org.acme.entity.Warna;
import org.acme.response.ApiResponse;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface WarnaRepository extends PanacheRepository<Warna> {
    Map<String, Object> createWarna(WarnaCmd WarnaDto);

    ApiResponse<WarnaDto> getWarnaByVarian(Long idVarian);

}
