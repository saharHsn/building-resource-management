package tech.builtrix.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dto.UserDto;
import tech.builtrix.dto.emailToken.RegisterUserDto;
import tech.builtrix.exception.*;
import tech.builtrix.model.user.Role;
import tech.builtrix.model.user.TokenPurpose;
import tech.builtrix.model.user.User;
import tech.builtrix.model.user.UserToken;
import tech.builtrix.repository.user.UserRepository;
import tech.builtrix.repository.user.UserTokenRepository;
import tech.builtrix.service.authenticate.CodeService;
import tech.builtrix.service.authenticate.ValidationService;
import tech.builtrix.util.HashUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;


@Component
@Slf4j
public class UserService extends GenericCrudServiceBase<User, UserRepository> {
    private final CodeService codeService;
    private final ValidationService validationService;

    private final UserTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    // API
    public String getUser(final String userToken) {
        final UserToken token = tokenRepository.findByToken(userToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        repository.save(user);
    }

    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public String generateQRUrl(User user) throws UnsupportedEncodingException {
        String APP_NAME = "Metrics";
        String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmailAddress(), user.getSecret(), APP_NAME), "UTF-8");
    }

   /* public User updateUser2FA(boolean use2FA) {
        final Authentication curAuth = SecurityContextHolder.getContext()
                .getAuthentication();
        User currentUser = (User) curAuth.getPrincipal();
        //currentUser.setUsing2FA(use2FA);
        currentUser = repository.save(currentUser);
        final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), curAuth.getAuthorities());
        SecurityContextHolder.getContext()
                .setAuthentication(auth);
        return currentUser;
    }*/

    private boolean emailExists(final String email) {
        return repository.findByEmailAddress(email) != null;
    }

    /*public List<String> getUsersFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals()
                .stream()
                .filter((u) -> !sessionRegistry.getAllSessions(u, false)
                        .isEmpty())
                .map(o -> {
                    if (o instanceof User) {
                        return ((User) o).getEmailAddress();
                    } else {
                        return o.toString();
                    }
                })
                .collect(Collectors.toList());

    }*/

    @Autowired
    public UserService(UserRepository repository,
                       CodeService codeService,
                       ValidationService validationService,
                       PasswordEncoder passwordEncoder,
                       UserTokenRepository tokenRepository) {
        super(repository);
        this.codeService = codeService;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }


    public UserDto findById(String id) throws NotFoundException {
        Optional<User> optionalUser = this.repository.findById(id);
        if (optionalUser.isPresent()) {
            return new UserDto(optionalUser.get());
        } else {
            throw new NotFoundException("user", "id", id);
        }
    }

    public User findByEmail(String email) throws NotFoundException {
        List<User> users = this.repository.findByEmailAddress(email);
        if (!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        } else {
            throw new NotFoundException("user", "email", email);
        }
    }

    public User save(UserDto userDto) {
        User user = new User(userDto);
        user = this.repository.save(user);
        return user;
    }

    public void update(UserDto user) {
        save(user);
    }

    public void delete(String userId) {
        this.repository.deleteById(userId);
    }

    public User registerNewUserAccount(final RegisterUserDto registerUserDto) throws AlreadyExistException {
        if (emailExists(registerUserDto.getEmailAddress())) {
            throw new AlreadyExistException("There is an account with that email address: " + registerUserDto.getEmailAddress());
        }
        final User user = new User();
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        //user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setPassword(HashUtil.sha1(registerUserDto.getPassword()));
        user.setEmailAddress(registerUserDto.getEmailAddress());
        //TODO
        /*user.setUsing2FA(registerUserDto.isUsing2FA());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));*/
        return repository.save(user);
    }

    public String registerUser(String firstName,
                               String lastName,
                               String email,
                               String password,
                               String confirmPassword,
                               String sessionKey) throws ExceptionBase {
        User user = updateUserByEmail(firstName, lastName, email, password, confirmPassword);
        UserToken userToken;
        userToken = this.codeService.createToken(user, TokenPurpose.Register);
        return userToken.getToken();
    }

    private User updateUserByEmail(String firstName,
                                   String lastName,
                                   String email,
                                   String password,
                                   String confirmPassword) throws ExceptionBase {
        //just reload user
        User user = null;
        try {
            user = this.findByEmail(email);
        } catch (NotFoundException e) {
            logger.info("User does not exist");
        }
        if (user != null) {
            throw new AlreadyExistException("emailAddress", email);
        }
        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordConfirmationException();
        }
        if (!this.validationService.validateEmail(email)) {
            throw new InvalidEmailException();
        }
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmailAddress(email);
        userDto.setRole(Role.User);
        String hashedPass = HashUtil.sha1(password);
        userDto.setPassword(hashedPass);
        return this.save(userDto);
    }

    public void enableUser(String userId) throws NotFoundException {
        User user = this.getById(userId);
        user.setEnabled(true);
        this.repository.save(user);
    }


   /* @Transactional
    public void addSuccessfulLogin(User user, String reason) {
        User dbUser = this.repository.findById(user.getId()).get();
        UserLogin login = new UserLogin();
        login.setUser(dbUser);
        login.setMessage(reason);
        login.setStatus(true);
        dbUser.setFailedLogin((byte) 0);
        dbUser.setLockedOutEndTime(null);
        dbUser.getLogins().add(login);
        this.repository.save(dbUser);
    }

    @Transactional
    public void addFailedLogin(User user, String reason) {
        User dbUser = this.repository.findById(user.getId()).get();
        UserLogin login = new UserLogin();
        login.setUser(dbUser);
        login.setMessage(reason);
        login.setStatus(false);
        dbUser.getLogins().add(login);
        dbUser.setFailedLogin((byte) (dbUser.getFailedLogin() + 1));
        if (dbUser.getFailedLogin() > MaxFailedLogin) {
            dbUser.setLockedOutEndTime(DateHelper.utcNowAddSeconds(LockedOutSeconds));
        }
        this.repository.save(dbUser);
    }*/
}
