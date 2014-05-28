package ai.server.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dati.Dati;
import dati.Registration;

@Controller
public class AndroidRegisterController {

	@Autowired
	private RegistrationValidation registrationValidation;
	
	public void setRegistrationValidation(RegistrationValidation registrationValidation) {
		this.registrationValidation = registrationValidation;
	}
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati) {
		this.dati = dati;
	}
	
	
	@RequestMapping(value="/android/register", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray processAndroidRegistration(@Valid Registration registration, BindingResult result, HttpServletRequest request) {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		JSONArray response = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		
		System.out.println("calling android register");
		
		registrationValidation.validate(registration, result);
		String numerocasuale = java.util.UUID.randomUUID().toString();
		Map<String, String> errorsMap = new HashMap<String, String>(); 
		
		if(result.hasErrors()){
			
			jsonObj.put("registration_result", false);
			
			for(FieldError fieldError : result.getFieldErrors())
				errorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
			jsonObj.put("errors", errorsMap);
			response.add(jsonObj);
			return response;
			
		}
		try{
			dati.inserisciUtente(registration.getEmail(), registration.getUserName(), registration.getPassword(), new Date(), numerocasuale);
		}catch(Exception e){
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			jsonObj.put("status", false);
			errorsMap.put("database",  e.getMessage());
			jsonObj.put("errors", errorsMap);
			response.add(jsonObj);
			return response;
		}
		
		Mail mail = (Mail) context.getBean("mail");

		String [] temp = request.getRequestURL().toString().split("/");
		String url = temp[0]+"//"+temp[1]+temp[2]+"/"+temp[3]+"/";
		mail.sendMail("brunano21@gmail.com", registration.getEmail(), "Registration Confirmation", "Click the link above to confirm your registration\n\n\n"+"<a href='"+url+"confirmregistration?numeroCasuale="+numerocasuale+"&email="+registration.getEmail()+"' />");
		jsonObj.put("registration_result", true);
		response.add(jsonObj);
		return response;
	}
}
