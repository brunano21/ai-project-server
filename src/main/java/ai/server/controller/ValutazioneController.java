package ai.server.controller;

import hibernate.Categoria;
import hibernate.Inserzione;
import hibernate.Sottocategoria;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dati.Dati;

@Controller
public class ValutazioneController {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	private Map<Integer,Inserzione> inserzioni;
	
	
	@RequestMapping(value = "/valutazione")
	public ModelAndView showValutazione(){

		
		return new ModelAndView("valutazione");
	}
	
	
	@RequestMapping(value="/valutazione/getIds",method = RequestMethod.GET,consumes="application/json")
	public @ResponseBody Set<Integer> getIds(String lat,String lng){
		
		if(inserzioni == null){
			inserzioni = new HashMap<Integer,Inserzione>();	
			
			for(Map.Entry<Integer, Inserzione> ii : dati.getInserzioni().entrySet()){
				if((int)((ii.getValue().getDataFine().getTime() - new Date().getTime()) / 86400000) > 1 &&
						distFrom(Float.parseFloat(lat), Float.parseFloat(lng), ii.getValue().getSupermercato().getLatitudine(), ii.getValue().getSupermercato().getLongitudine()) < 10)
					inserzioni.put(ii.getKey(), ii.getValue());	
			}
		}
		
		Set<Integer> ids = new HashSet<Integer>();
		for(Map.Entry<Integer, Inserzione> ii : inserzioni.entrySet()){
			ids.add(ii.getKey());
		}
		
		return ids;
		
	}
	
	
	
	@RequestMapping(value = "/valutazione/pictures/{imageId}")
	@ResponseBody
	public byte[] getImage(@PathVariable long imageId)  {
		
		File image = new File(inserzioni.get(imageId).getFoto());
		
		try {
			return FileUtils.readFileToByteArray(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
	    double earthRadius = 6371;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;


	    return new Float(dist).floatValue();
	 }

}
