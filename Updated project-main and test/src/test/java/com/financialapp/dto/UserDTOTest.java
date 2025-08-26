package com.financialapp.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDTOTest {
    private Validator validator;

    @BeforeEach
    void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    // ---------- EMAIL TESTS ----------
    @Test
    void whenEmailIsInvalid_ShouldFailValidation() {
        UserDTO dto = new UserDTO(1, "invalid-email", false, 100);
        var violations = validator.validate(dto);

        assertThat(violations).isNotEmpty(); // @Email violation
    }

    @Test
    void whenEmailIsBlank_ShouldFailValidation() {
        UserDTO dto = new UserDTO(2, "   ", false, 50);
        var violations = validator.validate(dto);

        assertThat(violations).isNotEmpty(); // @NotBlank violation
    }

    @Test
    void whenEmailIsNull_ShouldFailValidation() {
        UserDTO dto = new UserDTO(3, null, true, 10);
        var violations = validator.validate(dto);

        assertThat(violations).isNotEmpty(); // @NotBlank violation
    }

    @Test
    void whenEmailIsValid_ShouldPassValidation() {
        UserDTO dto = new UserDTO(4, "valid@example.com", true, 200);
        var violations = validator.validate(dto);

        assertThat(violations).isEmpty(); // No violations
    }

    // ---------- POINTS TESTS ----------
    @Test
    void whenPointsIsNegative_ShouldFailValidation() {
        UserDTO dto = new UserDTO(5, "test@example.com", false, -50);
        var violations = validator.validate(dto);

        assertThat(violations).isNotEmpty(); // @Min(0) violation
    }

    @Test
    void whenPointsIsNull_ShouldPassValidation_BecauseOptional() {
        UserDTO dto = new UserDTO(6, "nullpoints@example.com", false, null);
        var violations = validator.validate(dto);

        assertThat(violations).isEmpty(); // Null allowed
    }

    @Test
    void whenPointsIsZeroOrPositive_ShouldPassValidation() {
        UserDTO dtoZero = new UserDTO(7, "zero@example.com", false, 0);
        UserDTO dtoPositive = new UserDTO(8, "positive@example.com", false, 100);

        assertThat(validator.validate(dtoZero)).isEmpty();
        assertThat(validator.validate(dtoPositive)).isEmpty();
    }

    // ---------- APPADMIN + USERID ----------
    @Test
    void whenAppAdminIsNull_ShouldPassValidation() {
        UserDTO dto = new UserDTO(9, "admin@example.com", null, 10);
        var violations = validator.validate(dto);

        assertThat(violations).isEmpty(); // No constraint
    }

    @Test
    void whenUserIdIsNull_ShouldPassValidation() {
        UserDTO dto = new UserDTO(null, "idnull@example.com", false, 20);
        var violations = validator.validate(dto);

        assertThat(violations).isEmpty(); // No constraint
    }
}
