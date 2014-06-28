package ai.server.controller;

import hibernate.Utente;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;






import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.mobile.device.Device;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;
import dati.Registration;

@Controller
public class RegisterController {
	@Autowired
	private RegistrationValidation registrationValidation;
	
	
	public void setRegistrationValidation(RegistrationValidation registrationValidation){
		this.registrationValidation = registrationValidation;
	}
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String showForm(Model model, Device device){
		
		String pagina = "register";
		
//		if(device.isMobile()){
//			System.out.println("questo è uno smartphone");
//		}
		
//		if(device.isNormal()){
			Registration registration = new Registration();
			model.addAttribute("registration", registration);	
//		}
		return pagina;
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	@ResponseBody
	public void processRegistration(@Valid Registration registration, BindingResult result, HttpServletRequest request, HttpServletResponse response){
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	
		registrationValidation.validate(registration, result);
		String numerocasuale = java.util.UUID.randomUUID().toString();
		if(result.hasErrors()){
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			for(FieldError fieldError : result.getFieldErrors())
				response.setHeader(fieldError.getField(), fieldError.getDefaultMessage());
			return;
		}
		try{
			dati.inserisciUtente(registration.getEmail(), registration.getUserName(), registration.getPassword(), new Date(), numerocasuale);
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			response.setHeader("database", e.getMessage());
			return;
		}
		
		Mail mail = (Mail) context.getBean("mail");

		String [] temp = request.getRequestURL().toString().split("/");
		String url = temp[0]+"//"+temp[1]+temp[2]+"/"+temp[3]+"/";
		mail.sendMail("brunano21@gmail.com", registration.getEmail(), "Registration Confirmation", "Click the link above to confirm your registration\n\n\n"+"<a href='"+url+"confirmregistration?numeroCasuale="+numerocasuale+"&email="+registration.getEmail()+"' />");
		response.setStatus(HttpServletResponse.SC_OK);
		
		return;
	}
	
	@RequestMapping(value="/confirmregistration",method=RequestMethod.GET)
	public ModelAndView confirmRegistration(String numeroCasuale,String email){
		
		Utente utente = dati.getUtenti().get(email);
		if(utente == null)
			return new ModelAndView("confirmregistration","noUser","Utente non presente nel sistema!");
		else if(utente.getConfermato() == true)
			return new ModelAndView("confirmregistration","alreadyConfirmed","Regitrazione già confermata!");
		if(utente.getNumeroCasuale().equals(numeroCasuale)) {
			dati.modificaUtente(utente.getIdUtente(), utente.getMail(), utente.getNickname(), utente.getPassword(), utente.getDataRegistrazione(), true, "-1");
			
		}
		return new ModelAndView("confirmregistration");
		
	}
	
}
