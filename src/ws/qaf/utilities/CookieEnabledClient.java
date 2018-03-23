package ws.qaf.utilities;

import com.qmetry.qaf.automation.rest.client.BasicAuthRestClient;
import com.sun.jersey.api.client.Client;

public class CookieEnabledClient extends BasicAuthRestClient  {
	
	public static String REST_CLIENT_BASIC_AUTH_USER = "rest.client.basic.auth.username";
    public static String REST_CLIENT_BASIC_AUTH_PASSWORD = "rest.client.basic.auth.password";
    
	@Override
	protected Client createClient() {
		
		Client client = super.createClient();
		client.getProperties().put("jersey.config.client.followRedirects", true);
		client.addFilter(new SessionManagementFilter());
		
		return client;
		
	}

}
