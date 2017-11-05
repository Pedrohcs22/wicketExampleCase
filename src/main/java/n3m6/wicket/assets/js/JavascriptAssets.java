package n3m6.wicket.assets.js;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JavascriptAssets {

	public void renderBootstrapHeader(IHeaderResponse response, Class pagina) {
		response.render(JavaScriptHeaderItem
				.forUrl("https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(pagina, "assets/js/bootstrap.min.js")));
	}
	
	public void renderCadastroAssets(IHeaderResponse response, Class pagina) {
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(pagina, "assets/js/jquery.mask.min.js")));
		response.render(JavaScriptHeaderItem
				.forReference(new JavaScriptResourceReference(pagina, "assets/js/cadastro.js")));
	}
}
