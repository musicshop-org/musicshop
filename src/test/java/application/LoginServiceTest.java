package application;


import application.api.LoginService;
import domain.valueobjects.Role;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginServiceTest {

    private LoginService loginService = new LoginServiceImpl();

    @Test
    void given_validCredentials_when_login_then_returnTrue(){
        //given
        String username = "essiga";
        String password = "password01";

        //when
        Boolean result = loginService.checkCredentials(username,password);

        //then
        assertTrue(result);
    }

    @Test
    void given_invalidUsername_when_login_then_returnTrue(){
        //given
        String username = "ERROR";
        String password = "password01";

        //when
        Boolean result = loginService.checkCredentials(username,password);

        //then
        assertFalse(result);
    }

    @Test
    void given_invalidPassword_when_login_then_returnTrue(){
        //given
        String username = "essiga";
        String password = "ERROR";

        //when
        Boolean result = loginService.checkCredentials(username,password);

        //then
        assertFalse(result);
    }

    @Test
    void given_validUsername_when_getRole_then_returnCorrectRoles(){
        //given
        String username = "essiga";
        List<Role> expectedRoles = new LinkedList<>();
        expectedRoles.add(Role.OPERATOR);
        expectedRoles.add(Role.SALESPERSON);

        //when
        List<Role> results = loginService.getRole(username);

        //then
        assertTrue(results.containsAll(expectedRoles) && expectedRoles.containsAll(results));
    }

    @Test
    void given_userWithoutRoles_when_getRole_then_returnEmptyRoles(){
        //given
        String username = "unterkoflera";

        //when
        List<Role> results = loginService.getRole(username);

        //then
        assertTrue(results.isEmpty());
    }

    @Test
    void given_invalidUsername_when_getRole_then_returnEmptyRoles(){
        //given
        String username = "ERROR";

        //when
        List<Role> results = loginService.getRole(username);

        //then
        assertTrue(results.isEmpty());
    }

    @Test
    void given_userWithOneRole_when_getRole_then_returnOneRole(){
        //given
        String username = "mayerb";
        List<Role> expectedRoles = new LinkedList<>();
        expectedRoles.add(Role.OPERATOR);

        //when
        List<Role> results = loginService.getRole(username);

        //then
        assertTrue(results.containsAll(expectedRoles) && expectedRoles.containsAll(results));
    }
}
