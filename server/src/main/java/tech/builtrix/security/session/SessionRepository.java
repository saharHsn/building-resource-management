package tech.builtrix.security.session;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.builtrix.base.RepositoryBase;

import java.util.Date;
import java.util.List;


@Repository
public interface SessionRepository extends RepositoryBase<Session> {

    Session getBySessionKey(String sessionKey);

    Integer incrementRequests(@Param("key") String key);

    Page<Session> findUserSession(@Param("userId") String userId,
                                  Pageable pageable);

    void touchSession(@Param("id") String id,
                      @Param("expTime") Date expTime);


    Page<Session> findUserSessionToken(@Param("sessionId") String sessionId, Pageable pageable);

    void updateSessionUserId(@Param("userId") String userId,
                             @Param("sessionKey") String sessionKey
    );

    List<Session> getTestSession(@Param("mobileNumber") String mobileNumber);

    void disableAllOtherSessions(@Param("userId") String userId);
}

