package tech.builtrix.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(long id, String token);

}
