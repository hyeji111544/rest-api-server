package com.spring.demo.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class UserResponse {

    @Data
    public static class UsersDTO{
        private List<UserIdDTO> users = new ArrayList<>();

        public UsersDTO(List<Integer> users) {
            for (Integer id : users) {
                this.users.add(new UserIdDTO(id));
            }
        }
    }

    @Data
    public static class UserIdDTO {
        private Integer id;

        public UserIdDTO(Integer id) {
            this.id = id;
        }
    }

    @Data
    public static class UserDTO{
        private Integer id;
        private String name;

        public UserDTO(User user){
            this.id = user.getId();
            this.name = user.getName();
        }
    }
}
