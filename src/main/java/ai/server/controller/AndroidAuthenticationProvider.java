package ai.server.controller;

import hibernate.Utente;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import dati.Dati;

@Component 
public class AndroidAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati = dati;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		System.out.println(username);
		System.out.println(password);
		
		Utente utente = dati.getUtenti().get(username);
		if(utente != null) {
			if(!utente.getConfermato() || !utente.getPassword().equals(password))
				utente = null;
		} else {	
			System.out.println("User " + username + " not found");
			return null;
		}
		
		final List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        final UserDetails principal = new User(username, password, grantedAuths);
        final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
        return auth;
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		 return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
