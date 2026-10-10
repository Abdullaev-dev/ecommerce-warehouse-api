package az.ingress.e_commerce.service;

import az.ingress.e_commerce.dto.UserRequest;
import az.ingress.e_commerce.dto.UserResponse;
import az.ingress.e_commerce.entity.User;
import az.ingress.e_commerce.exception.ResourceNotFoundException;
import az.ingress.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest request) {

        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole()
                ))
                .toList();
    }

    public UserResponse getUserById(Long id) {

        User  user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(" User with id " + id + " not found."));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}
