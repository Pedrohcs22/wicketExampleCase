package n3m6.wicket;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.DefaultCssAutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import n3m6.entity.Carro;
import n3m6.entity.Modelo;
import n3m6.enums.Categoria;
import n3m6.enums.Tracao;
import n3m6.service.CarroService;
import n3m6.service.ModeloService;
import n3m6.wicket.assets.js.JavascriptAssets;
import n3m6.wicket.componentes.BootstrapFeedbackPanel;

public class CadastroPage extends HomePage {

	@SpringBean
	private ModeloService modeloService;

	@SpringBean
	private CarroService carroService;

	private IModel<Carro> modelCadastro = new Model<Carro>(new Carro());
	private ModalWindow modalFabricante = new ModalWindow("modalFabricante");

	private ListarFabricantePanel listarFabricantePanel;

	private IModel<String> textoBotaoConfirmar = Model.of("");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CadastroPage(PageParameters p) {
		inicializarCamposModal();
		inicializarCampos();

		String idAlteracao = p.get("idAlteracao").toString();
		if (idAlteracao != null) {
			modelCadastro.setObject(carroService.obter(Integer.parseInt(idAlteracao)));
			textoBotaoConfirmar.setObject("Cadastrar");
		}

		textoBotaoConfirmar.setObject("Salvar");
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		JavascriptAssets.renderCadastroAssets(response, CadastroPage.class);
	}

	private void inicializarCamposModal() {
		modalFabricante.setTitle(Model.of("Pesquisar Fabricante"));
		listarFabricantePanel = new ListarFabricantePanel(modalFabricante.getContentId(),
				new PropertyModel<>(modelCadastro, "fabricante"), modalFabricante);
		modalFabricante.setContent(listarFabricantePanel);
		modalFabricante.showUnloadConfirmation(false);
		add(modalFabricante);
	}

	private void inicializarCampos() {
		Form cadastrarForm = new Form<>("cadastrarForm");

		AutoCompleteSettings opts = new AutoCompleteSettings();
		opts.setCssClassName("ontopoly-autocompleter");
		opts.setAdjustInputWidth(false);
		opts.setPreselect(true);

		DefaultCssAutoCompleteTextField<Modelo> autoComplete = new DefaultCssAutoCompleteTextField<Modelo>(
				"modelosAutoComplete", new PropertyModel<>(modelCadastro, "modelo")) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<Modelo> getChoices(String input) {
				return modeloService.listar().iterator();
			}

			@Override
			@SuppressWarnings("unchecked")
			public <C> IConverter<C> getConverter(Class<C> type) {
				return (IConverter<C>) new IConverter<Modelo>() {

					@Override
					public Modelo convertToObject(String string, Locale arg1) throws ConversionException {
						Modelo model = modeloService.getByDescricao(string);

						// Já cadastrado
						if (model != null) {
							return model;
						} else {
							Modelo modelo = new Modelo();
							modelo.setDescricao(string);
							modeloService.salvar(modelo);

							return modelo;
						}
					}

					@Override
					public String convertToString(Modelo modelo, Locale arg1) {
						return (String) modelo.toString();
					}

				};
			}
		};
		autoComplete.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(autoComplete);
			}
		});
		autoComplete.add(new PropertyValidator());
		cadastrarForm.add(autoComplete);

		// Fabricante
		TextField nomeFabricante = new TextField<>("nomeFabricante",
				new PropertyModel<String>(modelCadastro, "fabricante.nome"));
		nomeFabricante.setEnabled(false);
		nomeFabricante.add(new PropertyValidator());
		cadastrarForm.add(nomeFabricante);

		// País fabricante
		TextField paisFabricante = new TextField<>("paisFabricante",
				new PropertyModel<String>(modelCadastro, "fabricante.pais"));
		paisFabricante.setEnabled(false);
		paisFabricante.add(new PropertyValidator());
		cadastrarForm.add(paisFabricante);

		//Botão pesquisar fabricante
		AjaxLink ajaxLink = new AjaxLink<Void>("pesquisarFabricante") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				listarFabricantePanel.cleanStart();
				modalFabricante.show(target);
			}
		};
		cadastrarForm.add(ajaxLink);

		// Placa
		TextField inputPlaca = new TextField<>("inputPlaca", new PropertyModel<String>(modelCadastro, "placa"));
		inputPlaca.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// ModelUpdate Only - Evita que os dados sejam perdidos após seleção de target
				// de fabricante
			}
		});
		inputPlaca.setMarkupId("inputPlaca").setOutputMarkupId(true);
		inputPlaca.add(placaNaoRepetidaValidator());
		inputPlaca.add(new PropertyValidator());

		cadastrarForm.add(inputPlaca);

		// Tração
		RadioChoice<Tracao> tracao = new RadioChoice<Tracao>("tracaoRadio",
				new PropertyModel<Tracao>(modelCadastro, "tracao"), Arrays.asList(Tracao.values()),
				new ChoiceRenderer<Tracao>("nome"));
		tracao.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Model update only
			}
		});
		tracao.add(new PropertyValidator());
		cadastrarForm.add(tracao);

		// Categorias drop down
		DropDownChoice dropDown = new DropDownChoice<Categoria>("categoriasDropDown",
				new PropertyModel<Categoria>(modelCadastro, "categoria"), Arrays.asList(Categoria.values()),
				new ChoiceRenderer<Categoria>("nome"));
		dropDown.add(new PropertyValidator());
		cadastrarForm.add(dropDown);

		BootstrapFeedbackPanel feedback = new BootstrapFeedbackPanel("feedbackPanel") {
			@Override
			protected void onConfigure() {
				setVisible(cadastrarForm.hasError());
			}
		};
		feedback.add(new AttributeAppender("class", "alert alert-danger"));
		cadastrarForm.add(feedback);

		// Botão salvar
		cadastrarForm.add(new Button("botaoSalvar") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				carroService.salvar(modelCadastro.getObject());
				setResponsePage(ConsultaPage.class);
			}
		});

		add(cadastrarForm);
	}

	public IValidator<String> placaNaoRepetidaValidator() {
		return validatable -> {
			boolean placaValida = true;
			String placa = validatable.getValue();

			if (modelCadastro.getObject().getId() == null && carroService.countByPlacaLike(placa) > 0) {
				placaValida = false;
			}

			if (modelCadastro.getObject().getId() != null) {
				Carro oldCar = carroService.getByPlaca(placa);
				if (oldCar != null && !oldCar.getId().equals(modelCadastro.getObject().getId())) {
					placaValida = false;
				}
			}

			if (!placaValida) {
				validatable.error(new ValidationError().setMessage("Placa já cadastrada"));
			}

		};
	}

}
