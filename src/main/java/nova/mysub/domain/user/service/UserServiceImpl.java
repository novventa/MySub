package nova.mysub.domain.user.service;

import nova.mysub.domain.user.model.dto.UserDto;
import nova.mysub.domain.user.model.entity.User;
import nova.mysub.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    public UserDto createUser(UserDto userDto) {
        User user = convertToEntity(userDto);
        user = userRepository.save(user);
        return convertToDto(user);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user = User.builder()
                .id(id)
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .profileImageUrl(userDto.getProfileImageUrl())
                .role(userDto.getRole())
                .kakaoId(user.getKakaoId()) // Keep the existing kakaoId
                .build();
        user = userRepository.save(user);
        return convertToDto(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .build();
    }

    private User convertToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .profileImageUrl(userDto.getProfileImageUrl())
                .role(userDto.getRole())
                .build();
    }
}