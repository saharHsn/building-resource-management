package tech.builtrix.services.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.commons.EmailSender;
import tech.builtrix.exceptions.*;
import tech.builtrix.models.user.Role;
import tech.builtrix.models.user.TokenPurpose;
import tech.builtrix.models.user.User;
import tech.builtrix.models.user.VerificationToken;
import tech.builtrix.repositories.user.UserRepository;
import tech.builtrix.repositories.user.VerificationTokenRepository;
import tech.builtrix.services.authenticate.CodeService;
import tech.builtrix.services.authenticate.ValidationService;
import tech.builtrix.utils.HashUtil;
import tech.builtrix.web.dtos.emailToken.RegisterUserDto;
import tech.builtrix.web.dtos.user.InvitationDto;
import tech.builtrix.web.dtos.user.UserDto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@Slf4j
public class UserService extends GenericCrudServiceBase<User, UserRepository> {
    private static final String TOKEN_INVALID = "invalidToken";
    private static final String TOKEN_EXPIRED = "expired";
    private static final String TOKEN_VALID = "valid";

    @Value("${metrics.email.url}")
    private String emailUrl;
    @Value("${metrics.redirect.url}")
    private String redirectUrl;

    private final CodeService codeService;
    private final ValidationService validationService;
    private final EmailSender emailSender;
    private final VerificationTokenRepository tokenRepository;

    // API

    @Autowired
    public UserService(UserRepository repository,
                       CodeService codeService,
                       ValidationService validationService,
                       EmailSender emailSender,
                       VerificationTokenRepository tokenRepository) {
        super(repository);
        this.codeService = codeService;
        this.validationService = validationService;
        this.emailSender = emailSender;
        this.tokenRepository = tokenRepository;
    }


    public User findById(String id) throws NotFoundException {
        Optional<User> optionalUser = this.repository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
            //return new UserDto(optionalUser.get());
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

    public User update(UserDto userDto) throws NotFoundException {
        User oldUser = this.findById(userDto.getId());
        return this.repository.save(getUserFromDto(oldUser, userDto));
    }

    private User getUserFromDto(User oldUser, UserDto userDto) {
        oldUser.setFirstName(userDto.getFirstName());
        oldUser.setLastName(userDto.getLastName());
        oldUser.setJob(userDto.getJob());
        oldUser.setGender(userDto.getGender());
        oldUser.setEmailAddress(userDto.getEmailAddress());
        oldUser.setPhoneNumber(userDto.getPhone());
        oldUser.setBirthDate(userDto.getBirthDate());
        return oldUser;
    }

    public void delete(String userId) {
        this.repository.deleteById(userId);
    }

    public User registerNewUserAccount(final RegisterUserDto registerUserDto) throws AlreadyExistException {
        if (emailExists(registerUserDto.getEmailAddress())) {
            throw new AlreadyExistException("Email Address", registerUserDto.getEmailAddress());
        }
        final User user = new User();
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setPassword(HashUtil.sha1(registerUserDto.getPassword()));
        user.setEmailAddress(registerUserDto.getEmailAddress());
        user.setRole(Role.Junior);
        // sendConfirmationEmail(user);
        //user.setEnabled(true);
        //TODO
        /*user.setUsing2FA(registerUserDto.isUsing2FA());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));*/
        return repository.save(user);
    }

    public User registerUserViaInvitation(InvitationDto invitationDto, User parent) throws AlreadyExistException {
        if (emailExists(invitationDto.getInviteeEmail())) {
            throw new AlreadyExistException("There is an account with that email address: " + invitationDto.getInviteeEmail());
        }
        User user = new User();
        String randomPassword = codeService.generateRandomNumber(6);
        user.setPassword(HashUtil.sha1(randomPassword));
        user.setEmailAddress(invitationDto.getInviteeEmail());
        user.setParent(parent.getId());
        user.setRole(Role.Junior);
        //TODO
        /*user.setUsing2FA(registerUserDto.isUsing2FA());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));*/
        user = repository.save(user);
        user.setRawPassword(randomPassword);
        return user;
    }

    public String registerUser(String firstName,
                               String lastName,
                               String email,
                               String password,
                               String confirmPassword,
                               String sessionKey) throws ExceptionBase {
        User user = updateUserByEmail(firstName, lastName, email, password, confirmPassword);
        VerificationToken userToken;
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


   
   /* public User registerNewUserAccount(final UserDto accountDto) {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email adress: " + accountDto.getEmail());
        }
        final User user = new User();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setUsing2FA(accountDto.isUsing2FA());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return repository.save(user);
    }*/

    public User getUser(final String userToken) throws NotFoundException {
        final VerificationToken token = tokenRepository.findByToken(userToken);
        if (token != null) {
            return findById(token.getUser());
        }
        return null;
    }

    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }


    public void saveRegisteredUser(final User user) {
        repository.save(user);
    }

   
   /* public void deleteUser(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        repository.delete(user);
    }*/


    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }


    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
                .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

   
   /* public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }*/


   /* public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }*/

   
   /* public User getUserByPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token)
                .getUser();
    }*/


    public void changeUserPassword(final User user, final String password) {
        user.setPassword(HashUtil.sha1(password));
        repository.save(user);
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
        return !CollectionUtils.isEmpty(repository.findByEmailAddress(email));
    }

    public String validateVerificationToken(String token) throws NotFoundException {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = findById(verificationToken.getUser());
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime()
                - cal.getTime()
                .getTime()) <= 0) {
            // tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        repository.save(user);
        return TOKEN_VALID;
    }

    /* public User updateUser2FA(boolean use2FA) {
         final Authentication curAuth = SecurityContextHolder.getContext()
                 .getAuthentication();
         User currentUser = (User) curAuth.getPrincipal();
         currentUser.setIsUsing2FA(use2FA);
         currentUser = repository.save(currentUser);
         final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), curAuth.getAuthorities());
         SecurityContextHolder.getContext()
                 .setAuthentication(auth);
         return currentUser;
     }
 */
    public String getRedirectUrl() {
        return redirectUrl;
    }

}
