package ws.qaf.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.ws.rs.core.NewCookie;

import org.glassfish.jersey.internal.util.Base64;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.automation.ws.Response;
import com.qmetry.qaf.automation.ws.RestWSTestCase;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

public class APIRequestQAF extends RestWSTestCase {

	public static Response response;
	public static String authHeader;
	public static String loggedInCookie;
	public static String mdSidCookie;
	public static String userId;
	public static List<NewCookie> cookies;
	public static String itemIdToBeDeleted;
	public static String itemId;
	public static String updatedQuantity;

	public Response POSTResponseForLogin(String path, String payload, String uName, String password, String email) {
		JSONObject payloadData = new JSONObject(
				GetProperty(payload).replace("UNAME", uName).replace("PWD", password).replace("EMAIL", email));
		getWebResource(GetProperty(path)).header("Content-Type", "application/json").post(payloadData.toString());
		return getResponse();
	}

	public Response POSTResponse(String path, String authHeader, String payload, String key, String value) {
		JSONObject payloadData = new JSONObject(GetProperty(payload).replace(key, value));
		getWebResource(GetProperty(path)).header("Content-Type", "application/json").header("Authorization", authHeader)
				.post(payloadData.toString());
		return getResponse();
	}

	public Response POSTResponse(String path, String authHeader, String payload) {
		JSONObject payloadData = new JSONObject(GetProperty(payload));
		getWebResource(GetProperty(path)).header("Content-Type", "application/json").header("Authorization", authHeader)
				.post(payloadData.toString());
		return getResponse();
	}

	public Response POSTResponse(String path, String authHeader) {
		getWebResource(GetProperty(path)).header("Content-Type", "application/json").header("Authorization", authHeader)
				.post();
		return getResponse();
	}

	public Response GETResponse(String path, String authHeader) {
		getWebResource(GetProperty(path)).header("Content-Type", "application/json").header("Authorization", authHeader)
				.get(ClientResponse.class);
		return getResponse();
	}

	public Response DELETEResponse(String path, String authHeader, String itemIdToBeDeleted) {
		getWebResource(String.format(GetProperty(path), itemIdToBeDeleted)).header("Content-Type", "application/json")
				.header("Authorization", authHeader).delete();
		return getResponse();
	}

