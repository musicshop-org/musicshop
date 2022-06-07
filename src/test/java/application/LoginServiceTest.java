package application;


import application.api.LoginService;
import application.api.SessionFacade;
import org.junit.jupiter.api.Test;
import sharedrmi.domain.valueobjects.Role;

import javax.security.auth.login.FailedLoginException;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private final LoginService loginService = new LoginServiceImpl();

    @Test
    void given_PssWrd_when_login_then_returnSessionFacade() throws AccessDeniedException, FailedLoginException, RemoteException {
        // given
        String username = "";
        String password = "PssWrd";
        SessionFacade expectedSessionFacade = new SessionFacadeImpl(List.of(Role.SALESPERSON, Role.OPERATOR), username);

        // when
        SessionFacade actualSessionFacade = loginService.login(username, password);

        // then
        assertAll(
                () -> assertEquals(expectedSessionFacade.getRoles(), actualSessionFacade.getRoles()),
                () -> assertEquals(expectedSessionFacade.getUsername(), actualSessionFacade.getUsername())
        );
    }

    @Test
    void given_validCredentials_when_login_then_returnSessionFacade() throws AccessDeniedException, FailedLoginException, RemoteException {
        // given
        String username = "meierm";
        String password = "password06";
        SessionFacade expectedSessionFacade = new SessionFacadeImpl(List.of(Role.SALESPERSON), username);

        // when
        SessionFacade actualSessionFacade = loginService.login(username, password);

        // then
        assertAll(
                () -> assertEquals(expectedSessionFacade.getRoles(), actualSessionFacade.getRoles()),
                () -> assertEquals(expectedSessionFacade.getUsername(), actualSessionFacade.getUsername())
        );
    }

    @Test
    void given_invalidCredentials_when_login_then_throwFailedLoginException() {
        // given
        String username = "ERROR";
        String password = "password01";

        // when ... then
        assertThrows(FailedLoginException.class, () -> loginService.login(username, password));
    }

    @Test
    void given_userWithoutRoles_when_login_then_throwAccessDeniedException() {
        // given
        String username = "unterkoflera";
        String password = "password07";

        // when ... then
        assertThrows(AccessDeniedException.class, () -> loginService.login(username, password));
    }

    @Test
    void given_validCredentials_when_checkCredentials_then_returnTrue() {
        // given
        String username = "essiga";
        String password = "password01";

        // when
        Boolean result = loginService.checkCredentials(username, password);

        // then
        assertTrue(result);
    }

    @Test
    void given_invalidUsername_when_checkCredentials_then_returnTrue() {
        // given
        String username = "ERROR";
        String password = "password01";

        // when
        Boolean result = loginService.checkCredentials(username, password);

        // then
        assertFalse(result);
    }

    @Test
    void given_invalidPassword_when_checkCredentials_then_returnTrue() {
        // given
        String username = "essiga";
        String password = "ERROR";

        // when
        Boolean result = loginService.checkCredentials(username, password);

        // then
        assertFalse(result);
    }

    @Test
    void given_validUsername_when_getRole_then_returnCorrectRoles() {
        // given
        String username = "essiga";
        List<Role> expectedRoles = new LinkedList<>();
        expectedRoles.add(Role.OPERATOR);
        expectedRoles.add(Role.SALESPERSON);

        // when
        List<Role> results = loginService.getRole(username);

        // then
        assertTrue(results.containsAll(expectedRoles) && expectedRoles.containsAll(results));
    }

    @Test
    void given_userWithoutRoles_when_getRole_then_returnEmptyRoles() {
        // given
        String username = "unterkoflera";

        // when
        List<Role> results = loginService.getRole(username);

        // then
        assertTrue(results.isEmpty());
    }

    @Test
    void given_invalidUsername_when_getRole_then_returnEmptyRoles() {
        // given
        String username = "ERROR";

        // when
        List<Role> results = loginService.getRole(username);

        // then
        assertTrue(results.isEmpty());
    }

    @Test
    void given_userWithOneRole_when_getRole_then_returnOneRole() {
        // given
        String username = "mayerb";
        List<Role> expectedRoles = new LinkedList<>();
        expectedRoles.add(Role.OPERATOR);

        // when
        List<Role> results = loginService.getRole(username);

        // then
        assertTrue(results.containsAll(expectedRoles) && expectedRoles.containsAll(results));
    }
}
