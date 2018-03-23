package ws.qaf.utilities;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.NewCookie;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.util.Reporter;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public class SessionManagementFilter extends ClientFilter {

	@SuppressWarnings("unchecked")
	@Override
	public ClientResponse handle(ClientRequest request) throws ClientHandlerException {

		ArrayList<NewCookie> cookies = new ArrayList<NewCookie>();
		Object ck = ConfigurationManager.getBundle().getObject("api.integration.cookie");
		if (ck != null) {
			if (ck instanceof NewCookie)
				cookies.add((NewCookie) ck);
			else
				cookies.addAll((Collection<? extends NewCookie>) ck);
		}
		StringBuffer buffer = new StringBuffer();

		if (cookies != null) {
			for (NewCookie n : cookies) {
				buffer.append(n.getName() + "=" + n.getValue() + ";");
			}
		}
		request.getHeaders().putSingle("Cookie", buffer.toString());
		Reporter.log(request.toString());
		ClientResponse response = getNext().handle(request);
		if (response.getCookies() != null) {
			cookies.addAll(response.getCookies());
			ConfigurationManager.getBundle().setProperty("api.integration.cookie", cookies);
		}
		
		return response;
	}

}