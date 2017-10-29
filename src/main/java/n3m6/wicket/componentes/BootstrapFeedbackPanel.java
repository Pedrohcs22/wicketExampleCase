package n3m6.wicket.componentes;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class BootstrapFeedbackPanel extends FeedbackPanel {

	    public BootstrapFeedbackPanel(String id) {
	        super(id);
	    }

	    @Override
	    protected String getCSSClass(FeedbackMessage message) {
	        String css;
	        switch (message.getLevel()){
	            case FeedbackMessage.SUCCESS:
	                css = "alert alert-success";
	                break;
	            case FeedbackMessage.INFO:
	                css = "alert alert-info";
	                break;
	            case FeedbackMessage.ERROR:
	                css = "alert alert-error";
	                break;
	            default:
	                css = "alert";
	        }

	        return css;
	    }
	}