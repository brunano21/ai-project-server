package ai.server.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;
import dati.Registration;

@Controller
public class ListaSpesaController {
	
	
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@RequestMapping(value="/todolist", method = RequestMethod.GET)
	public String showForm(Map model){
		//Registration registration = new Registration();
		//model.put("registration",registration);	
		
		return "todolist";
	}
	
	@RequestMapping(value="/todolistSaved", method = RequestMethod.POST)
	public String saveToDoList(@Valid Registration registration,BindingResult result,HttpServletRequest request){
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		
		System.out.println(request.getParameter("param"));
		System.out.println(request.getParameter("value"));
		
		/*registrationValidation.validate(registration, result);
		String numerocasuale = java.util.UUID.randomUUID().toString();
		if(result.hasErrors()){
			return new ModelAndView("register");
		}
		try{
			dati.inserisciUtente(registration.getEmail(), registration.getUserName(), registration.getPassword(),new Date(),numerocasuale);
		}catch(Exception e){
			
			return new ModelAndView("register","error",e.toString());
		}
		
		Mail mail = (Mail)context.getBean("mail");

		String [] temp = request.getRequestURL().toString().split("/");
		String url = temp[0]+"//"+temp[1]+temp[2]+"/"+temp[3]+"/";
		
		mail.sendMail("giorgio.ciacchella@gmail.com", registration.getEmail(), "Registration Confirmation", "Click the link above to confirm your registration\n\n\n"+"<a href='"+url+"confirmregistration?numeroCasuale="+numerocasuale+"&email="+registration.getEmail()+"' />");
		return new ModelAndView("registersuccess","registration",registration);
		*/
		return "todolist";
	}
}
