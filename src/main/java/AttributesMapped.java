import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class AttributesMapped {
	private String username;
	private String name;
	private String surname;
	private String email;
	private String mobilePhone;
	private String address;
	public AttributesMapped(Hashtable<String,String> attributes){
		this.username=attributes.get("sAMAccountName");
		this.name=setName(attributes.get("displayName"));//büyük harf denetimi gerekli (isim için hani özellik kullanılıyor , kontrol et)
		this.surname=attributes.get("sn");
		this.mobilePhone=attributes.get("telephoneNumber");
		this.address=attributes.get("streetAddress");
		this.email=attributes.get("mail");
	}
	private String setName(String displayName){
		List<String> splited = Arrays.asList(displayName.split("\\s+"));
		return String.join(" ",splited.subList(0,splited.size()-1));
	}
}
