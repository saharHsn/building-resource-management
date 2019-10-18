package tech.builtrix.service.authenticate;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.builtrix.exception.TokenExpiredException;
import tech.builtrix.exception.TokenNotExistException;
import tech.builtrix.exception.TokenUsedException;
import tech.builtrix.model.user.TokenPurpose;
import tech.builtrix.model.user.User;
import tech.builtrix.model.user.UserToken;
import tech.builtrix.repository.user.UserTokenRepository;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.*;

@Service
public class CodeService {

    @Value("${investment.token.email.length}")
    private int emailTokenLength;
    @Value("${investment.token.email.expiration}")
    private int emailTokenExpiration;

    @Value("${investment.token.phone.length}")
    private int phoneTokenLength;
    @Value("${investment.token.phone.expiration}")
    private int phoneTokenExpiration;

    private final UserTokenRepository userTokenRepository;

    @Autowired
    public CodeService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }


    public UserToken createEmailToken(User user, TokenPurpose purpose) {
        return createToken(user, purpose, emailTokenExpiration, emailTokenLength, false);
    }

    UserToken createPhoneToken(User user, TokenPurpose purpose) {
        return createToken(user, purpose, phoneTokenExpiration, phoneTokenLength, true);
    }

    @Transactional
    public void validateToken(User user, String token, TokenPurpose purpose) throws TokenExpiredException, TokenNotExistException, TokenUsedException {
        Optional<UserToken> optionalUserToken = user.getTokens()
                .stream()
                .filter(x -> x.getValue().equals(token))
                .sorted((o1, o2) -> o2.getCreationTime().compareTo(o1.getCreationTime()))
                .findAny();

        if (!optionalUserToken.isPresent()) {
            throw new TokenNotExistException();
        }
        UserToken userToken = optionalUserToken.get();
        if (userToken.getUsedTime() != null) {
            throw new TokenUsedException();
        }
        if (!userToken.getPurpose().equals(purpose)) {
            throw new TokenExpiredException("Token purpose not correct");
        }
        if (userToken.getExpirationTime().before(new Date())) {
            throw new TokenExpiredException("Token expired");
        }

        userToken.setUsedTime(new Date());
    }

    private UserToken createToken(User user, TokenPurpose purpose, int secondsToExpire, int length, boolean justNumber) {
        UserToken token = new UserToken();
        // getById current time
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        // add expiration time
        calendar.add(Calendar.SECOND, secondsToExpire);
        token.setExpirationTime(calendar.getTime());
        token.setUser(user);
        token.setPurpose(purpose);
        token.setValue(justNumber ? generateRandomNumber(length) : RandomString.make(length));
        this.userTokenRepository.save(token);
        return token;
    }

    private String generateRandomNumber(int length) {
        final String seed = "0123456789";
        Random random = new SecureRandom();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            builder.append(seed.charAt(random.nextInt(seed.length())));
        }
        return builder.toString();
    }
}

