package nova.mysub.domain.user.service;

import nova.mysub.domain.user.model.dto.UserDto;
import nova.mysub.domain.user.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getAllUsers();
    Optional<UserDto> getUserById(Long id);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);

}
