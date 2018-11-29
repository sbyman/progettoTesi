package com.soap.server;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class SAMLAssertion {
		
	public Element convertAssertion(String assertionString) {
		
		System.out.println("SAMLResponse:\n" + assertionString);
		
		// Passa da stringa a documento per ottenere un Element
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = null;
		Element element = null;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new ByteArrayInputStream(assertionString.getBytes("UTF-8"))));
			element = doc.getDocumentElement();
			
			//Scrivo l'xml
			BufferedWriter writer = new BufferedWriter(new FileWriter("assertion.xml"));
			String stringResponse = XMLHelper.nodeToString(element);
			writer.write(stringResponse);
			writer.close();
			
		} catch (ParserConfigurationException e) {
			System.err.println("Errore nella configurazione del parser");
			e.printStackTrace();
			System.exit(1);
		} catch (SAXException | IOException e) {
			System.err.println("Errore nella creazione del documento");
			e.printStackTrace();
			System.exit(1);
		}
		
		return element;
		
	}
	
	public Element getAssertion(String location) {
		
		try {
			SSLContext sslContext = new SSLContextBuilder()
				      .loadTrustMaterial(null, (certificate, authType) -> true).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CloseableHttpClient httpclient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpGet httpget = new HttpGet(location);
		httpget.setHeader("Accept", "application/xml");
		String assertionString = null;

		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			StringWriter writer = new StringWriter();
			IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
			assertionString = writer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertAssertion(assertionString);
		
	}

	public Map<String, String> unbuildAssertion(Element element) throws MarshallingException {
		
		Map<String, String> data = new HashMap<String, String>();
		
		// Attributi dell'utente
		//otteniamo le informazioni che mi servono
		NodeList nodeList = element.getElementsByTagName("saml2:Attribute");
		for(int i = 0; nodeList.getLength() > i; i++) {
			System.out.println("In While");
			Node node = nodeList.item(i);
			NamedNodeMap namedNodeMap = node.getAttributes();
			Node attribute = namedNodeMap.item(0);
			String value = node.getTextContent();
			value = value.replaceAll("\\s","");
			System.out.println(attribute.getNodeValue() + "; " + value);
			data.put(attribute.getNodeValue(), value);
		}

		return data;
	}

}
