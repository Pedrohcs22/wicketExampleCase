package n3m6.wicket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import n3m6.entity.Fabricante;
import n3m6.service.FabricanteService;

public class ListarFabricantePanel extends Panel {

	@SpringBean
	private FabricanteService fabricanteService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListarFabricantePanel(String id) {
		super(id);
		
		inicializarCampos();
		inicializarTabela();
	}
	
	@SuppressWarnings("unchecked")
	private void inicializarTabela() {
		 List colunas = new ArrayList<IColumn>();
		 
		 colunas.add(new PropertyColumn<>(Model.of("Nome"), "nome"));
		 colunas.add(new PropertyColumn<>(Model.of("País"), "pais"));
		 
		 DataTable table = new DataTable("tableFabricantes", colunas, new FabricanteProvider(), 10);
		 table.add(new NavigationToolbar(table));
		 table.add(new HeadersToolbar(table, null));
		 
		 // add(table);
		 
	}

	private void inicializarCampos() {
		Form formFabricante = new Form("fabricanteForm");
		
		TextField nomeFabricante = new TextField("nomeFabricante");
		formFabricante.add(nomeFabricante);
		
		TextField paisFabricante = new TextField("paisFabricante");
		formFabricante.add(paisFabricante);
		
		AjaxButton cadastrarFabricante = new AjaxButton("cadastrarFabricante", formFabricante) {
			
			protected void onSubmit(AjaxRequestTarget target) {
				System.out.println("SHEEP");
			}
			
		};
		formFabricante.add(cadastrarFabricante);
		
		add(formFabricante);
	}
	
	private class FabricanteProvider implements IDataProvider<Fabricante> {

		private static final long serialVersionUID = 1L;

		@Override
		public Iterator iterator(long first, long count) {
			return fabricanteService.listar().iterator();
		}

		@Override
		public long size() {
			return fabricanteService.listar().size();
		}

		@Override
		public IModel<Fabricante> model(Fabricante object) {
			return new Model<Fabricante>(object);
		}
		
	}
}
