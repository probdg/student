package org.acme.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.acme.dto.VarianDto;
import org.acme.entity.Varian;
import org.acme.repository.VarianRepository;
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

@Path("varian")
public class VarianResource {
    @Inject
    VarianRepository VarianRepository;

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public ApiResponse<VarianDto> getVarians() {
        List<Varian> Varians = VarianRepository.listAll();

        List<VarianDto> data = Varians.stream()
                .map(Varian -> new VarianDto(Varian.getId(), Varian.getName()))
                .toList();
        String message = "success";
        boolean success = true;

        return new ApiResponse<VarianDto>(200, data, message, success);

    }

    @POST
    @Transactional
    @Produces("application/json")
    @Consumes("application/json")
    public Response storeVarian(VarianDto req) {
        Map<String, Object> data = VarianRepository.createVarian(req);
        return Response.ok(data).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getVarian(@PathParam("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<Varian> VarianOptional = VarianRepository.findByIdOptional(id);

        if (VarianOptional.isPresent()) {
            Varian Varian = VarianOptional.get();
            VarianDto data = new VarianDto(Varian.getId(), Varian.getName());
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
    public Response updateVarian(@PathParam("id") Long id, VarianDto req) {
        Optional<Varian> VarianOptional = VarianRepository.findByIdOptional(id);
        if (VarianOptional.isPresent()) {
            Varian Varian = VarianOptional.get();
            Varian.setName(req.name());
            VarianRepository.persist(Varian);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Produces("application/json")

    public Response deleteVarian(@PathParam("id") Long id) {
        Optional<Varian> VarianOptional = VarianRepository.findByIdOptional(id);

        if (VarianOptional.isPresent()) {
            Varian Varian = VarianOptional.get();
            Varian.setDeleted(true);
            VarianRepository.persist(Varian);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
