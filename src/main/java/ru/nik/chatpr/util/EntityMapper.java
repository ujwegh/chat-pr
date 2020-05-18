package ru.nik.chatpr.util;

import org.springframework.security.core.GrantedAuthority;
import ru.nik.chatpr.dto.UserRegistrationDto;
import ru.nik.chatpr.model.enums.Role;
import ru.nik.chatpr.model.User;
import ru.nik.chatpr.security.UserPrincipal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EntityMapper {
    private EntityMapper() {
    }

    public static UserPrincipal toUserPrincipal(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>(user.getRoles());
        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword(), user.isEnabled(), authorities);
    }

    public static UserPrincipal toUserPrincipal(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = toUserPrincipal(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public static User fromUserRegistrationDto(UserRegistrationDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getName());
        user.setEmail(userDto.getUsername());
        user.setEnabled(true);
        user.setRoles(Collections.singletonList(Role.ROLE_USER));
        return user;
    }
}
