package n3m6.wicket;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.DefaultCssAutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import n3m6.entity.Carro;
import n3m6.entity.Modelo;
import n3m6.enums.Tracao;
import n3m6.service.CarroService;
import n3m6.service.ModeloService;
import n3m6.wicket.componentes.BootstrapFeedbackPanel;

public class CadastroPage extends HomePage {

	@SpringBean
	private ModeloService modeloService;
	
	@SpringBean
	private CarroService carroService;

	private IModel<Carro> modelCadastro = new Model(new Carro());
	private ModalWindow modalFabricante = new ModalWindow("modalFabricante");

	private IModel<String> textoBotaoConfirmar = Model.of("");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	public CadastroPage(PageParameters p) {
		inicializarCamposModal();
		inicializarCampos();
		
		String idAlteracao = p.get("idAlteracao").toString();
		if(idAlteracao != null) {
			modelCadastro.setObject(carroService.obter(Integer.parseInt(idAlteracao)));
			textoBotaoConfirmar.setObject("Cadastrar");
		}
		
		textoBotaoConfirmar.setObject("Salvar");
	}

	private void inicializarCamposModal() {
		modalFabricante.setTitle(Model.of("Pesquisar Fabricante"));
		modalFabricante.setContent(new ListarFabricantePanel(modalFabricante.getContentId(), 
				new PropertyModel(modelCadastro, "fabricante")
				,modalFabricante));
		modalFabricante.showUnloadConfirmation(false);
		add(modalFabricante);
	}

	private void inicializarCampos() {
		Form cadastrarForm = new Form("cadastrarForm");

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
						String id = string.substring(0, string.indexOf("-")).trim();

						// Já cadastrado
						if(id != null) {
							return (Modelo) modeloService.obter(Integer.parseInt(id));
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
		autoComplete.setRequired(true);
		cadastrarForm.add(autoComplete);

		// Fabricante
		TextField nomeFabricante = new TextField<>("nomeFabricante",
				new PropertyModel<String>(modelCadastro, "fabricante.nome"));
		nomeFabricante.setEnabled(false);
		nomeFabricante.setRequired(true);
		cadastrarForm.add(nomeFabricante);
		
		TextField paisFabricante = new TextField<>("paisFabricante",
				new PropertyModel<String>(modelCadastro, "fabricante.pais"));
		paisFabricante.setEnabled(false);
		cadastrarForm.add(paisFabricante);
		
		AjaxLink ajaxLink = new AjaxLink<Void>("pesquisarFabricante") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				modalFabricante.show(target);
			}
		};
		cadastrarForm.add(ajaxLink);

		// Placa
		TextField inputPlaca = new TextField<>("inputPlaca", new PropertyModel<String>(modelCadastro, "placa"));
		inputPlaca.setRequired(true);
		cadastrarForm.add(inputPlaca);

		RadioChoice<Tracao> tracao = new RadioChoice<Tracao>("tracaoRadio",
				new PropertyModel<Tracao>(modelCadastro, "tracao"), Arrays.asList(Tracao.values()),
				new ChoiceRenderer<Tracao>() {

					@Override
					public Object getDisplayValue(Tracao object) {
						return object.getNome();
					}

					@Override
					public String getIdValue(Tracao object, int index) {
						return object.name();
					}

				});
		
		tracao.setRequired(true);
		cadastrarForm.add(tracao);

		BootstrapFeedbackPanel feedback = new BootstrapFeedbackPanel("feedbackPanel");
		feedback.add(AttributeAppender.append("class", "alert alert-danger"));
		cadastrarForm.add(feedback);

		cadastrarForm.add(new Button("botaoSalvar") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				if(carroService.countByPlacaLike(modelCadastro.getObject().getPlaca()) > 0) {
					Session.get().error("Placa já cadastrada");
				}
				carroService.salvar(modelCadastro.getObject());
				setResponsePage(ConsultaPage.class);
			}
		});

		add(cadastrarForm);
	}

}
