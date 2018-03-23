package ws.qaf.ui.testdatabean;

import com.qmetry.qaf.automation.data.BaseDataBean;
import com.qmetry.qaf.automation.util.RandomStringGenerator.RandomizerTypes;
import com.qmetry.qaf.automation.util.Randomizer;

public class RegisterUserDataBean extends BaseDataBean {
    
	@Randomizer(prefix="test_", length=5, suffix="@mailinatoir.com")
    String email;
     
    @Randomizer(prefix="username_", length=5,type=RandomizerTypes.LETTERS_ONLY)
    String username;
    
    @Randomizer(prefix="firstname_", length=5,type=RandomizerTypes.LETTERS_ONLY)
    String firstnmae;
    
    @Randomizer(prefix="lastname_", length=5,type=RandomizerTypes.LETTERS_ONLY)
    String lastname;
    
    @Randomizer(prefix="password_", length=5,type=RandomizerTypes.LETTERS_ONLY)
    String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstnmae() {
		return firstnmae;
	}

	public void setFirstnmae(String firstnmae) {
		this.firstnmae = firstnmae;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
