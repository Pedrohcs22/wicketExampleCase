package n3m6.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class CadastroPage extends WebPage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	public CadastroPage() {
		add(new Label("labelDeTeste", "teste"));
	}
	
	
}
