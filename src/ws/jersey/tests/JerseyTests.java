package ws.jersey.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ws.jersey.utilities.APIRequestJersey;;

public class JerseyTests {
	
	APIRequestJersey apiRequestJersey = new APIRequestJersey();

    @BeforeMethod
    public void setUp() {
	    System.out.println("-----Start Testcase-----");
	    apiRequestJersey.registerUserThroughAPI("register.payload");
	    apiRequestJersey.loginThroughAPI();
    }
    
	@Test(testName = "1. Place An Order", enabled = true)
	public void placeAnOrderThoughAPI() {
		apiRequestJersey.addItemInCartThroughAPI("item.payload");
		apiRequestJersey.addAddressThroughAPI("address.payload");
		apiRequestJersey.addCardThroughAPI("card.payload");
		apiRequestJersey.placeAnOrderThroughAPI();
		apiRequestJersey.getOrderDetailsThroughAPI();
		apiRequestJersey.verifyOrderedItemThroughAPI(1);
	}
	
	@Test(testName = "2. Update and Delete items from cart", enabled = true)
	public void updateCartThroughAPI() {
		
 		apiRequestJersey.addItemInCartThroughAPI("item.payload");
		apiRequestJersey.addItemInCartThroughAPI("item.payload1");
		apiRequestJersey.getItemsFromCartThroughAPI();
		
		apiRequestJersey.updateItemFromCartThroughAPI("item.update.payload");
		apiRequestJersey.getItemsFromCartThroughAPI();
		apiRequestJersey.storeItemToBeDeletedThroughAPI();
		apiRequestJersey.verifyUpdatedCartItemThroughAPI();
		
		apiRequestJersey.deleteCartItemThroughAPI();
		apiRequestJersey.getItemsFromCartThroughAPI();
		apiRequestJersey.verifyDeletedCartItemThroughAPI();
		
	}
	
	
	@Test(testName = "3. Place an order with multiple items", enabled = true)
	public void updateCartAndPlaceAnOrdeThroughAPI() {
		
 		apiRequestJersey.addItemInCartThroughAPI("item.payload");
		apiRequestJersey.addItemInCartThroughAPI("item.payload1");
		apiRequestJersey.addItemInCartThroughAPI("item.payload2");

		apiRequestJersey.getItemsFromCartThroughAPI();
		
		apiRequestJersey.updateItemFromCartThroughAPI("item.update.payload");
		apiRequestJersey.getItemsFromCartThroughAPI();
		apiRequestJersey.storeItemToBeDeletedThroughAPI();
		apiRequestJersey.verifyUpdatedCartItemThroughAPI();
		
		apiRequestJersey.deleteCartItemThroughAPI();
		apiRequestJersey.getItemsFromCartThroughAPI();
		apiRequestJersey.verifyDeletedCartItemThroughAPI();
		
		apiRequestJersey.addAddressThroughAPI("address.payload");
		apiRequestJersey.addCardThroughAPI("card.payload");
		apiRequestJersey.placeAnOrderThroughAPI();
		
		apiRequestJersey.getOrderDetailsThroughAPI();
		
		apiRequestJersey.verifyOrderedItemThroughAPI(2);
		apiRequestJersey.verifyItemIsSameAsOrderedThroughAPI();
		apiRequestJersey.verifyItemQuantityIsSameAsOrderedThroughAPI();
		apiRequestJersey.verifyDeletedItemIsNotDisplayedInOrderThroughAPI();
	}
	
	@AfterMethod
	public void tearDown() {
	    System.out.println("-----End Testcase-------");
	}
}
