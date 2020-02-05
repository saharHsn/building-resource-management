package tech.builtrix.repositories.user;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.user.VerificationToken;

/**
 * Created By Sahar at 2/23/19 : 10:43 AM
 **/

@EnableScan
public interface VerificationTokenRepository extends RepositoryBase<VerificationToken> {

	VerificationToken findByToken(String token);

	// VerificationToken findByUser(User user);

	// Stream<VerificationToken> findAllByExpirationTimeLessThan(Date now);

	// void deleteByExpirationTimeLessThan(Date now);
}
