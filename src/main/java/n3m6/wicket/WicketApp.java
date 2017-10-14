package n3m6.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class WicketApp extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		// TODO Auto-generated method stub
		return HomePage.class;
	}

}
