package tech.builtrix.dtos;

import lombok.Data;
import tech.builtrix.services.session.SessionKeyService;

@Data
public class GetSessionResponse {
    private String session;
    private String headerKey = SessionKeyService.HeaderKey;
    private String cookieKey = SessionKeyService.CookieKey;
    private String queryKey = SessionKeyService.QueryKey;
}
