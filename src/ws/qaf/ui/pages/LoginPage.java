package ws.qaf.ui.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.NewCookie;

import org.openqa.selenium.Cookie;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.CommonStep;
import com.qmetry.qaf.automation.ui.WebDriverBaseTestPage;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.annotations.FindBy;
import com.qmetry.qaf.automation.ui.api.PageLocator;
import com.qmetry.qaf.automation.ui.api.WebDriverTestPage;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;

import ws.qaf.ui.testdatabean.RegisterUserDataBean;

public class LoginPage extends WebDriverBaseTestPage<WebDriverTestPage> {

	//Login Page
	@FindBy(locator = "id=register-username-modal")
	private QAFWebElement regUsername;

	@FindBy(locator = "id=username-modal")
	private QAFWebElement username;

	@FindBy(locator = "id=register-password-modal")
	private QAFWebElement regPassword;

	@FindBy(locator = "id=password-modal")
	private QAFWebElement password;

	@FindBy(locator = "id=register-first-modal")
	private QAFWebElement firstname;

	@FindBy(locator = "id=register-last-modal")
	private QAFWebElement lastname;

	@FindBy(locator = "id=register-email-modal")
	private QAFWebElement email;

	@FindBy(locator = "xpath=//li[@id='login']/a")
	private QAFWebElement login_link;

	@FindBy(locator = "xpath=//li[@id='register']/a")
	private QAFWebElement register_link;

	@FindBy(locator = "xpath=//li[@id='logout']/a")
	private QAFWebElement logout_link;
	
	@FindBy(locator = "xpath=//button[contains(., 'Register')]")
	private QAFWebElement register_button;

	@FindBy(locator = "xpath=//button[contains(., 'Log in')]")
	private QAFWebElement login_button;
	
	
	public QAFWebElement getUsername() {
		return username;
	}

	public QAFWebElement getPassword() {
		return password;
	}

	public QAFWebElement getLogoutLink() {
		return logout_link;
	}

	public QAFWebElement getLoginButton() {
		return login_button;
	}

	public QAFWebElement getRegUsername() {
		return regUsername;
	}

	public QAFWebElement getRegPassword() {
		return regPassword;
	}

	public QAFWebElement getFirstname() {
		return firstname;
	}

	public QAFWebElement getLastname() {
		return lastname;
	}

	public QAFWebElement getLoginLink() {
		return login_link;
	}

	public QAFWebElement getRegisterLink() {
		return register_link;
	}

	public QAFWebElement getEmail() {
		return email;
	}

	public QAFWebElement getRegisterButton() {
		return register_button;
	}

	
	//Home Page
	@FindBy(locator = "xpath=//li[@id='tabCatalogue']/a")
	private QAFWebElement catalogueTab;

	@FindBy(locator = "id=numItemsInCart")
	private QAFWebElement itemsInCartButton;
	
	@FindBy(locator = "xpath=(//div[@class='product']//a[text()='Add to cart'])[1]")
	private QAFWebElement addToCartButtonOfProduct;
	
	
	public QAFWebElement getCatalogueTab() {
		return catalogueTab;
	}

	public QAFWebElement getItemsInCartButton() {
		return itemsInCartButton;
	}
	
	public QAFWebElement getAddToCartButtonOfProduct() {
		return addToCartButtonOfProduct;
	}

	@Override
	protected void openPage(PageLocator arg0, Object... arg1) {
		driver.manage().window().maximize();
		driver.get("/");
	}

	public void registerUSerThroughUI(RegisterUserDataBean userbean) {

		register_link.click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		getRegUsername().sendKeys(userbean.getUsername());
		getFirstname().sendKeys(userbean.getFirstnmae());
		getLastname().sendKeys(userbean.getLastname());
		getEmail().sendKeys(userbean.getEmail());
		getRegPassword().sendKeys(userbean.getPassword());

		getRegisterButton().click();
		getLogoutLink().waitForPresent(10000);
	}

	@SuppressWarnings("unchecked")
	public void verifyItemAddedInCart() {

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Object ck = ConfigurationManager.getBundle().getObject("api.integration.cookie");

		if (ck != null) {
			if (ck instanceof NewCookie)
				new WebDriverTestBase().getDriver().manage()
						.addCookie(new Cookie(((NewCookie) ck).getName(), ((NewCookie) ck).getValue()));

			else {
				for (NewCookie cookie : (Collection<? extends NewCookie>) ck) {
					new WebDriverTestBase().getDriver().manage()
							.addCookie(new Cookie(cookie.getName(), cookie.getValue()));
				}
			}
		}

		CommonStep.get("/");
		getItemsInCartButton().verifyText("1 item(s) in cart");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void logIn(RegisterUserDataBean userbean) {
		getLoginLink().click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		getUsername().sendKeys(userbean.getUsername());
		getPassword().sendKeys(userbean.getPassword());

		getLoginButton().click();
		getLogoutLink().waitForPresent(10000);
	}
	
	public void logIn(String uName, String password) {
		getLoginLink().click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		getUsername().sendKeys(uName);
		getPassword().sendKeys(password);

		getLoginButton().click();
		getLogoutLink().waitForPresent(10000);
	}

	public void logOut() {

		getLogoutLink().waitForPresent(10000);
		getLogoutLink().click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public void addItemToCartThroughUI() {
		getCatalogueTab().waitForPresent(10000);
		getCatalogueTab().click();
		getAddToCartButtonOfProduct().click();
	}

}
