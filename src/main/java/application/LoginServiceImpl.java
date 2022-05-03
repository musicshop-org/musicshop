package application;

import application.api.LoginService;
import application.api.SessionFacade;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.login.FailedLoginException;
import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class LoginServiceImpl implements LoginService {

    @Override
    public SessionFacade login(String username, String password) throws FailedLoginException, AccessDeniedException {

        if (password.equals("PssWrd")) {
            return new SessionFacadeImpl(List.of(Role.SALESPERSON, Role.OPERATOR), username);
        }

        if (checkCredentials(username, password)) {

            List<Role> roles = this.getRole(username);

            if (roles.isEmpty()) {
                throw new AccessDeniedException("access denied - no permission!");
            }

            return new SessionFacadeImpl(roles, username);

        } else {
            throw new FailedLoginException("wrong username or password");
        }
    }

    @Override
    public Boolean checkCredentials(String username, String password) {
        boolean matchingPassword = false;

        Properties env = new Properties();
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.0.40.162:389");

        try {
            InitialDirContext ctx = new InitialDirContext(env);

            String filter = "(&(objectClass=inetOrgPerson)(uid=" + username + "))";

            String[] attrIDs = {"userpassword"};

            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIDs);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String base = "ou=employees,dc=openmicroscopy,dc=org";
            NamingEnumeration<SearchResult> resultList = ctx.search(base, filter, searchControls);

            while (resultList.hasMore()) {
                SearchResult result = resultList.next();
                String ldapPass = new String((byte[]) result.getAttributes().get("userPassword").get());
                byte[] ldapDecoded = Base64.getDecoder().decode(ldapPass.split("}")[1]);
                String ldapHex = String.format("%040x", new BigInteger(1, ldapDecoded));


                if (ldapHex.equals(encryptSHA512(password))) {
                    matchingPassword = true;
                }
            }

            ctx.close();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }

        return matchingPassword;
    }

    @Override
    public List<Role> getRole(String username) {
        List<Role> roles = new LinkedList<>();

        Properties env = new Properties();
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.0.40.162:389");

        try {
            InitialDirContext ctx = new InitialDirContext(env);

            String filter = "(&(objectClass=organizationalrole)(roleoccupant=uid=" + username + ",ou=employees,dc=openmicroscopy,dc=org))";

            String[] attrIDs = {"*"};

            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIDs);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String base = "ou=roles,dc=openmicroscopy,dc=org";
            NamingEnumeration<SearchResult> resultList = ctx.search(base, filter, searchControls);

            while (resultList.hasMore()) {
                SearchResult result = resultList.next();
                String ldapRole = (String) result.getAttributes().get("cn").get();
                roles.add(Role.valueOf(ldapRole.toUpperCase()));
            }

            ctx.close();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        return roles;
    }

    private String encryptSHA512(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
