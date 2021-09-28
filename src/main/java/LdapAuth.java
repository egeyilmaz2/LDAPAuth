import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;
import java.util.Objects;


public class LdapAuth {

	private LdapContext ldapContext=null;
	private void setLdapContext(LdapContext ldapContext){
		if(Objects.nonNull(ldapContext)){
			this.ldapContext=ldapContext;
		}
	}
	private LdapContext getLdapContext(){
		if(Objects.isNull(this.ldapContext)){
			return null;
		}
		return this.ldapContext;
	}
	public int setUpLdapContextWithUsernameAndPassword(String username, String password) {
		try {
			Hashtable<String,String> authEnv = new Hashtable<String,String>();
			authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			authEnv.put(Context.PROVIDER_URL, "ldap://youradress");
			authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			authEnv.put(Context.SECURITY_PRINCIPAL, username+"@example.com");
			authEnv.put(Context.SECURITY_CREDENTIALS, password);
			try {
				setLdapContext(new InitialLdapContext(authEnv,null));
			} catch (AuthenticationException authEx) {
				return EnumAuthentication.AUTHENTICATION_SUCCESSFUL.getCode();
			}
			return EnumAuthentication.AUTHENTICATION_CREDENTIAL_ERROR.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EnumAuthentication.AUTHENTICATION_CONFIG_ERROR.getCode();//configuration error (Context configuration)
	}
	public Hashtable<String,String> getUserInfoWithUsernameWithAuthenticatedLdapContext(String username){
		SearchControls controls = new SearchControls();
		//String[] attrIDs = {"employeeID","description","streetAddress","company","userPrincipalName"
		//	,"mobile","cn","distinguishedName", "sn", "givenname", "mail", "telephoneNumber"};
		String[] testAllAttr={"*","+"};
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(testAllAttr);
		Hashtable<String,String > attrList=new Hashtable<>();
		try{
			NamingEnumeration<SearchResult> answer = getLdapContext().search("dc=yourDcName,dc=com,dc=tr","sAMAccountName="+username,controls);//search by sent username
			if(answer.hasMore()){
				Attributes attributes = answer.next().getAttributes();
				NamingEnumeration<String> attributesIDs= attributes.getIDs();
				while(attributesIDs.hasMore()){
					String next=attributesIDs.next();
					attrList.put(next,attributes.get(next).toString());
				}
				System.out.println();
			}
		}catch(Throwable e){
			e.printStackTrace();//If context is wrong , you get this
		}
		return attrList;
	}
	public void setUpAdminContext(String adminUsername,String adminPassword){
		setUpLdapContextWithUsernameAndPassword(adminUsername,adminPassword);
	}

}
