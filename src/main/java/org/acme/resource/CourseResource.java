package org.acme.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.acme.dto.CourseDto;
import org.acme.entity.Course;
import org.acme.repository.CourseRepository;
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

@Path("courses")
public class CourseResource {
    @Inject
    CourseRepository courseRepository;

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public ApiResponse<CourseDto> getCourses() {
        List<Course> Courses = courseRepository.listAll();

        List<CourseDto> data = Courses.stream()
                .map(Course -> new CourseDto(Course.getId(), Course.getName()))
                .toList();
        String message = "success";
        boolean success = true;

        return new ApiResponse<CourseDto>(200, data, message, success);

    }

    @POST
    @Transactional
    @Produces("application/json")
    @Consumes("application/json")
    public Response storeCourse(CourseDto req) {
        Map<String, Object> data = courseRepository.createCourse(req);
        return Response.ok(data).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getCourse(@PathParam("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<Course> CourseOptional = courseRepository.findByIdOptional(id);

        if (CourseOptional.isPresent()) {
            Course Course = CourseOptional.get();
            CourseDto data = new CourseDto(Course.getId(), Course.getName());
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
    public Response updateCourse(@PathParam("id") Long id, CourseDto req) {
        Optional<Course> CourseOptional = courseRepository.findByIdOptional(id);
        if (CourseOptional.isPresent()) {
            Course Course = CourseOptional.get();
            Course.setName(req.name());
            courseRepository.persist(Course);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Produces("application/json")

    public Response deleteCourse(@PathParam("id") Long id) {
        Optional<Course> CourseOptional = courseRepository.findByIdOptional(id);

        if (CourseOptional.isPresent()) {
            Course Course = CourseOptional.get();
            Course.setDeleted(true);
            courseRepository.persist(Course);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
