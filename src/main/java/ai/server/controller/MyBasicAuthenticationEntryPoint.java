package ai.server.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {


	public void CustomBasicAuthenticationEntryPoint(String realmName) {
		setRealmName(realmName);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, 	
			AuthenticationException authException) throws IOException, ServletException {
		System.out.println("sono passato dalla commence di MyBasicAuthentication");
		System.out.println(getRealmName());
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter writer = response.getWriter();
		writer.println("HTTP Status " + HttpServletResponse.SC_UNAUTHORIZED + " - " + authException.getMessage());
	}
}


