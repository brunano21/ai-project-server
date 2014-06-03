package ai.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dati.Dati;

@Controller
public class AndroidLoginController {

	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati) {
		this.dati = dati;
	}
	
	 @RequestMapping(value="/android/login", method = RequestMethod.POST)
	 @ResponseBody
	 public Boolean androidLoginSuccess(HttpServletRequest request, HttpServletResponse response) {
		 
		 System.out.println("sono entrato in login SUCCESS android");
		 
		 return new Boolean(true);
	 }
}
