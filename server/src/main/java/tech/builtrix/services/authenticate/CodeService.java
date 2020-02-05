package tech.builtrix.services.authenticate;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.builtrix.exceptions.TokenExpiredException;
import tech.builtrix.exceptions.TokenNotExistException;
import tech.builtrix.exceptions.TokenUsedException;
import tech.builtrix.models.user.TokenPurpose;
import tech.builtrix.models.user.User;
import tech.builtrix.models.user.VerificationToken;
import tech.builtrix.repositories.user.VerificationTokenRepository;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

@Component
//@ConfigurationProperties("metrics.token")
public class CodeService {

	@Value("${metrics.token.email.length}")
	private int emailTokenLength;
	@Value("${metrics.token.email.expiration}")
	private int emailTokenExpiration;

	public static final String TOKEN_INVALID = "invalidToken";
	public static final String TOKEN_EXPIRED = "expired";
	public static final String TOKEN_VALID = "valid";

	/*
	 * @Value("${metrics.token.phone.length}") private int phoneTokenLength;
	 * 
	 * @Value("${metrics.token.phone.expiration}") private int phoneTokenExpiration;
	 */

	private final VerificationTokenRepository userTokenRepository;

	@Autowired
	public CodeService(VerificationTokenRepository userTokenRepository) {
		this.userTokenRepository = userTokenRepository;
	}

	public VerificationToken createToken(User user, TokenPurpose purpose) {
		return createToken(user, purpose, emailTokenExpiration, emailTokenLength, false);
	}

	public String validateToken(String token, TokenPurpose purpose)
			throws TokenNotExistException, TokenUsedException, TokenExpiredException {
		VerificationToken userToken = userTokenRepository.findByToken(token);
		if (userToken == null) {
			throw new TokenNotExistException();
		}
		if (userToken.getUsedTime() != null) {
			throw new TokenUsedException();
		}
		if (!userToken.getPurpose().equals(purpose)) {
			throw new TokenExpiredException("Token purpose not correct");
		}
		if (userToken.getExpiryDate().before(new Date())) {
			throw new TokenExpiredException("Token expired");
		}
		String userId = userToken.getUser();
		userToken.setUsedTime(new Date());
		return userToken.getUser();
		/*
		 * if (verificationToken == null) { return TOKEN_INVALID; } final User user =
		 * verificationToken.getUser(); final Calendar cal = Calendar.getInstance(); if
		 * ((verificationToken.getExpiryDate() .getTime() - cal.getTime() .getTime()) <=
		 * 0) { tokenRepository.delete(verificationToken); return TOKEN_EXPIRED; }
		 * user.setEnabled(true); // tokenRepository.delete(verificationToken);
		 * userRepository.save(user); return TOKEN_VALID;
		 */
	}

	private VerificationToken createToken(User user, TokenPurpose purpose, int secondsToExpire, int length,
			boolean justNumber) {
		VerificationToken token = new VerificationToken();
		// getById current time
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		// add expiration time
		calendar.add(Calendar.SECOND, secondsToExpire);
		token.setExpiryDate(calendar.getTime());
		token.setUser(user.getId());
		token.setPurpose(purpose);
		token.setToken(justNumber ? generateRandomNumber(length) : RandomString.make(length));
		this.userTokenRepository.save(token);
		return token;
	}

	public String generateRandomNumber(int length) {
		final String seed = "0123456789";
		Random random = new SecureRandom();
		StringBuilder builder = new StringBuilder(length);
		for (int i = 0; i < length; ++i) {
			builder.append(seed.charAt(random.nextInt(seed.length())));
		}
		return builder.toString();
	}
}
