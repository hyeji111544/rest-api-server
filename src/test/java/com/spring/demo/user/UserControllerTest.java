package com.spring.demo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.demo.core.error.ExceptionApi400;
import com.spring.demo.core.error.ExceptionApi404;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    @Test
    void save_ok_Test() throws Exception {
        UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
        joinDTO.setName("testUser");

        List<Integer> userIds = Arrays.asList(1, 2);
        when(userService.getUsers(any(UserRequest.JoinDTO.class)))
                .thenReturn(new UserResponse.UsersDTO(userIds));

        // API 호출 및 검증
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users[0].id").value(1))
                .andExpect(jsonPath("$.users[1].id").value(2));
    }

    @Test
    void save_fail_Test() throws Exception {
        UserRequest.JoinDTO joinDTO = new UserRequest.JoinDTO();
        joinDTO.setName("user1"); // 존재하는 사용자 이름

        when(userService.getUsers(any(UserRequest.JoinDTO.class)))
                .thenThrow(new ExceptionApi400("이미 존재하는 유저입니다."));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value("이미 존재하는 유저입니다."));
    }

    @Test
    void get_ok_Test() throws Exception {
        int userId = 1; // 조회할 사용자 ID

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("user1");

        UserResponse.UserDTO existingUserDTO = new UserResponse.UserDTO(existingUser); // User로부터 DTO 생성
        when(userService.getUser(userId)).thenReturn(existingUserDTO);

        // API 호출 및 검증
        mockMvc.perform(get("/users/{id}", userId) // GET 요청
                        .accept(MediaType.APPLICATION_JSON)) // 응답 타입 설정
                .andExpect(status().isOk()) // 200 OK 상태 확인
                .andExpect(jsonPath("$.id").value(userId)) // ID 확인
                .andExpect(jsonPath("$.name").value("user1")); // 이름 확인
    }

    @Test
    void get_fail_Test() throws Exception {
        int userId = 2; // 존재하지 않는 사용자 ID

        when(userService.getUser(userId)).thenThrow(new ExceptionApi404("유저가 존재하지 않습니다."));

        // API 호출 및 예외 검증
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value("유저가 존재하지 않습니다."));// 예외 메시지 확인
    }

    @Test
    void update_ok_Test() throws Exception {
        // 업데이트할 User 객체 생성
        User user = new User();
        user.setId(1);
        user.setName("user1Update");

        // Mocking: userService.updateUser 메서드가 반환할 UserResponse.UserDTO 객체 생성
        UserResponse.UserDTO updatedUserDTO = new UserResponse.UserDTO(user);

        // Mocking: userService.updateUser 메서드가 호출될 때 반환하도록 설정
        when(userService.updateUser(eq(1), any(UserRequest.UserDTO.class))).thenReturn(updatedUserDTO);


        // PUT 요청을 수행하고 응답 검증
        mockMvc.perform(put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("user1Update"));

    }


    @Test
    void urlFilter_Test() throws Exception {
        String invalidUrl = "/users/1?name=test!!"; // 유효하지 않은 URL

        mockMvc.perform(get(invalidUrl))
                .andExpect(status().isBadRequest()) // 400 상태 확인
                .andExpect(jsonPath("$.reason").value("유효하지 않은 문자가 주소에 포함되어 있습니다."));
    }
}