package org.acme.dto;

import java.util.List;

public record StudentCourseDto(Long id,
        Long id_student,
        String name,
        List<ScheduleDto> schedules) {
}
