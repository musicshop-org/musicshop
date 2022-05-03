package communication.rest;

import communication.rest.api.RestLoginService;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

public class RestLoginServiceImpl implements RestLoginService {

    @Override
    public boolean checkCredentials(String username, String password) {
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

            String base = "ou=customer,dc=openmicroscopy,dc=org";
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
