package com.spring.demo.user;

import com.spring.demo.core.error.ExceptionApi400;
import com.spring.demo.core.error.ExceptionApi404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse.UsersDTO getUsers(UserRequest.JoinDTO joinDTO){

        Optional<User> userOP= userRepository.findByUserName(joinDTO.getName());
        if(userOP.isPresent()) {
            throw new ExceptionApi400("이미 존재하는 유저입니다.");
        }
        User userPS = userRepository.save(joinDTO.toEntity());

        List<Integer> users= userRepository.findUsersId();
        return new UserResponse.UsersDTO(users);
    }

    public UserResponse.UserDTO getUser(int id){
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new ExceptionApi404("유저가 존재하지 않습니다."));
        return new UserResponse.UserDTO(user);
    }

    @Transactional
    public UserResponse.UserDTO updateUser(int id, UserRequest.UserDTO userDTO){
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new ExceptionApi404("유저가 존재하지 않습니다."));

        user.setName(userDTO.getName());

        return new UserResponse.UserDTO(user);
    }
}
