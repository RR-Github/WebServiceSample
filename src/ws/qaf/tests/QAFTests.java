package ws.qaf.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ws.qaf.utilities.APIRequestQAF;

public class QAFTests {
	
	APIRequestQAF apiRequestQAF = new APIRequestQAF();

    @BeforeMethod
    public void setUp() {
	    System.out.println("-----Start Testcase-----");
	    apiRequestQAF.registerUserThroughAPI("register.payload");
	    apiRequestQAF.loginThroughAPI();
    }
    
	@Test(testName = "1. Place An Order", enabled = true)
	public void placeAnOrderThoughAPI() {
		apiRequestQAF.addItemInCartThroughAPI("item.payload");
		apiRequestQAF.addAddressThroughAPI("address.payload");
		apiRequestQAF.addCardThroughAPI("card.payload");
		apiRequestQAF.placeAnOrderThroughAPI();
		apiRequestQAF.getOrderDetailsThroughAPI();
		apiRequestQAF.verifyOrderedItemThroughAPI(1);
	}
	
	@Test(testName = "2. Update and Delete items from cart", enabled = true)
	public void updateCartThroughAPI() {
		
 		apiRequestQAF.addItemInCartThroughAPI("item.payload");
		apiRequestQAF.addItemInCartThroughAPI("item.payload1");
		apiRequestQAF.getItemsFromCartThroughAPI();
		
		apiRequestQAF.updateItemFromCartThroughAPI("item.update.payload");
		apiRequestQAF.getItemsFromCartThroughAPI();
		apiRequestQAF.storeItemToBeDeletedThroughAPI();
		apiRequestQAF.verifyUpdatedCartItemThroughAPI();
		
		apiRequestQAF.deleteCartItemThroughAPI();
		apiRequestQAF.getItemsFromCartThroughAPI();
		apiRequestQAF.verifyDeletedCartItemThroughAPI();
		
	}
	
	
	@Test(testName = "3. Place an order with multiple items", enabled = true)
	public void updateCartAndPlaceAnOrdeThroughAPI() {
		
 		apiRequestQAF.addItemInCartThroughAPI("item.payload");
		apiRequestQAF.addItemInCartThroughAPI("item.payload1");
		apiRequestQAF.addItemInCartThroughAPI("item.payload2");

		apiRequestQAF.getItemsFromCartThroughAPI();
		
		apiRequestQAF.updateItemFromCartThroughAPI("item.update.payload");
		apiRequestQAF.getItemsFromCartThroughAPI();
		apiRequestQAF.storeItemToBeDeletedThroughAPI();
		apiRequestQAF.verifyUpdatedCartItemThroughAPI();
		
		apiRequestQAF.deleteCartItemThroughAPI();
		apiRequestQAF.getItemsFromCartThroughAPI();
		apiRequestQAF.verifyDeletedCartItemThroughAPI();
		
		apiRequestQAF.addAddressThroughAPI("address.payload");
		apiRequestQAF.addCardThroughAPI("card.payload");
		apiRequestQAF.placeAnOrderThroughAPI();
		
		apiRequestQAF.getOrderDetailsThroughAPI();
		
		apiRequestQAF.verifyOrderedItemThroughAPI(2);
		apiRequestQAF.verifyItemIsSameAsOrderedThroughAPI();
		apiRequestQAF.verifyItemQuantityIsSameAsOrderedThroughAPI();
		apiRequestQAF.verifyDeletedItemIsNotDisplayedInOrderThroughAPI();
	}
	
	@AfterMethod
	public void tearDown() {
	    System.out.println("-----End Testcase-------");
	}
	
}
