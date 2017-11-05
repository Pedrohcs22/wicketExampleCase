package n3m6.wicket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import n3m6.entity.Carro;
import n3m6.service.CarroService;

public class ConsultaPage extends HomePage {

	@SpringBean
	private CarroService carroService;

	private WebMarkupContainer carrosWrapper;

	private WebMarkupContainer labelContainer;

	private WebMarkupContainer filtroContainer;

	private static final long serialVersionUID = 1L;

	private String filtroCarros = "";

	public ConsultaPage() {
		inicializarCampos();
	}

	private void inicializarCampos() {
		Form carrosForm = new Form("carrosForm");

		labelContainer = new WebMarkupContainer("labelContainer") {
			@Override
			protected void onConfigure() {
				setVisible(carroService.count() == 0);
			}
		};
		labelContainer.setOutputMarkupPlaceholderTag(true);

		carrosWrapper = new WebMarkupContainer("carrosWrapper") {
			@Override
			protected void onConfigure() {
				setVisible(carroService.count() > 0);
			}
		};

		carrosWrapper.setOutputMarkupPlaceholderTag(true);

		carrosWrapper.add(new RefreshingView<Carro>("carros") {
			@Override
			protected void populateItem(Item<Carro> item) {
				item.add(new Label("modelo", new PropertyModel(item.getModel(), "modelo.descricao")));
				item.add(new Label("placa", new PropertyModel(item.getModel(), "placa")));
				item.add(new Label("tracao", new PropertyModel(item.getModel(), "tracao.nome")));
				item.add(new Label("categoria", new PropertyModel(item.getModel(), "categoria.nome")));
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
						target.add(carrosWrapper, labelContainer, filtroContainer);
					}
				});
			}

			@Override
			protected Iterator<IModel<Carro>> getItemModels() {
				final List<IModel<Carro>> carrosModel = new ArrayList<IModel<Carro>>();
				List<Carro> carros = getCarrosFiltrados(carroService.listar());
				carros.stream().map(car -> new Model(car)).forEach(carModel -> {
					carrosModel.add(carModel);
				});
				return carrosModel.iterator();
			}
		});

		// Filtro
		filtroContainer = new WebMarkupContainer("filtroContainer") {
			@Override
			protected void onConfigure() {
				setVisible(carroService.count() > 0);
			}
		};
		filtroContainer.setOutputMarkupPlaceholderTag(true);
		TextField filtro = new TextField("filtroCarros", new PropertyModel<>(this, "filtroCarros"));

		filtro.add(new AjaxFormComponentUpdatingBehavior("keyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(carrosWrapper);
			}
		});

		filtroContainer.add(filtro);
		carrosForm.add(filtroContainer);

		carrosForm.add(labelContainer);
		carrosForm.add(carrosWrapper);
		add(carrosForm);
	}

	private List<Carro> getCarrosFiltrados(List<Carro> carros) {
		if (filtroCarros != null && !filtroCarros.isEmpty()) {
			return carros.stream().filter(car -> filtrarCarroPorPropriedades(car)).collect(Collectors.toList());
		}

		return carros;
	}

	private boolean filtrarCarroPorPropriedades(Carro carro) {
		return (carro.getCategoria() != null && carro.getCategoria().getNome().contains(filtroCarros))
				|| (carro.getModelo() != null && carro.getModelo().getDescricao().contains(filtroCarros))
				|| (carro.getPlaca() != null && carro.getPlaca().contains(filtroCarros))
				|| (carro.getTracao() != null && carro.getTracao().getNome().contains(filtroCarros))
				|| (carro.getFabricante() != null && carro.getFabricante().getNome().contains(filtroCarros));
	}

	public String getFiltroCarros() {
		return filtroCarros;
	}

	public void setFiltroCarros(String filtroCarros) {
		this.filtroCarros = filtroCarros;
	}

}
