package nova.mysub.domain.user;

import nova.mysub.domain.user.model.dto.UserDto;
import nova.mysub.domain.user.repository.UserRepository;
import nova.mysub.domain.user.service.UserService;
import nova.mysub.domain.user.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1L, "user1", "user1@example.com", "http://example.com/profile1.jpg", "USER", 12345L);
        User user2 = new User(2L, "user2", "user2@example.com", "http://example.com/profile2.jpg", "USER", 12346L);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserDto> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    public void testGetUserById() {
        User user = new User(1L, "user1", "user1@example.com", "http://example.com/profile1.jpg", "USER", 12345L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDto> userDto = userService.getUserById(1L);

        assertTrue(userDto.isPresent());
        assertEquals("user1", userDto.get().getUsername());
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto(null, "user1", "user1@example.com", "http://example.com/profile1.jpg", "USER", 12345L);
        User user = new User(null, "user1", "user1@example.com", "http://example.com/profile1.jpg", "USER", 12345L);
        User savedUser = new User(1L, "user1", "user1@example.com", "http://example.com/profile1.jpg", "USER", 12345L);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser.getId());
        assertEquals("user1", createdUser.getUsername());
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User(1L, "user1", "user1@example.com", "http://example.com/profile1.jpg", "USER", 12345L);
        UserDto userDto = new UserDto(null, "user2", "user2@example.com", "http://example.com/profile2.jpg", "USER", 12346L);
        User updatedUser = new User(1L, "user2", "user2@example.com", "http://example.com/profile2.jpg", "USER", 12346L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDto result = userService.updateUser(1L, userDto);

        assertEquals("user2", result.getUsername());
        assertEquals("user2@example.com", result.getEmail());
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}