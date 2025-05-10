package ch.zhaw.fundhive.service.helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService();
    }

    private void setSecurityContextWithJwt(String subject, List<String> roles) {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", subject)
                .claim("user_roles", roles)
                .build();

        var auth = new UsernamePasswordAuthenticationToken(jwt, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void userHasRole_returnsTrueIfRolePresent() {
        setSecurityContextWithJwt("user123", List.of("entrepreneur", "admin"));
        assertTrue(userService.userHasRole("admin"));
    }

    @Test
    void userHasRole_returnsFalseIfRoleNotPresent() {
        setSecurityContextWithJwt("user123", List.of("investor"));
        assertFalse(userService.userHasRole("admin"));
    }

    @Test
    void getCurrentUserId_returnsSubjectFromJwt() {
        setSecurityContextWithJwt("user456", List.of("investor"));
        assertEquals("user456", userService.getCurrentUserId());
    }
}
