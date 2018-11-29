package com.sample.demo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.Clock;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RegistrationController {

	private String state;
	private String scope;

	@RequestMapping("/authorize")
	public RedirectView authorize(@RequestParam("scope") String scope) {
		
		this.scope = scope;
		// Crea uno state casuale e lo codifica in Base64
		state = Base64.encodeBase64URLSafeString(Clock.systemUTC().instant().toString().getBytes());

		// Crea l'url alla quale reindirizzare il browser
		String url = "https://gluu-server.us-east1-b.c.test1-212904.internal/oxauth/restv1/authorize?"
				+ "response_type=code&" + "client_id=@!5F6A.FD87.8CA7.3E66!0001!7D5C.026A!0008!ECDB.5A2D.75BC.EA55&"
				+ "redirect_uri=https://localhost:8443/callback&" + "state=" + state + "&" + "scope=" + scope;

		/*
		 * Access token con implicit grant String url =
		 * "https://pc192.homenet.telecomitalia.it/oxauth/restv1/authorize?" +
		 * "response_type=token&" +
		 * "client_id=@!0D50.F6F6.6A69.6A60!0001!34E2.412B!0008!629E.2882.D258.1AE8&" +
		 * "redirect_uri=https://localhost:8443/callback&" + "scope=email&"+
		 * "nonce=pippooppip";
		 */

		// (DEBUG) Stampa la url su console
		System.out.println("URL: " + url);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(url);

		return redirectView;
	}

	@RequestMapping("/callback")
	public RedirectView callback(@RequestParam("code") String auth,
			@RequestParam(value = "state", required = false, defaultValue = "default") String st)
			throws GeneralSecurityException {

		// (DEBUG) Stampa su console i parametri ottenuti
		System.out.println("Authorization code: " + auth);
		System.out.println("State: " + st);

		// Controlla che lo stato corrisponda
		if (!(st.equals(state))) {
			System.err.println("Il parametro State non corrisponde con quello generato");
			System.err.println("Generato: " + state + "\nOttenuto: " + st);
			System.exit(0);
		}

		// Reindirizza il browser alla nuova pagina per la richiesta del token
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://localhost:8443/requestToken?code=" + auth + "&state=" + st);

		return redirectView;
	}

	@RequestMapping("/requestToken")
	public RedirectView register(@RequestParam("code") String auth,
			@RequestParam(value = "state", required = false, defaultValue = "default") String st) {

		/*
		 * 
		 * JsonRequest json = new
		 * JsonRequest("@!0D50.F6F6.6A69.6A60!0001!34E2.412B!0008!629E.2882.D258.1AE8",
		 * "client-secret-12345", "https://localhost:8443/callback", auth,
		 * "authorization_code");
		 *
		 * ObjectMapper objectMapper = new ObjectMapper();
		 *
		 * String body = objectMapper.writeValueAsString(json);
		 *
		 */

		// Prepara la redirect_uri
		URL redirect_uri = null;
		try {
			redirect_uri = new URL("https://localhost:8443/callback");
		} catch (MalformedURLException e) {
			System.err.println("Errore nella creazione della URL");
			e.printStackTrace();
			System.exit(0);
		}

		RestTemplate restTemplate = new RestTemplate();

		/*
		 * RequestEntity<String> request = RequestEntity .post(new
		 * URI("https://pc192.homenet.telecomitalia.it/oxauth/restv1/token"))
		 * .header("Content-Type", "application/x-www-form-urlencoded") .body(body);
		 */

		// Codifica le credenziali per l'authorization header
		byte[] eC = Base64.encodeBase64(
				"@!5F6A.FD87.8CA7.3E66!0001!7D5C.026A!0008!ECDB.5A2D.75BC.EA55:client-secret-12345".getBytes());
		String encodedCredential = new String(eC);

		// Prepara gli header per la POST
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("Authorization", "Basic " + encodedCredential);
		/*
		 * headers.add("Pragma", "no-cache"); headers.add("Cache-Control", "no-store");
		 */

		// Prepara il body per la POST
		String body = "grant_type=authorization_code" + "&code=" + auth + "&redirect_uri=" + redirect_uri
				+ "&client_id=@!5F6A.FD87.8CA7.3E66!0001!7D5C.026A!0008!ECDB.5A2D.75BC.EA55"
				+ "&client_secret=client-secret-12345"
				+ "&state=" + st
				+ "&scope=" + scope;
		
		// (DEBUG) Stampa il body su console
		System.out.println("Body: " + body);

		// Crea la richiesta
		RequestEntity<String> request = null;
		try {
			request = new RequestEntity<String>(body, headers, HttpMethod.POST,
					new URI("https://gluu-server.us-east1-b.c.test1-212904.internal/oxauth/restv1/token"));
		} catch (URISyntaxException e) {
			System.err.println("Errore nella creazione della URL");
			e.printStackTrace();
			System.exit(0);
		}

		// (DEBUG) Stampa la richiesta su console
		System.out.println("Request:\n" + request.getBody().toString());

		// Scambia la POST con le credenziali per il JSON
		ResponseEntity<Json> response = restTemplate.exchange(request, Json.class);
		
		if(!VerifyIdToken.isValid(response.getBody().getId())) {
			System.err.println("L'ID Token non è valido");
			System.exit(0);
		}

		// (DEBUG) Stampa la response su console
		System.out.println("Response:\n" + response.getBody().toString());
		
		String resp_uri = Base64.encodeBase64URLSafeString("https://192.168.1.12:8443/resource".getBytes());
		
		// Reindirizza il browser sulla nuova pagina dove verranno ottenute le
		// informazioni sull'utente
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://soap-server.com:8443/check?token=" + response.getBody().getAt() + "&redirect_uri=" + resp_uri);

		return redirectView;
		
	}

}
