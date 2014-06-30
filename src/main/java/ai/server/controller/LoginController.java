package ai.server.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;




import dati.Dati;
import hibernate.Profilo;

@Controller
public class LoginController {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	 @RequestMapping(value="/welcome",method = RequestMethod.GET)
	 public ModelAndView printWelcome(Principal principal){
		 Map <String, Object> userModel = new HashMap<String, Object>();
		 userModel.put("username", dati.getUtenti().get(principal.getName()).getNickname());
		 
		 Profilo profilo = (Profilo) dati.getUtenti().get(principal.getName()).getProfilos().iterator().next();
		 userModel.put("creditiPendenti",  profilo.getCreditiPendenti());
		 userModel.put("creditiAcquisiti",  profilo.getCreditiAcquisiti());
		 userModel.put("reputazione",  profilo.getReputazione());
		 
		 return new ModelAndView("userlogged", userModel);
	 }
	 
	 @RequestMapping(value="/login",method = RequestMethod.GET)
	 public String login(@RequestParam(value = "error", required = false) String error,
		 				Principal principal, 
		 				RedirectAttributes attributes, HttpServletResponse response){
		 
		 if(principal != null){
			 attributes.addFlashAttribute("error", "you're already logged");
			 System.out.println("Redirect");
			 return "redirect:/";
		 } 
		 
		 if(error != null) {
			 /* viene ritornata una jsp indicante un error nel login */
			 response.addHeader("loginFailed", "true");
			 return "loginfailed";
		 }
		 
		 System.out.println("Login");
		 return "login";
	 }
	 
	 @RequestMapping(value="/logout",method = RequestMethod.GET)
	 public ModelAndView logout(){
		 return new ModelAndView("index");
	 }
	 
	 @RequestMapping(value="/custom-index",method = RequestMethod.GET)
	 public ModelAndView getCustomUserPage(HttpServletRequest request, Principal principal, HttpServletResponse response) {
		 
		 if (request.getParameter("latitudine") == null || request.getParameter("longitudine") == null)
			 try {
				response.sendError(666, "Coordinates not found!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 return new ModelAndView("customUserIndex", null);
	 }
	 
}