	public void registerUserThroughAPI(String registerPayload) {
		Reporter.log("Registering a user");
		String uName = getRandomString(5);
		String password = getRandomString(10);
		String email = getRandomString(15);
		
		ConfigurationManager.getBundle().setProperty("rest.client.basic.auth.username", uName);
		ConfigurationManager.getBundle().setProperty("rest.client.basic.auth.password", password);
		
		response = POSTResponseForLogin("register", registerPayload, uName, password, email);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.OK));
		Reporter.log("User is registered successfully");
		getAuthorization(uName, password);
		JSONObject responseJson = new JSONObject(response.getMessageBody());
		userId = responseJson.getString("id");
		System.out.println("ID:: " + userId);
	}

	public void loginThroughAPI() {
		Reporter.log("User is getting logged in");
		response = GETResponse("login", authHeader);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.OK));
		System.out.println("Cookies:: " + response.getCookies());
		cookies = response.getCookies(); // store cookies
		Reporter.log("User is logged in successfully");
	}

	public void addItemInCartThroughAPI(String itemPaylod) {
		Reporter.log("Adding item in cart");
		response = POSTResponse("cart", authHeader, itemPaylod);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.CREATED));
		Reporter.log("Item is added successfully");
	}

	public void getItemsFromCartThroughAPI() {
		Reporter.log("Getting items from cart");
		response = GETResponse("cart", authHeader);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.OK));
		Reporter.log("Items received successfully");
		
		String itemDetails = response.getMessageBody();
		System.out.println("Item Details:: " + itemDetails);

		JSONArray itemArray = new JSONArray(itemDetails);

		itemId = itemArray.getJSONObject(0).getString("itemId");

		System.out.println("Item ID:: " + itemId);
	}

	public void storeItemToBeDeletedThroughAPI() {
		String itemDetails = response.getMessageBody();
		JSONArray itemArray = new JSONArray(itemDetails);
		itemIdToBeDeleted = itemArray.getJSONObject(1).getString("itemId");

		Reporter.log("Item ID to be deleted:: " + itemIdToBeDeleted);
	}

	public void addAddressThroughAPI(String addressPaylod) {
		Reporter.log("Adding address");
		response = POSTResponse("address", authHeader, addressPaylod, "USERID", userId);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.OK));
		Reporter.log("Address is added successfully");
	}

	public void addCardThroughAPI(String cardPaylod) {
		Reporter.log("Adding card details");
		response = POSTResponse("payment", authHeader, cardPaylod, "USERID", userId);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.OK));
		Reporter.log("Card is added successfully");
	}

	public void updateItemFromCartThroughAPI(String cartPaylod) {
		Reporter.log("Updating cart");
		response = POSTResponse("update.cart", authHeader, cartPaylod, "ITEMID", itemId);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.ACCEPTED));
		Reporter.log("Card is updated successfully");
	}

	public void deleteCartItemThroughAPI() {
		Reporter.log("Deleting a cart item");
		response = DELETEResponse("delete.cart", authHeader, itemIdToBeDeleted);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.ACCEPTED));
		Reporter.log("Item is deleted successfully - " + itemIdToBeDeleted);
	}

	public void placeAnOrderThroughAPI() {
		Reporter.log("Placing an order");
		response = POSTResponse("order", authHeader);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.CREATED));
		Reporter.log("Order is placed successfully");
	}

	public void getOrderDetailsThroughAPI() {
		Reporter.log("Getting an order details");
		response = GETResponse("order", authHeader);
		Validator.verifyThat(response.getStatus(), Matchers.equalTo(Status.CREATED));
		Reporter.log("Order details received successfully");
	}

	public void verifyOrderedItemThroughAPI(int itemNo) {
		Reporter.log("Verifying ordered item details");

		String orderDetails = response.getMessageBody();
		System.out.println("Order Details:: " + orderDetails);

		JSONObject orderObjectActual = new JSONObject(orderDetails.substring(1, orderDetails.length() - 1));
		JSONArray itemArray = orderObjectActual.getJSONArray("items");

		JSONObject orderObjectExpected = new JSONObject(GetProperty("item.payload"));
		
		Validator.verifyThat(itemArray.length(), Matchers.equalTo(itemNo));
		Validator.verifyThat(itemArray.getJSONObject(0).getString("itemId"), Matchers.equalTo(orderObjectExpected.getString("id")));
		Reporter.log("Ordered item details verified successfully");

	}

	public void verifyItemIsSameAsOrderedThroughAPI() {
		Reporter.log("Verifying item details are same as ordered");

		String orderDetails = response.getMessageBody();

		JSONObject orderObjectActual = new JSONObject(orderDetails.substring(1, orderDetails.length() - 1));
		JSONArray itemsArray = orderObjectActual.getJSONArray("items");

		JSONObject orderObjectExpected = new JSONObject(GetProperty("item.payload"));
		String itemIdExpected = orderObjectExpected.getString("id");

		boolean flag = false;
		for (int i = 0; i <= itemsArray.length() - 1; i++) {
			if (itemsArray.getJSONObject(i).getString("itemId").equals(itemIdExpected)) {
				flag = true;
				break;
			}
		}
		
		Validator.verifyThat(flag, Matchers.equalTo(true));
		Reporter.log("Verified Item details are same as ordered successfully");
	}

	public void verifyItemQuantityIsSameAsOrderedThroughAPI() {

		Reporter.log("Verifying Item quantity is same as ordered");

		String orderDetails = response.getMessageBody();

		JSONObject orderObjectActual = new JSONObject(orderDetails.substring(1, orderDetails.length() - 1));
		JSONArray itemsArray = orderObjectActual.getJSONArray("items");
		
		JSONObject updatedCartPayloadObject = new JSONObject(GetProperty("item.update.payload"));
		String expectedUpdatedQuantity = updatedCartPayloadObject.getString("quantity");

		for (int i = 0; i <= itemsArray.length() - 1; i++) {
			if (itemsArray.getJSONObject(i).getString("itemId").equals(itemId)) {
				updatedQuantity = itemsArray.getJSONObject(i).get("quantity").toString();
			}
		}

		Validator.verifyThat(updatedQuantity, Matchers.equalTo(expectedUpdatedQuantity));
		Reporter.log("Verified Item quantity is same as ordered successfully");
	}
	
	public void verifyDeletedItemIsNotDisplayedInOrderThroughAPI() {

		Reporter.log("Verifying deleted item is not displayed in order");

		String orderDetails = response.getMessageBody();

		JSONObject orderObjectActual = new JSONObject(orderDetails.substring(1, orderDetails.length() - 1));
		JSONArray itemsArray = orderObjectActual.getJSONArray("items");

		boolean flag = true;
		for (int i = 0; i <= itemsArray.length() - 1; i++) {
			if (itemsArray.getJSONObject(i).getString("itemId").equals(itemIdToBeDeleted)) {
				flag = false;
				break;
			}
		}

		Validator.verifyTrue(itemsArray.length() != 0, "No item is present in the order", "1 or more items present in the order");

		Validator.verifyThat(flag, Matchers.equalTo(true));
		
		Reporter.log("Verified deleted item is not displayed in order successfully");

	}
	
	public void verifyCartItemThroughAPI() {
		Reporter.log("Verifying cart item details");

		response = GETResponse("cart", authHeader);
		Assert.assertEquals(response.getStatus(), Status.OK, "Items received successfully");
		
		String itemDetails = response.getMessageBody();
		System.out.println("Item Details:: " + itemDetails);

		JSONArray itemDetailsArray = new JSONArray(itemDetails);

		Validator.verifyThat(itemDetailsArray.length(), Matchers.equalTo(1));

		Reporter.log("Cart item is verified successfully");
	}

	public void verifyUpdatedCartItemThroughAPI() {
		Reporter.log("Verifying updated cart item details");

		String itemDetails = response.getMessageBody();
		System.out.println("Item Details after Updation:: " + itemDetails);

		JSONArray itemUpdatedArray = new JSONArray(itemDetails);
		
		JSONObject updatedCartPayloadObject = new JSONObject(GetProperty("item.update.payload"));
		String expectedUpdatedQuantity = updatedCartPayloadObject.getString("quantity");

		for (int i = 0; i <= itemUpdatedArray.length() - 1; i++) {
			if (itemUpdatedArray.getJSONObject(i).getString("itemId").equals(itemId)) {
				updatedQuantity = itemUpdatedArray.getJSONObject(i).get("quantity").toString();
			}
		}

		Validator.verifyThat(updatedQuantity, Matchers.equalTo(expectedUpdatedQuantity));
		Reporter.log("Quantity is updated successfully for the item - " + itemId);
	}

	public void verifyDeletedCartItemThroughAPI() {
		Reporter.log("Verifying deleted cart item details");

		String itemDetails = response.getMessageBody();
		System.out.println("Item Details After Deletion:: " + itemDetails);

		JSONArray itemUpdatedArray = new JSONArray(itemDetails);

		boolean flag = true;

		for (int i = 0; i <= itemUpdatedArray.length() - 1; i++) {
			if (itemUpdatedArray.getJSONObject(i).getString("itemId").equals(itemIdToBeDeleted)) {
				flag = false;
				break;
			}
		}

		Validator.verifyTrue(itemUpdatedArray.length() != 0, "No item is present in the cart", "1 or more items present in the cart");

		Validator.verifyThat(flag, Matchers.equalTo(true));

		Reporter.log("Deleted cart item details successfully for the item - " + itemIdToBeDeleted);
	}

	public void getAuthorization(String uName, String password) {
		System.out.println("UserName: " + uName + " Password: " + password);
		Reporter.log("UserName: " + uName + " Password: " + password);
		byte[] encoded = Base64.encode((uName + ":" + password).getBytes());
		authHeader = "Basic " + new String(encoded);
	}

	public static String GetProperty(String key) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("resources\\resources.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop.getProperty(key);
	}

	public String getRandomString(int length) {

		String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < length) {
			int index = (int) (rnd.nextFloat() * ALPHANUMERIC.length());
			salt.append(ALPHANUMERIC.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

}
