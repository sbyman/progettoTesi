package com.sample.demo;

import java.io.IOException;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class NativeResponseController {

	private Map<String, String> responseContent;
	private SAMLAssertionResponse assertionResponse = new SAMLAssertionResponse();
	private User user;

	private String decodePOSTFormat(byte[] samlResponse) throws IOException {
		byte[] base64 = Base64.decodeBase64(samlResponse);
		return new String(base64);
	}

	public Map<String, String> unbuildResponse(String response, boolean check) {
		// (DEBUG) Stampa la response su console
		System.out.println(response);
		String decodedResponse = "";
		try {
			// Decodifica la response
			decodedResponse = decodePOSTFormat(response.getBytes());

			// (DEBUG) Stampa la response decodificata a schermo
			System.out.println(decodedResponse);

		} catch (IOException e) {
			System.err.println("Errore nella decodifica della richiesta");
			e.printStackTrace();
			System.exit(1);

		}
		// Controlla il tipo di response ricevuto
		try {
			if (check == true) {
				// Legge la response
				responseContent = new SAMLResponse().unbuildResponse(decodedResponse, user = new User(),
						assertionResponse);
			} else {
				// Legge l'artifact response
				responseContent = new SAMLArtifactResponse().unbuildResponse(decodedResponse, user = new User(),
						assertionResponse);
			}
		} catch (MarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseContent;

	}

	@RequestMapping(value = "/native/response", method = RequestMethod.POST)
	@ResponseBody
	public RedirectView response(@RequestParam(value = "SAMLResponse") String response) {

		responseContent = unbuildResponse(response, true);

		stampMap(responseContent);

		RedirectView redirectView;

		if (user.getSamlNameId() != null) {
			redirectView = new RedirectView();
			redirectView.setUrl("myapp://callback?id=" + user.getSamlNameId() + "&user=" + user.getUser());

		} else {
			redirectView = new RedirectView();
			redirectView.setUrl("myapp://callback?error=fail");
		}

		return redirectView;
	}

	@RequestMapping(value = "/native/response", method = RequestMethod.GET)
	@ResponseBody
	public RedirectView artifactResponse(@RequestParam(value = "SAMLResponse") String artifactResponse) {

		responseContent = unbuildResponse(artifactResponse, false);

		stampMap(responseContent);

		RedirectView redirectView;

		if (user.getSamlNameId() != null) {
			redirectView = new RedirectView();
			redirectView.setUrl("myapp://callback?id=" + user.getSamlNameId() + "&user=" + user.getUser());

		} else {
			redirectView = new RedirectView();
			redirectView.setUrl("myapp://callback?error=fail");
		}

		return redirectView;

	}

	private void stampMap(Map<String, String> map) {

		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "/" + entry.getValue());
		}
	}

}
