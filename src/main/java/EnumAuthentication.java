import lombok.Getter;

@Getter
public enum EnumAuthentication
{
	AUTHENTICATION_CONFIG_ERROR(2,"Konfigürasyon Hatalı"),
	AUTHENTICATION_CREDENTIAL_ERROR(1,"Kimlik bilgileri hatalı"),
	AUTHENTICATION_SUCCESSFUL(0,"Kimlik doğrulama işlemi başarılı");

	private final int code;

	private final String aciklama;

	EnumAuthentication(int code, String aciklama){
		this.code=code;
		this.aciklama=aciklama;
	}


}
