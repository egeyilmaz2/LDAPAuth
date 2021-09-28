import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;
import java.util.Objects;


public class main {
	public static void main(String[] args) {
		String username="";
		String password = "";
		LdapAuth ldapAuth = new LdapAuth();
		int checkAuth=ldapAuth.setUpLdapContextWithUsernameAndPassword(username,password);
		Hashtable<String,String> attributeList = null;
		attributeList= ldapAuth.getUserInfoWithUsernameWithAuthenticatedLdapContext(username);
		System.out.println((attributeList));
		if(Objects.nonNull(attributeList)){
			AttributesMapped mappedAttributes=new AttributesMapped(attributeList);
		}
	}
}
