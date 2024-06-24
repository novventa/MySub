package nova.mysub.domain.user;

import nova.mysub.domain.user.model.dto.UserDto;
import nova.mysub.domain.user.model.entity.User;
import nova.mysub.domain.user.repository.UserRepository;
import nova.mysub.domain.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setProfileImageUrl("http://example.com/image.jpg");
        user.setRole("USER");
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setProfileImageUrl("http://example.com/image.jpg");
        userDto.setRole("USER");

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<UserDto> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        verify(userRepository).findById(anyLong());
    }
    
}