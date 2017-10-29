package n3m6.wicket;

import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.util.encoding.CharSetUtil;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.http.WebResponse;

import n3m6.wicket.assets.css.CssAssets;
import n3m6.wicket.assets.js.JavascriptAssets;

public class HomePage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HomePage() {
		add(new AjaxLink<Void>("cadastroLink") {

			@Override
			public void onClick(AjaxRequestTarget arg0) {
				setResponsePage(CadastroPage.class);
			}
		});
		
		add(new AjaxLink<Void>("consultaLink") {

			@Override
			public void onClick(AjaxRequestTarget arg0) {
				setResponsePage(ConsultaPage.class);
			}
		});
		
		
	}

	
	@Override
	public void renderHead(IHeaderResponse response) {
		JavascriptAssets.renderBootstrapHeader(response, HomePage.class);
		CssAssets.renderBoostrapCss(response, HomePage.class);
	}
	
}
