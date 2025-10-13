package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AfterDateValidator implements ConstraintValidator<AfterDate, LocalDate> {
    private LocalDate date;

    @Override
    public void initialize(AfterDate constraintAnnotation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(constraintAnnotation.pattern());
        this.date = LocalDate.parse(constraintAnnotation.value(), formatter);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || value.isAfter(this.date);
    }
}