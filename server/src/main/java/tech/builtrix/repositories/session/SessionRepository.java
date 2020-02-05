package tech.builtrix.repositories.session;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.session.Session;

import java.util.List;

@EnableScan
public interface SessionRepository extends RepositoryBase<Session> {

	// Session getBySessionKey(String sessionKey);

	// Integer incrementRequests(@Param("key") String key);

	List<Session> findByUser(String userId);

	/*
	 * void touchSession(@Param("id") String id,
	 * 
	 * @Param("expTime") Date expTime);
	 */

	Page<Session> findBySessionKey(String sessionId, Pageable pageable);

	/*
	 * void updateSessionUserId(@Param("userId") String userId,
	 * 
	 * @Param("sessionKey") String sessionKey );
	 */

	Session findBySessionKey(String sessionKey);

	// List<Session> getTestSession(@Param("mobileNumber") String mobileNumber);

	// void disableAllOtherSessions(@Param("userId") String userId);
}
