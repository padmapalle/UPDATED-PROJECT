package com.financialapp.entity;

import org.junit.jupiter.api.Test;

import com.financialapp.entity.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void lombokAnnotations_ShouldWork() {
        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setAppAdmin(false);
        user.setPoints(100);

        assertThat(user.getUserId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPoints()).isEqualTo(100);
    }
}