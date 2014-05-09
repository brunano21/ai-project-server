package ai.server.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		 /*
		 System.out.println(principal);
		 String name = principal.getName();
		 HashMap<String, String> map = new HashMap<String, String>();
		 map.put("name", name);
		 map.put("message", "Spring Security Custom Form example");
		 */
		 Map <String, Object> userModel = new HashMap<String, Object>();
		 userModel.put("username", dati.getUtenti().get(principal.getName()).getNickname());
		 
		 Profilo profilo = (Profilo) dati.getUtenti().get(principal.getName()).getProfilos().iterator().next();
		 userModel.put("creditiPendenti",  profilo.getCreditiPendenti());
		 userModel.put("creditiAcquisiti",  profilo.getCreditiAcquisiti());
		 userModel.put("reputazione",  profilo.getReputazione());
		 
		 return new ModelAndView("userlogged", userModel);
	 }
	 
	 @RequestMapping(value="/login",method = RequestMethod.GET)
	 public String login(Principal principal,RedirectAttributes attributes){
		 if(principal != null){
			 attributes.addFlashAttribute("error", "you're already logged");
			 System.out.println("Redirect");
			 return "redirect:/";
		 }else{
			System.out.println("Login"); 
		 	return "login";
		 }
	 }
	 
	 @RequestMapping(value="/loginfailed",method = RequestMethod.GET)
	 public ModelAndView loginerror(){
		 return new ModelAndView("login","error","true");
	 }
	 
	 @RequestMapping(value="/logout",method = RequestMethod.GET)
	 public ModelAndView logout(){
		 return new ModelAndView("index");
	 }

}
