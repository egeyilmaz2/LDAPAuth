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
			authEnv.put(Context.PROVIDER_URL, "ldap://192.168.10.5");
			authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			authEnv.put(Context.SECURITY_PRINCIPAL, username+"@smartict.com.tr");
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
		return EnumAuthentication.AUTHENTICATION_CONFIG_ERROR.getCode();//configuration hatalı ise buraya düşer (Context configuration)
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
			NamingEnumeration<SearchResult> answer = getLdapContext().search("dc=smartict,dc=com,dc=tr","sAMAccountName="+username,controls);//Kullanıcı adı ile aratma yapar
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
			e.printStackTrace();//Context hatalı ise buraya düşer
		}
		return attrList;
	}
	public void setUpAdminContext(String adminUsername,String adminPassword){
		setUpLdapContextWithUsernameAndPassword(adminUsername,adminPassword);
	}

}
