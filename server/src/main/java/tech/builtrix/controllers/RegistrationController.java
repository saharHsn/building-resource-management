package tech.builtrix.controllers;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.annotations.Authorize;
import tech.builtrix.annotations.NoSession;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.dtos.emailToken.RegisterUserDto;
import tech.builtrix.dtos.user.InvitationDto;
import tech.builtrix.exceptions.*;
import tech.builtrix.models.user.Role;
import tech.builtrix.models.user.TokenPurpose;
import tech.builtrix.models.user.User;
import tech.builtrix.registration.OnRegistrationCompleteEvent;
import tech.builtrix.services.authenticate.CodeService;
import tech.builtrix.services.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

/**
 * Created By sahar-hoseini at 23. October 2019 1:56 PM
 **/

@RestController
@RequestMapping("/v1/users")
@Api(value = "User Registration Controller", tags = {"User Registration Controller"})
@Slf4j
public class RegistrationController extends ControllerBase {

    private final UserService userService;
    private final CodeService codeService;
    private final MessageSource messages;
    private final ApplicationEventPublisher eventPublisher;
    private final Environment env;

    public RegistrationController(UserService userService,
                                  CodeService codeService,
                                  MessageSource messages,
                                  ApplicationEventPublisher eventPublisher,
                                  Environment env) {
        super();
        this.userService = userService;
        this.codeService = codeService;
        this.messages = messages;
        this.eventPublisher = eventPublisher;
        this.env = env;
    }

    // Registration
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    @NoSession
    public Response<Void> registerUserAccount(@Valid final RegisterUserDto registerUserDto, final HttpServletRequest request) throws AlreadyExistException {
        logger.debug("Registering user account with information: {}", registerUserDto);
        final User registered = userService.registerNewUserAccount(registerUserDto);
        //eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return Response.ok();
    }

    @RequestMapping(value = "/invitation", method = RequestMethod.POST)
    @ResponseBody
    @Authorize(roles = Role.Senior)
    public Response<Void> registerViaInvitation(@Valid final InvitationDto invitationDto,
                                                final HttpServletRequest request) throws AlreadyExistException {

        logger.debug("Registering user account with information: {}", invitationDto);
        User parent = requestContext.getUser();
        final User registered = userService.registerUserViaInvitation(invitationDto, parent);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return Response.ok();
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    @NoSession
    public Response<String> confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token) throws NotFoundException {
        Locale locale = request.getLocale();
        String userId = null;
        try {
            userId = codeService.validateToken(token, TokenPurpose.Register);
        } catch (TokenNotExistException | TokenUsedException | TokenExpiredException e) {
            //TODO
            model.addAttribute("message", messages.getMessage(e.getMessage(), null, locale));
            ErrorMessage errorMessage = e.getClass().getDeclaredAnnotation(ErrorMessage.class);
            //"redirect:/badUser.html?lang=" + locale.getLanguage()
            return Response.error(errorMessage.code(), e.getMessage());
        }
        userService.enableUser(userId);
        return Response.ok("redirect:/console.html?lang=" + locale.getLanguage());
       /* if (result.equals("valid")) {
            final User user = userService.getById(userService.getUser(token));
            // if (user.isUsing2FA()) {
            // model.addAttribute("qr", userService.generateQRUrl(user));
            // return "redirect:/qrcode.html?lang=" + locale.getLanguage();
            // }
            authWithoutPassword(user);
            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
            return "redirect:/console.html?lang=" + locale.getLanguage();
        }

        model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
        model.addAttribute("expired", "expired".equals(result));
        model.addAttribute("token", token);
        return "redirect:/badUser.html?lang=" + locale.getLanguage();*/
    }

    // ============== NON-API ============
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
}
