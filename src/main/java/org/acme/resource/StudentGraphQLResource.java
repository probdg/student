package org.acme.resource;

import java.util.List;
import java.util.Optional;

import org.acme.dto.StudentDto;
import org.acme.entity.Student;
import org.acme.repository.StudentRepository;
import org.acme.response.ApiResponse;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@GraphQLApi
public class StudentGraphQLResource {

    @Inject
    StudentRepository studentRepository;

    @Query("students")
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

    @Mutation("createStudent")
    @Transactional
    public Student storeStudent(StudentDto req) {
        Student student = new Student();
        student.setName(req.name());
        student.setMajor(req.major());
        student.setPlaceOfBirth(req.placeOfBirth());
        student.setDateOfBirth(req.dateOfBirth());
        student.setHobbies(req.hobbies());

        studentRepository.persist(student);
        return student;
    }

    @Mutation("updateStudent")
    @Transactional
    public Student updateStudent(Long id, StudentDto req) {
        Optional<Student> studentOptional = studentRepository.findByIdOptional(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setName(req.name());
            student.setMajor(req.major());

            studentRepository.persist(student);
            return student;
        }

        return null;
    }

    @Mutation("deleteStudent")
    @Transactional
    public Student deleteStudent(Long id) {
        Optional<Student> studentOptional = studentRepository.findByIdOptional(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setDeleted(true);

            studentRepository.persist(student);
            return student;
        }

        return null;
    }

}
