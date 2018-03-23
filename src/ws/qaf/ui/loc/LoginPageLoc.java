package ws.qaf.ui.loc;

public interface LoginPageLoc {
	 
	static final String USERNAME = "{'locator':'id=register-username-modal';'desc':'username'}";
	 static final String PASSWORD = "{'locator':'id=register-password-modal';'desc':'password'}";
	 static final String FIRSTNAME = "{'locator':'id=register-first-modal';'desc':'first name'}";
	 static final String LASTNAME = "{'locator':'id=register-last-modal';'desc':'last name'}";
	 static final String EMAIL = "{'locator':'id=register-email-modal';'desc':'email'}";
	 static final String LOGIN_LINK = "{'locator':'xpath=//li[@id=login']/a';'desc':'login link'}";
	 static final String REGISTER_LINK = "{'locator':'xpath=//li[@id='register']/a';'desc':'register link'}";
	 static final String REGISTER_BUTTON = "{'locator':'xpath=//button[contains(., 'Register')]';'desc':'register button'}";
}
