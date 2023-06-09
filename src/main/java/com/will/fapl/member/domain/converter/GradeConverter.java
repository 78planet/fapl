package com.will.fapl.member.domain.converter;

import com.will.fapl.member.domain.Grade;
import jakarta.persistence.AttributeConverter;
import java.util.stream.Stream;

public class GradeConverter implements AttributeConverter<Grade, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Grade grade) {
        if (grade == null) {
            return null;
        }
        return grade.getCode();
    }

    @Override
    public Grade convertToEntityAttribute(Integer dbData) {
        if (dbData == null){
            return null;
        }

        return Stream.of(Grade.values())
            .filter(c -> c.getCode().equals(dbData))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
