package dati;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import hibernate.Inserzione;
import hibernate.Prodotto;

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
			if(!inserzione.getDataInizio().equals("")){
				//Inserzione pi� vecchia di 30 giorni
				if((new Date().getTime()/86400000-sdf.parse(inserzione.getDataInizio()).getTime()/86400000) > 30){
					errors.rejectValue("dataInizio", "invalidDate.InserzioneForm.dataInizio", 
							"inserzione troppo vecchia");
				}
				//Inserzione nel futuro
				if((new Date().getTime()/86400000-sdf.parse(inserzione.getDataInizio()).getTime()/86400000) < 0){
					errors.rejectValue("dataInizio", "invalidDate.InserzioneForm.dataInizio", 
							"Tempo inconsistente");
				}
				if(sdf.parse(inserzione.getDataInizio()).getTime() >= sdf.parse(inserzione.getDataFine()).getTime())
					errors.rejectValue("dataInizio", "invalidDate.InserzioneForm.dataInizio", 
							"Data di inizio maggiore della data di Fine");
				
				Prodotto prodotto = dati.getProdotti().get(inserzione.getCodiceBarre());
				int count = 0;
				if(prodotto != null){
					for(Inserzione i : (Set<Inserzione>)prodotto.getInserziones()){
						// varianza del prezzo troppo bassa
						if(i.getUtente().getMail().equals(principal.getName()) && Math.abs(i.getPrezzo() - inserzione.getPrezzo()) < 0.10 
								&& Math.abs(sdf.parse(inserzione.getDataInizio()).getTime()/86400000-i.getDataInizio().getTime()/86400000) <= 30){
							errors.rejectValue("prezzo", "invalidPrezzo.InserzioneForm.prezzo", 
									"� gia presente una tua inserzione con un prezzo simile");
							break;
						}
						// data identica
						if(i.getUtente().getMail().equals(principal.getName()) &&
								i.getDataInizio().equals(sdf.parse(inserzione.getDataInizio()))&&
								(int)i.getSupermercato().getLatitudine()==(int)inserzione.getLat()&&
								(int)i.getSupermercato().getLongitudine()==(int)inserzione.getLng()){
							errors.rejectValue("dataInizio", "invalidDate.InserzioneForm.dataInizio", 
									"� gia presente una tua inserzione con la stessa data");
							break;
						}
						
						//troppe insezioni in un mese
						if(Math.abs(sdf.parse(inserzione.getDataInizio()).getTime()/86400000-i.getDataInizio().getTime()/86400000) <= 30 
								&& i.getUtente().getMail().equals(principal.getName()) 
								&& inserzione.getSupermercato().equals(i.getSupermercato().getNome())){
							count++;
						}
						if(count > 2){
							errors.rejectValue("dataInizio", "invalidDate.InserzioneForm.dataInizio", 
									"troppe inserzioni in un certo periodo di tempo");
						}
					}
				}
			}else{
				errors.rejectValue("dataInizio", "invalidDate.InserzioneForm.dataInizio", 
						"data di inizio inesistente");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}

}
