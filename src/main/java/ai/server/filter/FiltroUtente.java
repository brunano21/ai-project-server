package ai.server.filter;

import hibernate.Profilo;
import hibernate.Utente;

import java.io.IOException;
import java.security.Principal;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dati.Dati;

/**
 * Servlet Filter implementation class FiltroUtente
 */
public class FiltroUtente implements Filter {

	private ApplicationContext context;
	private Dati dati;
    /**
     * Default constructor. 
     */
    public FiltroUtente() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HttpServletResponse resp = (HttpServletResponse) response;
		if(auth.getName() != "anonymousUser"){
			if(dati != null){
				Utente utente = dati.getUtenti().get(auth.getName());
				Profilo profilo = null;
				for(Profilo p : (Set<Profilo>) utente.getProfilos()){
					profilo = p;
					break;
				}
				
				if(profilo.getReputazione() == 0){
					request.setAttribute("dati", dati);
					request.setAttribute("error", "Non hai i requisiti per andare in quella pagina");
					request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
				}
			}
		}
		chain.doFilter(request, response);
		// pass the request along the filter chain
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		context = WebApplicationContextUtils.getWebApplicationContext(fConfig.getServletContext());
		dati = (Dati)context.getBean("dati");
	}

}
