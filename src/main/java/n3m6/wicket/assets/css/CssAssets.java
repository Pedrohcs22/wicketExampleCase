package n3m6.wicket.assets.css;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.CssResourceReference;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CssAssets {

	public void renderBoostrapCss(IHeaderResponse response, Class classe) {
		response.render(CssHeaderItem.forReference(new CssResourceReference(classe, "assets/css/bootstrap.min.css")));
	}
}
