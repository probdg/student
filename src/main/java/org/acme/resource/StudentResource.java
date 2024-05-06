package org.acme.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.acme.dto.ScheduleDto;
import org.acme.dto.StudentAddrresDto;
import org.acme.dto.StudentCourseDto;
import org.acme.dto.StudentDto;
import org.acme.dto.StudentWithDetailDto;
import org.acme.entity.Student;
import org.acme.repository.StudentRepository;
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

@Path("students")
public class StudentResource {
    @Inject
    StudentRepository studentRepository;

    @GET
    @Produces("application/json")
    @Consumes("application/json")
    public ApiResponse<StudentDto> getStudents() {
        List<Student> students = studentRepository.listAll();

        List<StudentDto> data = students.stream()
                .map(student -> new StudentDto(student.getId(), student.getName(), student.getMajor(),
                        student.getPlaceOfBirth(), student.getDateOfBirth(), student.getHobbies()))
                .toList();
        String message = "success";
        boolean success = true;

        return new ApiResponse<StudentDto>(200, data, message, success);

    }

    @GET
    @Path("/detail")
    @Produces("application/json")
    @Consumes("application/json")
    public ApiResponse<StudentWithDetailDto> getStudentsWithDetail() {
        List<Student> students = studentRepository.listAll();

        List<StudentWithDetailDto> data = students.stream().map(student -> {
            List<StudentAddrresDto> studentAddresses = student.getAddresses().stream()
                    .map(addresses -> new StudentAddrresDto(addresses.getId(),
                            addresses.getStudent().getId(),
                            addresses.getAddress()))
                    .toList();
            List<StudentCourseDto> studentCourses = student.getCourses().stream()
                    .map(course -> {
                        List<ScheduleDto> schedules = course.getSchedules().stream().map(schedule -> {
                            return new ScheduleDto(schedule.getId(), schedule.getCourse().getId(), schedule.getDay());
                        }).toList();
                        return new StudentCourseDto(course.getId(),
                                course.getStudent().getId(),
                                course.getName(), schedules);
                    })
                    .toList();
            return new StudentWithDetailDto(student.getId(), student.getName(), student.getMajor(), studentAddresses,
                    studentCourses);
        }).toList();
        String message = "success";
        boolean success = true;

        return new ApiResponse<StudentWithDetailDto>(200, data, message, success);

    }

    @POST
    @Transactional
    @Produces("application/json")
    @Consumes("application/json")
    public Response storeStudent(StudentDto req) {
        Student student = new Student();
        student.setName(req.name());
        student.setMajor(req.major());
        student.setPlaceOfBirth(req.placeOfBirth());
        student.setDateOfBirth(req.dateOfBirth());
        student.setHobbies(req.hobbies());

        studentRepository.persist(student);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response getStudent(@PathParam("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<Student> studentOptional = studentRepository.findByIdOptional(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            StudentDto data = new StudentDto(student.getId(), student.getName(), student.getMajor(),
                    student.getPlaceOfBirth(), student.getDateOfBirth(), student.getHobbies());
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
    public Response updateStudent(@PathParam("id") Long id, StudentDto req) {
        Optional<Student> studentOptional = studentRepository.findByIdOptional(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setName(req.name());
            student.setMajor(req.major());

            studentRepository.persist(student);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Produces("application/json")

    public Response deleteStudent(@PathParam("id") Long id) {
        Optional<Student> studentOptional = studentRepository.findByIdOptional(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setDeleted(true);

            studentRepository.persist(student);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
