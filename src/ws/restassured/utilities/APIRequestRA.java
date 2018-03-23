package ws.restassured.utilities;

import static com.jayway.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.ws.rs.core.NewCookie;

import org.glassfish.jersey.internal.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import com.jayway.restassured.response.Response;
import com.qmetry.qaf.automation.util.Reporter;

public class APIRequestRA {

	public static Response response;
	public static String authHeader;
	public static String loggedInCookie;
	public static String mdSidCookie;
	public static String userId;
	public static List<NewCookie> cookies;
	public static String itemIdToBeDeleted;
	public static String itemId;
	public static String updatedQuantity;
	
	
	public static Response POSTResponse(String path, String payload, String uName, String password, String email) {
	    
		JSONObject payloadData = new JSONObject(GetProperty(payload).replace("UNAME", uName).replace("PWD",password).replace("EMAIL", email));
		
	    return given()
        .contentType("application/json")
        .body(payloadData.toString())
        .when().post(GetProperty("base.url.api") + GetProperty(path));
	    
    }
	
	public static Response GETResponse(String path, String authHeader) {

		return given()
        .contentType("application/json").header("Authorization", authHeader, loggedInCookie, mdSidCookie)
        .when().get(GetProperty("base.url.api") + GetProperty(path));
		
    }
	
	public static Response POSTResponse(String path, String authHeader, String loggedInCookie, String mdSidCookie, String payload, String key, String value) {
        
		JSONObject payloadData = new JSONObject(GetProperty(payload).replace(key, value));
		System.out.println("payloadData.toString():: "+payloadData.toString());
	    return given()
        .contentType("application/json")
        .header("Authorization", authHeader, loggedInCookie, mdSidCookie)
        .cookie("logged_in", loggedInCookie).cookie("md.sid", mdSidCookie)
        .body(payloadData.toString())
        .when().post(GetProperty("base.url.api") + GetProperty(path));
	    
	}
	
	public static Response POSTResponse(String path, String authHeader, String loggedInCookie, String mdSidCookie) {

		return given()
        .contentType("application/json")
        .header("Authorization", authHeader, loggedInCookie, mdSidCookie)
        .cookie("logged_in", loggedInCookie).cookie("md.sid", mdSidCookie)
        .when().post(GetProperty("base.url.api") + GetProperty(path));
		
	}
	
	public static Response GETResponse(String path, String authHeader, String loggedInCookie, String mdSidCookie) {

		return given()
	    .contentType("application/json")
	    .header("Authorization", authHeader, loggedInCookie, mdSidCookie)
        .cookie("logged_in", loggedInCookie).cookie("md.sid", mdSidCookie)
	    .when().get(GetProperty("base.url.api") + GetProperty(path));
		
	}
		
	public static Response DELETEResponse(String path, String authHeader, String loggedInCookie, String mdSidCookie, String itemIdToBeDeleted) {
        
	    return given()
	    .contentType("application/json")
	    .header("Authorization", authHeader, loggedInCookie, mdSidCookie)
        .cookie("logged_in", loggedInCookie).cookie("md.sid", mdSidCookie)
	    .when().delete(GetProperty("base.url.api") + String.format(GetProperty(path), itemIdToBeDeleted));
	
	}
	
	public void registerUserThroughAPI(String registerPayload) {
		Reporter.log("Registering a user");
		String uName = getRandomString(5);
		String password = getRandomString(10);
		String email = getRandomString(15);
		
		response = POSTResponse("register", registerPayload, uName, password, email);
		Assert.assertEquals(response.getStatusCode(), 200, "User is registered successfully");
		Reporter.log("User is registered successfully");
		getAuthorization(uName, password);
		JSONObject responseJson = new JSONObject(response.asString());
		userId = responseJson.getString("id");
		System.out.println("ID:: " + userId);
	}

	public void loginThroughAPI() {
		Reporter.log("User is getting logged in");
		response = GETResponse("login", authHeader);
		Assert.assertEquals(response.getStatusCode(), 200, "User is logged in successfully");

	    System.out.println("Cookies:: "+response.getCookies());
	    Map<String, String> cookies = response.cookies(); //store cookies
	    loggedInCookie = cookies.get("logged_in");
	    mdSidCookie = cookies.get("md.sid");
		Reporter.log("User is logged in successfully");
	}

