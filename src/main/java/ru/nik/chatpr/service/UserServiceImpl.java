package ru.nik.chatpr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.nik.chatpr.model.User;
import ru.nik.chatpr.repository.UserRepository;
import ru.nik.chatpr.util.EntityMapper;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.nik.chatpr.util.ValidationUtil.checkNotFound;
import static ru.nik.chatpr.util.ValidationUtil.checkNotFoundWithId;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Sort SORT_EMAIL = Sort.by(Sort.Direction.ASC, "email");
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User create(User user) {
        checkNotFound(user, "user must not be null");
        return prepareAndSave(user);
    }

    @Override
    public void delete(long id) {
        log.debug("Delete user by id: {}", id);
        repository.deleteById(id);
    }

    @Override
    public User getById(long id) {
        log.debug("Get user by id: {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @Override
    public User getByEmail(@NotNull String email) {
        log.debug("Get user by email {}", email);
        return checkNotFound(repository.findByEmail(email).orElse(null), "email=" + email);
    }

    @Override
    public List<User> getAll() {
        log.debug("Get all users");
        return repository.findAll(SORT_EMAIL);
    }

    @Override
    public void update(User user) {
        log.debug("update user by user {}", user);
        User existed = getById(user.getId());
        existed.setEmail(user.getEmail());
        existed.setFirstName(user.getFirstName());
        existed.setLastName(user.getLastName());
        existed.setGender(user.getGender());
        existed.setRoles(user.getRoles());
        existed.setPhone(user.getPhone());
        repository.save(user);
    }

    @Override
    public void enable(long id, boolean enabled) {
        log.debug(enabled ? "enable {}" : "disable {}", id);
        User user = getById(id);
        user.setEnabled(enabled);
        repository.save(user);
    }

    @Override
    public boolean isExist(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Load user by user email {}", email);
        User user = repository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " is not found"));
        return EntityMapper.toUserPrincipal(user);
    }

    private User prepareAndSave(User user) {
        log.debug("prepare and save {}", user);
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return repository.save(user);
    }
}
