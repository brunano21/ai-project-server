package ai.server.controller;

import hibernate.Utente;

import java.util.ArrayList;
import java.util.HashSet;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import dati.Dati;

@Component 
public class MyRESTAuthenticationProvider implements AuthenticationProvider  {
	
	@Autowired
	private Dati dati;
	
	public void setDati(Dati dati){
		this.dati=dati;
	}
	/*
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		Utente utente = dati.getUtenti().get(username);
		if(utente != null){
			if(!utente.getConfermato())
				utente = null;
		}
		if(utente == null)
			throw new UsernameNotFoundException("User " + username + " not found");
		
		HashSet<GrantedAuthority> aut = new HashSet<GrantedAuthority>();
		aut.add(new SimpleGrantedAuthority("ROLE_USER"));
		System.out.println("ANDROID loadUserByUsername");
		return new User(utente.getMail(), utente.getPassword(), true, true, false, false, aut);
	}
	*/
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("sono nella authenticate di MyRESTAuthenticationProvider");
		System.out.println(authentication.getName());
		System.out.println(authentication.getCredentials().toString());
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		if (dati.getUtenti().containsKey(email) && password.equals(dati.getUtenti().get(email).getPassword())) {
			List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
			Authentication auth = new UsernamePasswordAuthenticationToken(email, password, grantedAuths);
			return auth;
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		System.out.println("sono nella supports di MyRESTAuthenticationProvider");
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}



	
}
