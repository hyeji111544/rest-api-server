package com.spring.demo.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

public class UserRequest {

    @Data
    public static class JoinDTO{
        @NotEmpty
        private String name;

        public User toEntity() {
            return User.builder().name(name).build();
        }
    }

    @Data
    public static class UserDTO{
        @NotEmpty
        private Integer id;
        @NotEmpty
        private String name;
    }
}
