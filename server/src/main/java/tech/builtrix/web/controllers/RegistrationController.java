package tech.builtrix.web.controllers;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.annotations.Authorize;
import tech.builtrix.annotations.NoSession;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.exceptions.AlreadyExistException;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.user.DemoUser;
import tech.builtrix.models.user.User;
import tech.builtrix.models.user.VerificationToken;
import tech.builtrix.models.user.enums.Role;
import tech.builtrix.registration.OnRegistrationCompleteEvent;
import tech.builtrix.services.authenticate.CodeService;
import tech.builtrix.services.user.DemoUserService;
import tech.builtrix.services.user.UserService;
import tech.builtrix.web.dtos.emailToken.RegisterDemoUserDto;
import tech.builtrix.web.dtos.emailToken.RegisterUserDto;
import tech.builtrix.web.dtos.user.InvitationDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created By sahar-hoseini at 23. October 2019 1:56 PM
 **/

@RestController
@RequestMapping("/v1/users")
@Api(value = "User Registration Controller", tags = {"User Registration Controller"})
@Slf4j
public class RegistrationController extends ControllerBase {

    private final UserService userService;
    private final DemoUserService demoUserService;
    private final CodeService codeService;
    private final MessageSource messages;
    private final ApplicationEventPublisher eventPublisher;
    private final Environment env;

    public RegistrationController(UserService userService,
                                  DemoUserService demoUserService,
                                  CodeService codeService,
                                  MessageSource messages,
                                  ApplicationEventPublisher eventPublisher,
                                  Environment env) {
        super();
        this.userService = userService;
        this.demoUserService = demoUserService;
        this.codeService = codeService;
        this.messages = messages;
        this.eventPublisher = eventPublisher;
        this.env = env;
    }

    // Registration
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @NoSession
    public Response<Void> registerUserAccount(@Valid @RequestBody RegisterUserDto registerUserDto,
                                              final HttpServletRequest request) throws AlreadyExistException {
        logger.debug("Registering user account with information: {}", registerUserDto);
        final User registered = userService.registerNewUserAccount(registerUserDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return Response.ok();
    }

    // Registration
    @RequestMapping(value = "/registerDemoUser", method = RequestMethod.POST)
    @ResponseBody
    @NoSession
    public Response<Void> registerDemoUser(@Valid @RequestBody RegisterDemoUserDto registerUserDto,
                                           final HttpServletRequest request) {
        logger.debug("Registering demo user with information: {}", registerUserDto);
        final DemoUser registered = demoUserService.registerDemoUser(registerUserDto);
        return Response.ok();
    }

    @RequestMapping(value = "/invitation", method = RequestMethod.POST)
    @ResponseBody
    @Authorize(roles = Role.Senior)
    public Response<Void> registerViaInvitation(@RequestBody InvitationDto invitationDto,
                                                final HttpServletRequest request) throws AlreadyExistException {

        logger.debug("Registering user account with information: {}", invitationDto);
        User parent = requestContext.getUser();
        final User registered = userService.registerUserViaInvitation(invitationDto, parent);
        eventPublisher
                .publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return Response.ok();
    }

    /*
     * @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
     *
     * @NoSession public Response<String> confirmRegistration(final
     * HttpServletRequest request, final Model model,
     *
     * @RequestParam("token") final String token) throws NotFoundException { Locale
     * locale = request.getLocale(); String userId = null; try { userId =
     * codeService.validateToken(token, TokenPurpose.Register); } catch
     * (TokenNotExistException | TokenUsedException | TokenExpiredException e) {
     * //TODO model.addAttribute("message", messages.getMessage(e.getMessage(),
     * null, locale)); ErrorMessage errorMessage =
     * e.getClass().getDeclaredAnnotation(ErrorMessage.class);
     * //"redirect:/badUser.html?lang=" + locale.getLanguage() return
     * Response.error(errorMessage.code(), e.getMessage()); }
     * userService.enableUser(userId); return
     * Response.ok("redirect:/console.html?lang=" + locale.getLanguage());
     *//*
     * if (result.equals("valid")) { final User user =
     * userService.getById(userService.getUser(token)); // if (user.isUsing2FA()) {
     * // model.addAttribute("qr", userService.generateQRUrl(user)); // return
     * "redirect:/qrcode.html?lang=" + locale.getLanguage(); // }
     * authWithoutPassword(user); model.addAttribute("message",
     * messages.getMessage("message.accountVerified", null, locale)); return
     * "redirect:/console.html?lang=" + locale.getLanguage(); }
     *
     * model.addAttribute("message", messages.getMessage("auth.message." + result,
     * null, locale)); model.addAttribute("expired", "expired".equals(result));
     * model.addAttribute("token", token); return "redirect:/badUser.html?lang=" +
     * locale.getLanguage();
     *//*
     * }
     */
    @NoSession
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public byte[] confirmRegistration(final HttpServletRequest request, @RequestParam("token") final String token)
            throws IOException, NotFoundException {
        final String result = userService.validateVerificationToken(token);
        String template;
        final User user = userService.getUser(token);
        if (result.equals("valid")) {
            template = getTemplate("templates/email/email.verification.success.template");
            template = template.replace("{555}", userService.getRedirectUrl());
        } else {
            template = getTemplate("templates/email/email.verification.failure.template");
            template = template.replace("{555}", getAppUrl(request) + "/v1/users/resendConfirm?token=" + token);
        }

        return template.getBytes(StandardCharsets.UTF_8);
    }

    @NoSession
    @RequestMapping(value = "/resendConfirm", method = RequestMethod.GET)
    public Response<Void> resendConfirmation(final HttpServletRequest request,
                                             @RequestParam("token") final String existingToken) throws NotFoundException {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        if (user != null) {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), getAppUrl(request)));
            return Response.ok();
        }
        throw new NotFoundException("User", "", "");
    }

    // ============== NON-API ============
    private String getTemplate(String templatePath) throws IOException {
        String template;
        InputStream inputStream = new ClassPathResource(templatePath).getInputStream();
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        template = scanner.hasNext() ? scanner.next() : "";
        return template;
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmailAddress());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    /*
     * public void authWithoutPassword(User user) { List<Privilege> privileges =
     * user.getRoles().stream().map(role -> role.getPrivileges()).flatMap(list ->
     * list.stream()).distinct().collect(Collectors.toList());
     * List<GrantedAuthority> authorities = privileges.stream().map(p -> new
     * SimpleGrantedAuthority(p.getName())).collect(Collectors.toList());
     *
     * Authentication authentication = new UsernamePasswordAuthenticationToken(user,
     * null, authorities);
     *
     * SecurityContextHolder.getContext().setAuthentication(authentication); }
     */
}
