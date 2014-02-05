package dati;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Set;

import hibernate.Inserzione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component(value="InserzioneFormValidator")
public class InserzioneValidation {
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati = dati;
	}
	
	
	
	public void validate(Object target, Errors errors,Principal principal){
		
		InserzioneForm inserzione = (InserzioneForm)target;
		
		if(inserzione.getDescrizione().length()<10){
			errors.rejectValue("descrizione", 
					"lengthOfDescrizione.InserzioneForm.descrizione", 
					"Descrizione troppo corta, deve essere almeno di 10 caratteri");
		}
		
		if(Long.toString(inserzione.getCodiceBarre()).length()!=13){
			errors.rejectValue("codiceBarre", 
					"lengthOfCodiceBarre.InserzioneForm.codiceBarre", 
					"Il codice a barre deve avere 13 cifre");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			for(Inserzione i : (Set<Inserzione>) dati.getUtenti().get(principal.getName()).getInserziones() ){
				if(i.getProdotto().getCodiceBarre()==inserzione.getCodiceBarre()&&
						i.getDataInizio().equals(sdf.parse(inserzione.getDataInizio()))&&
						i.getSupermercato().getLatitudine().intValue()==(int)inserzione.getLat()&&
						i.getSupermercato().getLongitudine().intValue()==(int)inserzione.getLng()){
					errors.rejectValue("dataInizio", "invalidDate.InserzioneForm.dataInizio", 
							"� gia presente una tua inserzione con la stessa data");
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
