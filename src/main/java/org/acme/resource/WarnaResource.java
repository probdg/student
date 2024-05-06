package org.acme.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.acme.command.WarnaCmd;
import org.acme.dto.WarnaDto;
import org.acme.entity.Warna;
import org.acme.repository.WarnaRepository;
import org.acme.response.ApiResponse;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("warna")
public class WarnaResource {
    @Inject
    WarnaRepository WarnaRepository;

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public ApiResponse<WarnaDto> getWarnas() {
        List<Warna> warnas = WarnaRepository.listAll();
        List<WarnaDto> data = warnas.stream()
                .map(warna -> new WarnaDto(warna.getId(), warna.getName(), warna.getVarian().getName()))
                .toList();
        String message = "success";
        boolean success = true;

        return new ApiResponse<WarnaDto>(200, data, message, success);

    }

    @GET
    @Path("/varian/{idVarian}")
    @Produces("application/json")
    @Consumes("application/json")
    public ApiResponse<WarnaDto> getWarnaByVarian(Long idVarian) {
        return WarnaRepository.getWarnaByVarian(idVarian);

    }

    @POST
    @Transactional
    @Produces("application/json")
    @Consumes("application/json")
    public Response storeWarna(WarnaCmd req) {
        Map<String, Object> data = WarnaRepository.createWarna(req);
        return Response.ok(data).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getWarna(@PathParam("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<Warna> WarnaOptional = WarnaRepository.findByIdOptional(id);

        if (WarnaOptional.isPresent()) {
            Warna Warna = WarnaOptional.get();
            WarnaDto data = new WarnaDto(Warna.getId(), Warna.getName(), Warna.getVarian().getName());
            String message = "success";
            boolean success = true;
            map.put("data", data);
            map.put("message", message);
            map.put("success", success);
            return Response.ok(map).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateWarna(@PathParam("id") Long id, WarnaDto req) {
        Optional<Warna> WarnaOptional = WarnaRepository.findByIdOptional(id);
        if (WarnaOptional.isPresent()) {
            Warna Warna = WarnaOptional.get();
            Warna.setName(req.name());
            WarnaRepository.persist(Warna);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Produces("application/json")

    public Response deleteWarna(@PathParam("id") Long id) {
        Optional<Warna> WarnaOptional = WarnaRepository.findByIdOptional(id);

        if (WarnaOptional.isPresent()) {
            Warna Warna = WarnaOptional.get();
            Warna.setDeleted(true);
            WarnaRepository.persist(Warna);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
