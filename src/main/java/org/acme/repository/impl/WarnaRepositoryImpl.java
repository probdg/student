package org.acme.repository.impl;

import java.util.List;
import java.util.Map;

import org.acme.command.WarnaCmd;
import org.acme.dto.WarnaDto;
import org.acme.entity.Varian;
import org.acme.entity.Warna;
import org.acme.repository.WarnaRepository;
import org.acme.response.ApiResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarnaRepositoryImpl implements WarnaRepository {

    @Override
    public Map<String, Object> createWarna(WarnaCmd req) {
        Warna warna = new Warna();
        warna.setName(req.name());
        Varian varians = Varian.findById(req.idVarian());
        if (varians == null) {
            return Map.of("error", "Varian not found");
        } else {
            warna.setVarian(varians);
            Warna.persist(warna);
            return Map.of("id", warna.getId(), "name", warna.getName(), "varianName", warna.getVarian().getName());
        }

    }

    @Override
    public ApiResponse<WarnaDto> getWarnaByVarian(Long idVarian) {
        Varian varian = Varian.findById(idVarian);
        if (varian == null) {
            return new ApiResponse<WarnaDto>(404, null, "Varian not found", false);
        } else {
            List<Warna> warnas = Warna.find("varian", varian).list();
            List<WarnaDto> data = warnas.stream()
                    .map(warna -> new WarnaDto(warna.getId(), warna.getName(), warna.getVarian().getName()))
                    .toList();
            return new ApiResponse<WarnaDto>(200, data, "success", true);
        }
    }

}
