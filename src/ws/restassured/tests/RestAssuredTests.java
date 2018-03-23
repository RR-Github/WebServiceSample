package ws.restassured.tests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ws.restassured.utilities.APIRequestRA;

public class RestAssuredTests {

	APIRequestRA apiRequestRA = new APIRequestRA();

    @BeforeMethod
    public void setUp() {
	    System.out.println("-----Start Testcase-----");
	    apiRequestRA.registerUserThroughAPI("register.payload");
	    apiRequestRA.loginThroughAPI();
    }
    
	@Test(testName = "1. Place An Order", enabled = true)
	public void placeAnOrderThoughAPI() {
		apiRequestRA.addItemInCartThroughAPI("item.payload");
		apiRequestRA.addAddressThroughAPI("address.payload");
		apiRequestRA.addCardThroughAPI("card.payload");
		apiRequestRA.placeAnOrderThroughAPI();
		apiRequestRA.getOrderDetailsThroughAPI();
		apiRequestRA.verifyOrderedItemThroughAPI(1);
	}
	
	@Test(testName = "2. Update and Delete items from cart", enabled = true)
	public void updateCartThroughAPI() {
		
 		apiRequestRA.addItemInCartThroughAPI("item.payload");
		apiRequestRA.addItemInCartThroughAPI("item.payload1");
		apiRequestRA.getItemsFromCartThroughAPI();
		
		apiRequestRA.updateItemFromCartThroughAPI("item.update.payload");
		apiRequestRA.getItemsFromCartThroughAPI();
		apiRequestRA.storeItemToBeDeletedThroughAPI();
		apiRequestRA.verifyUpdatedCartItemThroughAPI();
		
		apiRequestRA.deleteCartItemThroughAPI();
		apiRequestRA.getItemsFromCartThroughAPI();
		apiRequestRA.verifyDeletedCartItemThroughAPI();
		
	}
	
	
	@Test(testName = "3. Place an order with multiple items", enabled = true)
	public void updateCartAndPlaceAnOrdeThroughAPI() {
		
 		apiRequestRA.addItemInCartThroughAPI("item.payload");
		apiRequestRA.addItemInCartThroughAPI("item.payload1");
		apiRequestRA.addItemInCartThroughAPI("item.payload2");

		apiRequestRA.getItemsFromCartThroughAPI();
		
		apiRequestRA.updateItemFromCartThroughAPI("item.update.payload");
		apiRequestRA.getItemsFromCartThroughAPI();
		apiRequestRA.storeItemToBeDeletedThroughAPI();
		apiRequestRA.verifyUpdatedCartItemThroughAPI();
		
		apiRequestRA.deleteCartItemThroughAPI();
		apiRequestRA.getItemsFromCartThroughAPI();
		apiRequestRA.verifyDeletedCartItemThroughAPI();
		
		apiRequestRA.addAddressThroughAPI("address.payload");
		apiRequestRA.addCardThroughAPI("card.payload");
		apiRequestRA.placeAnOrderThroughAPI();
		
		apiRequestRA.getOrderDetailsThroughAPI();
		
		apiRequestRA.verifyOrderedItemThroughAPI(2);
		apiRequestRA.verifyItemIsSameAsOrderedThroughAPI();
		apiRequestRA.verifyItemQuantityIsSameAsOrderedThroughAPI();
		apiRequestRA.verifyDeletedItemIsNotDisplayedInOrderThroughAPI();
	}
	
	@AfterMethod
	public void tearDown() {
	    System.out.println("-----End Testcase-------");
	}
	
}