	public void addItemInCartThroughAPI(String itemPaylod) {
		Reporter.log("Adding item in cart");
		response = POSTResponse("cart", authHeader, loggedInCookie, mdSidCookie, itemPaylod,"USERID", userId);
		Assert.assertEquals(response.getStatusCode(), 201, "Item is added successfully");
		Reporter.log("Item is added successfully");
	}

	public void getItemsFromCartThroughAPI() {
		Reporter.log("Getting items from cart");
		response = GETResponse("cart", authHeader, loggedInCookie, mdSidCookie);
		Assert.assertEquals(response.getStatusCode(), 200, "Items received successfully");
		Reporter.log("Items received successfully");
		String itemDetails = response.asString();
		System.out.println("Item Details:: " + itemDetails);

		JSONArray itemArray = new JSONArray(itemDetails);

		itemId = itemArray.getJSONObject(0).getString("itemId");

		System.out.println("Item ID:: " + itemId);
	}

	public void storeItemToBeDeletedThroughAPI() {
		String itemDetails = response.asString();
		JSONArray itemArray = new JSONArray(itemDetails);
		itemIdToBeDeleted = itemArray.getJSONObject(1).getString("itemId");

		Reporter.log("Item ID to be deleted:: " + itemIdToBeDeleted);
	}

	public void addAddressThroughAPI(String addressPaylod) {
		Reporter.log("Adding address");
		response = POSTResponse("address", authHeader, loggedInCookie, mdSidCookie, addressPaylod, "USERID", userId);
		Assert.assertEquals(response.getStatusCode(), 200, "Address is added successfully");
		Reporter.log("Address is added successfully");
	}

	public void addCardThroughAPI(String cardPaylod) {
		Reporter.log("Adding card details");
		response = POSTResponse("payment", authHeader, loggedInCookie, mdSidCookie, cardPaylod, "USERID", userId);
		Assert.assertEquals(response.getStatusCode(), 200, "Card details is added successfully");
		Reporter.log("Card is added successfully");
	}

	public void updateItemFromCartThroughAPI(String cartPaylod) {
		Reporter.log("Updating cart");
		response = POSTResponse("update.cart", authHeader, loggedInCookie, mdSidCookie, cartPaylod, "ITEMID", itemId);
		Assert.assertEquals(response.getStatusCode(), 202, "Item is updated successfully - " + itemId);
		Reporter.log("Card is updated successfully");
	}

	public void deleteCartItemThroughAPI() {
		Reporter.log("Deleting a cart item");
		response = DELETEResponse("delete.cart", authHeader, loggedInCookie, mdSidCookie, itemIdToBeDeleted);
		Assert.assertEquals(response.getStatusCode(), 202,
				"Item is deleted successfully - " + itemIdToBeDeleted);
		Reporter.log("Item is deleted successfully - " + itemIdToBeDeleted);
	}

	public void placeAnOrderThroughAPI() {
		Reporter.log("Placing an order");
		response = POSTResponse("order", authHeader, loggedInCookie, mdSidCookie);
		Assert.assertEquals(response.getStatusCode(), 201, "Order is placed successfully");
		Reporter.log("Order is placed successfully");
	}

	public void getOrderDetailsThroughAPI() {
		Reporter.log("Getting an order details");
		response = GETResponse("order", authHeader, loggedInCookie, mdSidCookie);
		Assert.assertEquals(response.getStatusCode(), 201, "Order is received successfully");
		Reporter.log("Order details received successfully");
	}

	public void verifyOrderedItemThroughAPI(int itemNo) {
		Reporter.log("Verifying ordered item details");

		String orderDetails = response.asString();
		System.out.println("Order Details:: " + orderDetails);

		JSONObject orderObjectActual = new JSONObject(orderDetails.substring(1, orderDetails.length() - 1));
		JSONArray itemArray = orderObjectActual.getJSONArray("items");

		JSONObject orderObjectExpected = new JSONObject(GetProperty("item.payload"));
		String itemIdExpected = orderObjectExpected.getString("id");

		Assert.assertEquals(itemArray.length(), itemNo, +itemNo + " item is ordered");
		Assert.assertEquals(itemIdExpected, orderObjectExpected.getString("id"), "Item id is same as ordered");
		Reporter.log("Ordered item details verified successfully");

	}

	public void verifyItemIsSameAsOrderedThroughAPI() {
		Reporter.log("Verifying item details are same as ordered");

		String orderDetails = response.asString();

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
		Assert.assertEquals(flag, true, "Item 1 is same as ordered - " + itemIdExpected);

		Reporter.log("Verified Item details are same as ordered successfully");
	}

