package ws.qaf.tests;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.WebDriverTestCase;
import ws.qaf.ui.pages.LoginPage;
import ws.qaf.ui.testdatabean.RegisterUserDataBean;
import ws.qaf.utilities.APIRequestQAF;

public class QAFTestsUIandWS extends WebDriverTestCase {

	@Test(testName = "4. Register User through UI & Add Item In Cart through WS & Verify Cart thorugh UI", enabled = true)
	public void registerUserThroughUIandVerifyThroughAPI() {
		APIRequestQAF apiRequestQAF = new APIRequestQAF();

		LoginPage login = new LoginPage();
		login.launchPage(null);
		
		RegisterUserDataBean registerBean = new RegisterUserDataBean();
		registerBean.fillRandomData();

		login.registerUSerThroughUI(registerBean);
		
		ConfigurationManager.getBundle().setProperty("rest.client.basic.auth.username", registerBean.getUsername());
		ConfigurationManager.getBundle().setProperty("rest.client.basic.auth.password", registerBean.getPassword());
		
		apiRequestQAF.getAuthorization(registerBean.getUsername(), registerBean.getPassword());

		apiRequestQAF.loginThroughAPI();
		apiRequestQAF.addItemInCartThroughAPI("item.payload");
		apiRequestQAF.getItemsFromCartThroughAPI();
		
		login.verifyItemAddedInCart();
	}
	
	@Test(testName = "5. Register User through WS & Add Item In Cart through UI & Verify Cart thorugh WS", enabled = true)
	public void registerUserThroughWSandVerifyThroughUI() {
		APIRequestQAF apiRequestQAF = new APIRequestQAF();
		
		apiRequestQAF.registerUserThroughAPI("register.payload");

		LoginPage login = new LoginPage();
		login.launchPage(null);
		
		login.logIn(ConfigurationManager.getBundle().getProperty("rest.client.basic.auth.username").toString(), ConfigurationManager.getBundle().getProperty("rest.client.basic.auth.password").toString());
		login.addItemToCartThroughUI();
		login.logOut();
		
		apiRequestQAF.loginThroughAPI();
		apiRequestQAF.verifyCartItemThroughAPI();
	}
	
}
