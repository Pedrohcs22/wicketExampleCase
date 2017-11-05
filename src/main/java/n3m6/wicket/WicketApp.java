package n3m6.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.hibernate.cfg.beanvalidation.BeanValidationIntegrator;

public class WicketApp extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		// TODO Auto-generated method stub
		return HomePage.class;
	}
	
	@Override
	protected void init() {
		super.init();
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		mountPage("/carros", ConsultaPage.class);
		mountPage("/carro", CadastroPage.class);
		mountPage("/carro/${idAlteracao}", CadastroPage.class);
		
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8"); 
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8"); 
        
        new BeanValidationConfiguration().configure(this);
        
	}
	
	private void onInit() {
		IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard();
		if (packageResourceGuard instanceof SecurePackageResourceGuard) {
			SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
			guard.addPattern("+*.js");
			guard.addPattern("+*.css");
		}
	}
}
