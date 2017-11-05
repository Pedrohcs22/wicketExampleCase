package n3m6.wicket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import n3m6.entity.Fabricante;
import n3m6.service.FabricanteService;
import n3m6.wicket.componentes.BootstrapFeedbackPanel;

public class ListarFabricantePanel extends Panel {

	@SpringBean
	private FabricanteService fabricanteService;

	private ModalWindow parentModal;

	private Form formFabricante;
	
	private IModel<Fabricante> fabricanteModel = Model.of(new Fabricante());
	
	private IModel<Fabricante> fabricanteOriginalModel;
	
	private WebMarkupContainer novoFabricanteContainer;
	
	private WebMarkupContainer fabricantesListWrapper;
	
	private FeedbackPanel feedbackPanel;
	
	private TextField paisFabricante;
	
	private AjaxButton cadastrarFabricante;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListarFabricantePanel(String id, IModel<Fabricante> fabricanteOriginalModel , ModalWindow parentModal) {
		super(id);
		this.parentModal = parentModal;
		this.fabricanteOriginalModel = fabricanteOriginalModel;
		
		inicializarCampos();
		inicializarTabela();
	}

	@SuppressWarnings("unchecked")
	private void inicializarTabela() {
		
		fabricantesListWrapper = new WebMarkupContainer("fabricantesListWrapper");
		fabricantesListWrapper.setOutputMarkupId(true);
		
		fabricantesListWrapper.add(new RefreshingView<Fabricante>("fabricantes") {
			@Override
			protected void populateItem(Item<Fabricante> item) {
				item.add(new Label("nome", new PropertyModel(item.getModel(), "nome")));
				item.add(new Label("pais", new PropertyModel(item.getModel(), "pais")));
				item.add(new AjaxButton("selecionar") {
					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						fabricanteOriginalModel.setObject(item.getModelObject());
						parentModal.close(target);
						target.add(parentModal.getPage());
					}
					
					@Override
					protected void onError(AjaxRequestTarget target) {
						target.add(feedbackPanel);
					}
				});
			}

			@Override
			protected Iterator<IModel<Fabricante>> getItemModels() {
				final List<IModel<Fabricante>> fabricantes = new ArrayList<IModel<Fabricante>>();
				List<Fabricante> fabricantesEntity;
				if(fabricanteModel.getObject().getNome() != null) {
					fabricantesEntity = fabricanteService.listarByNomeContemIgnoreCase(
							fabricanteModel.getObject().getNome());
				} else {
					fabricantesEntity = fabricanteService.listar();
				}
			
				fabricantesEntity.stream().forEach(fab -> fabricantes.add(Model.of(fab)));
				return fabricantes.iterator();
			}
		});

		formFabricante.add(fabricantesListWrapper);
		feedbackPanel = new BootstrapFeedbackPanel("feedbackPanel") {
			@Override
			protected void onConfigure() {
				setVisible(formFabricante.hasError());
			}
		};
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
		add(feedbackPanel);
	}

	private void inicializarCampos() {
		novoFabricanteContainer = new WebMarkupContainer("novoFabricanteContainer");
		
		formFabricante = new Form("fabricanteForm");

		TextField nomeFabricante = new TextField("nomeFabricante",
				new PropertyModel<String>(fabricanteModel, "nome"));
		formFabricante.add(nomeFabricante);
		nomeFabricante.add(new AjaxFormComponentUpdatingBehavior("keyup") {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if(fabricanteModel.getObject().getNome() != null
						&& fabricanteService.countByNomeContemIgnoreCase(
								fabricanteModel.getObject().getNome()) == 0) {
					novoFabricanteContainer.setVisible(true);
				} else {
					novoFabricanteContainer.setVisible(false);
				}
			
				target.add(fabricantesListWrapper, novoFabricanteContainer);
			}
		});
		
		paisFabricante = new TextField<>("paisFabricante", new PropertyModel<>(fabricanteModel, "pais"));
		paisFabricante.add(new PropertyValidator());
		novoFabricanteContainer.add(paisFabricante);
		
		cadastrarFabricante = new AjaxButton("cadastrarFabricante", formFabricante) {

			protected void onSubmit(AjaxRequestTarget target) {
				// Salva novo fabricante
				Fabricante novoFabricante = new Fabricante();
				novoFabricante.setNome(fabricanteModel.getObject().getNome());
				novoFabricante.setPais(fabricanteModel.getObject().getPais());
				
				fabricanteService.salvar(novoFabricante);
				
				fabricanteOriginalModel.setObject(novoFabricante);
				parentModal.close(target);
				target.add(parentModal.getPage());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
				target.add(feedbackPanel);
			}
		};
		novoFabricanteContainer.add(cadastrarFabricante);
		
		novoFabricanteContainer.setOutputMarkupPlaceholderTag(true);
		novoFabricanteContainer.setVisible(false);
		
		formFabricante.add(novoFabricanteContainer);
		add(formFabricante);
	}
	
	public void cleanStart() {
		fabricanteModel.getObject().setNome("");
		fabricanteModel.getObject().setPais("");
	}

}
