package n3m6.wicket;

import java.util.Iterator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringBean;

import n3m6.entity.Carro;
import n3m6.entity.Modelo;
import n3m6.service.ModeloService;

public class CadastroPage extends HomePage {

	@SpringBean
	private ModeloService modeloService;
	
	private IModel<Carro> modelCadastro = new Model(new Carro());

	private ModalWindow modalFabricante = new ModalWindow("modalFabricante");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	public CadastroPage() {
		inicializarCampos();
		inicializarCamposModal();
	}

	private void inicializarCamposModal() {
		modalFabricante.add(new Label("teste", "teste"));
	}
	
	private void inicializarCampos() {
		Form cadastrarForm = new Form("cadastrarForm");

		AutoCompleteSettings opts = new AutoCompleteSettings();
		opts.setCssClassName("ontopoly-autocompleter");
		opts.setAdjustInputWidth(false);
		opts.setPreselect(true);
		
		// Modelo
		AutoCompleteSettings autoCompleteSettings = new AutoCompleteSettings();
	    autoCompleteSettings.setShowListOnEmptyInput(false);
	    autoCompleteSettings.setMaxHeightInPx(200);
	   
	    AutoCompleteTextField<Modelo> autoComplete = new AutoCompleteTextField<Modelo>(
	    		"modelosAutoComplete", 
	    		new PropertyModel<>(modelCadastro, "modelo"), 
	    		Modelo.class,
	    		getModeloRenderer(),
	    		autoCompleteSettings) {

			private static final long serialVersionUID = 1L;

			@Override
	        protected Iterator<Modelo> getChoices(String input) {
	            return modeloService.listar().iterator();
	        }
	    };
	    autoComplete.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(autoComplete);
			}
		});
	    
		cadastrarForm.add(autoComplete);
		
		// Placa
		TextField inputPlaca = new TextField<>("inputPlaca", new PropertyModel<String>(modelCadastro, "placa"));
		cadastrarForm.add(inputPlaca);

		cadastrarForm.add(new Button("botaoSalvar") {
			@Override
			public void onSubmit() {
				info("Carro salvo com sucesso");
			}
		});

		add(cadastrarForm);
	}
	
	private IAutoCompleteRenderer<Modelo> getModeloRenderer() {
		return new IAutoCompleteRenderer<Modelo>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void render(Modelo object, Response response, String criteria) {
				response.write("<li>" + object.getDescricao() + " </li>");
			}

			@Override
			public void renderHeader(Response response) {
				response.write("");
			}

			@Override
			public void renderFooter(Response response, int count) {
				response.write("");
			}
		};
	}
}
