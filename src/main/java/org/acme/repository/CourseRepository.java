package org.acme.repository;

import java.util.Map;

import org.acme.dto.CourseDto;
import org.acme.entity.Course;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface CourseRepository extends PanacheRepository<Course> {
    Map<String, Object> createCourse(CourseDto courseDto);

}
