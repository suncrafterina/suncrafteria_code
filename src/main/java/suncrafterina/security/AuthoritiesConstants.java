package suncrafterina.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String SUB_ADMIN = "ROLE_SUB_ADMIN";

    public static final String FACTORY_VENDOR = "ROLE_FACTORY_VENDOR";

    public static final String CUSTOMER = "ROLE_CUSTOMER";

    private AuthoritiesConstants() {
    }
}
