package ai.server.controller;

import hibernate.Inserzione;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.node.ObjectNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dati.Dati;

@Controller
public class AndroidAbitudiniAcquistoController {
	
	@Autowired
	private ServletContext context;

	public void setServletContext(ServletContext context) {
		this.context = context;
	}
	
	@Autowired
	private Dati dati;

	public void setDati(Dati dati){
		this.dati=dati;
	}
	
	@RequestMapping(value="/android/abitudini", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray processAbitudini(HttpServletRequest request, Principal principal) {
		JSONArray response = null;
		ObjectNode result = null;
		
		switch(request.getParameter("cmd")) {
			case "lista_descrizioni":
				System.out.println("lista_descrizioni");
				response = new JSONArray();
				JSONObject elementoAbitudine = null;
				
				List abitudiniList = dati.getAbitudiniSpesa(principal.getName());
				
				for(Object obj : abitudiniList) {
					elementoAbitudine = new JSONObject();
					Object[] fields = (Object[]) obj;
					if(fields[0] != null)
						elementoAbitudine.put("descrizione", fields[0].toString());
					if(fields[1] != null)
						elementoAbitudine.put("contatore", Integer.parseInt(fields[1].toString()));
					
					response.add(elementoAbitudine);
					
					System.out.println("Descrizione: " + elementoAbitudine.get("descrizione"));
					System.out.println("Contatore: " + elementoAbitudine.get("contatore"));
				}
				System.out.println("JSONARRAY " + response.size());
				break;
				
			case "ottieni_suggerimenti_acquisti":
				System.out.println("ottieni_suggerimenti_acquisti");
				System.out.println(request.getParameter("latitudine"));
				System.out.println(request.getParameter("longitudine"));
				System.out.println(request.getParameter("array_descrizioni"));
				response = new JSONArray();
				JSONObject suggerimentoJsonObj;
				Map<Integer, Inserzione> inserzioniMapTmp1 = null;
				String imageDataString1 = null;

				List abitudiniList1 = dati.getSuggerimentiAbitudini(principal.getName(), request.getParameter("latitudine"), request.getParameter("longitudine"), request.getParameter("array_descrizioni"));
				
				inserzioniMapTmp1 = dati.getInserzioni();
				
				for(Object obj : abitudiniList1) {
					elementoAbitudine = new JSONObject();
					Object[] fields = (Object[]) obj;
					if(fields[0] != null)
						elementoAbitudine.put("id_inserzione", Integer.parseInt(fields[0].toString()));
					if(fields[1] != null)
						elementoAbitudine.put("descrizione", fields[1].toString());
					if(fields[2] != null)
						elementoAbitudine.put("supermercato", inserzioniMapTmp1.get(Integer.parseInt(fields[0].toString())).getSupermercato().getNome() + ", " + inserzioniMapTmp1.get(Integer.parseInt(fields[0].toString())).getSupermercato().getIndirizzo() + ", " + inserzioniMapTmp1.get(Integer.parseInt(fields[0].toString())).getSupermercato().getComune());
					if(fields[3] != null)
						elementoAbitudine.put("data_fine", fields[3].toString());
					if(fields[4] != null)
						elementoAbitudine.put("prezzo", fields[4].toString());
					if(fields[5] != null) {
						imageDataString1 = null;
						try {
							
							BufferedImage originalImage = ImageIO.read(new File(context.getRealPath("/") + inserzioniMapTmp1.get(Integer.parseInt(fields[0].toString())).getFoto()));
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(originalImage, "jpg", baos);
							baos.flush();
							byte[] imageInByte = baos.toByteArray();
							imageDataString1 = new String(Base64.encodeBase64(imageInByte));
							baos.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						elementoAbitudine.put("foto", imageDataString1);
					}
					response.add(elementoAbitudine);
				}
				System.out.println("JSONARRAY " + response.size());
				break;
				
			default:
				System.out.println("OPS!! - uknown command " +  request.getParameter("cmd"));
				break;
			}
		
		return response;
	}
	
}
