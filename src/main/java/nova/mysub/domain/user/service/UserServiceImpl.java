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

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = convertToEntity(userDto);
        user = userRepository.save(user);
        return convertToDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setProfileImageUrl(userDto.getProfileImageUrl());
        user.setKakaoId(userDto.getKakaoId());
        user = userRepository.save(user);
        return convertToDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getProfileImageUrl(), user.getRole(), user.getKakaoId());  // kakaoId 포함
    }

    private User convertToEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getUsername(), userDto.getEmail(), userDto.getProfileImageUrl(), userDto.getRole(), userDto.getKakaoId());  // kakaoId 포함
    }
}
