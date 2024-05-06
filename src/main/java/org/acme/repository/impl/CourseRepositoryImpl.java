package org.acme.repository.impl;

import java.util.Map;

import org.acme.dto.CourseDto;
import org.acme.entity.Course;
import org.acme.repository.CourseRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CourseRepositoryImpl implements CourseRepository {

    @Override
    public Map<String, Object> createCourse(CourseDto req) {
        Course course = new Course();
        course.setName(req.name());
        Course.persist(course);
        return Map.of("id", course.getId(), "name", course.getName());

    }

}
