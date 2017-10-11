package n3m6.filter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.apache.wicket.protocol.http.WicketFilter;

@WebFilter(urlPatterns = "/*", initParams = {
    @WebInitParam(name = "applicationClassName", value = "n3m6.wicket.WicketApp")
})
public class AppFilter extends WicketFilter {}