	public void verifyItemQuantityIsSameAsOrderedThroughAPI() {

		Reporter.log("Verifying Item quantity is same as ordered");

		String orderDetails = response.asString();

		JSONObject orderObjectActual = new JSONObject(orderDetails.substring(1, orderDetails.length() - 1));
		JSONArray itemsArray = orderObjectActual.getJSONArray("items");
		
		JSONObject updatedCartPayloadObject = new JSONObject(GetProperty("item.update.payload"));
		String expectedUpdatedQuantity = updatedCartPayloadObject.getString("quantity");

		for (int i = 0; i <= itemsArray.length() - 1; i++) {
			if (itemsArray.getJSONObject(i).getString("itemId").equals(itemId)) {
				updatedQuantity = itemsArray.getJSONObject(i).get("quantity").toString();
			}
		}

		Assert.assertEquals(updatedQuantity, expectedUpdatedQuantity,
				"Quantity is updated successfully for the item - " + itemId);
		
		Reporter.log("Verified Item quantity is same as ordered successfully");
	}
	
	public void verifyDeletedItemIsNotDisplayedInOrderThroughAPI() {

		Reporter.log("Verifying deleted item is not displayed in order");

		String orderDetails = response.asString();

		JSONObject orderObjectActual = new JSONObject(orderDetails.substring(1, orderDetails.length() - 1));
		JSONArray itemsArray = orderObjectActual.getJSONArray("items");

		boolean flag = true;
		for (int i = 0; i <= itemsArray.length() - 1; i++) {
			if (itemsArray.getJSONObject(i).getString("itemId").equals(itemIdToBeDeleted)) {
				flag = false;
				break;
			}
		}

		Assert.assertNotEquals(itemsArray.length(), 0);
		Assert.assertEquals(flag, true, "Item is deleted successfully - " + itemIdToBeDeleted);
		
		Reporter.log("Verified deleted item is not displayed in order successfully");

	}
	
	public void verifyCartItemThroughAPI() {
		Reporter.log("Verifying cart item details");

		response = GETResponse("cart", authHeader, loggedInCookie, mdSidCookie);
		Assert.assertEquals(response.getStatusCode(), 200, "Items received successfully");
		
		String itemDetails = response.asString();
		System.out.println("Item Details:: " + itemDetails);

		JSONArray itemDetailsArray = new JSONArray(itemDetails);

		Assert.assertEquals(1, itemDetailsArray.length(),
				"Item is added successfully in Cart");

		Reporter.log("Cart item is verified successfully");
	}

	public void verifyUpdatedCartItemThroughAPI() {
		Reporter.log("Verifying updated cart item details");

		String itemDetails = response.asString();
		System.out.println("Item Details after Updation:: " + itemDetails);

		JSONArray itemUpdatedArray = new JSONArray(itemDetails);
		
		JSONObject updatedCartPayloadObject = new JSONObject(GetProperty("item.update.payload"));
		String expectedUpdatedQuantity = updatedCartPayloadObject.getString("quantity");

		for (int i = 0; i <= itemUpdatedArray.length() - 1; i++) {
			if (itemUpdatedArray.getJSONObject(i).getString("itemId").equals(itemId)) {
				updatedQuantity = itemUpdatedArray.getJSONObject(i).get("quantity").toString();
			}
		}

		Assert.assertEquals(updatedQuantity, expectedUpdatedQuantity,
				"Quantity is updated successfully for the item - " + itemId);

		Reporter.log("Quantity is updated successfully for the item - " + itemId);
	}

	public void verifyDeletedCartItemThroughAPI() {
		Reporter.log("Verifying deleted cart item details");

		String itemDetails = response.asString();
		System.out.println("Item Details After Deletion:: " + itemDetails);

		JSONArray itemUpdatedArray = new JSONArray(itemDetails);

		boolean flag = true;

		for (int i = 0; i <= itemUpdatedArray.length() - 1; i++) {
			if (itemUpdatedArray.getJSONObject(i).getString("itemId").equals(itemIdToBeDeleted)) {
				flag = false;
				break;
			}
		}

		Assert.assertNotEquals(itemUpdatedArray.length(), 0);
		Assert.assertEquals(flag, true, "Item is deleted successfully - " + itemIdToBeDeleted);

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
