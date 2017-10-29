package n3m6.wicket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import n3m6.entity.Carro;
import n3m6.entity.Fabricante;
import n3m6.service.CarroService;

public class ConsultaPage extends HomePage {

	@SpringBean
	private CarroService carroService;

	private WebMarkupContainer carrosWrapper;

	private static final long serialVersionUID = 1L;

	public ConsultaPage() {
		inicializarCampos();
	}

	private void inicializarCampos() {
		Form carrosForm = new Form("carrosForm");

		carrosWrapper = new WebMarkupContainer("carrosWrapper");
		carrosWrapper.setOutputMarkupPlaceholderTag(true);
		
		carrosWrapper.add(new RefreshingView<Carro>("carros") {
			@Override
			protected void populateItem(Item<Carro> item) {
				item.add(new Label("modelo", new PropertyModel(item.getModel(), "modelo.descricao")));
				item.add(new Label("placa", new PropertyModel(item.getModel(), "placa")));
				item.add(new Label("tracao", new PropertyModel(item.getModel(), "tracao.nome")));
				item.add(new Label("categoria", new PropertyModel(item.getModel(), "tracao.nome")));
				item.add(new Label("fabricante", new PropertyModel(item.getModel(), "fabricanteFormatado")));
				item.add(new AjaxButton("alterar") {
					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						PageParameters params = new PageParameters();
						params.add("idAlteracao", item.getModelObject().getId());
						setResponsePage(CadastroPage.class, params);
					}
				});
				item.add(new AjaxButton("excluir") {
					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						carroService.remover(item.getModelObject().getId());
						target.add(carrosWrapper);
					}
				});
			}

			@Override
			protected Iterator<IModel<Carro>> getItemModels() {
				final List<IModel<Carro>> carros = new ArrayList<IModel<Carro>>();
				carroService.listar().stream().map(car -> new Model(car)).forEach(carModel -> {
					carros.add(carModel);
				});
				return carros.iterator();
			}
		});

		carrosForm.add(carrosWrapper);
		add(carrosForm);
	}

}
