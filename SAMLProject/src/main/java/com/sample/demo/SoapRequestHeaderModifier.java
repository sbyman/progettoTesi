package com.sample.demo;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;

import com.sample.demo.wsse.ObjectFactory;
import com.sample.demo.wsse.SecurityHeaderType;

public class SoapRequestHeaderModifier implements WebServiceMessageCallback {


    private SecurityHeaderType security;

	public SoapRequestHeaderModifier(SecurityHeaderType security) {
        this.security = security;
	}


	public void doWithMessage(WebServiceMessage message) throws IOException {
        try {
            SoapMessage soapMessage = (SoapMessage)message;
            SoapHeader header = soapMessage.getSoapHeader();
            
            JAXBContext context = JAXBContext.newInstance(SecurityHeaderType.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            ObjectFactory objectWsse = new ObjectFactory();
            
            JAXBElement<SecurityHeaderType> securityObject = objectWsse.createSecurity(security);
            
            marshaller.marshal(securityObject, header.getResult());
            
            //Stampiamo il messaggio prima che parta
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapMessage.writeTo(out);
            String strMsg = new String(out.toByteArray());
            System.out.println(strMsg);
            
        } catch (JAXBException e) {
			System.err.println("Errore nel marshalling del SAML Token");
			e.printStackTrace();
			System.exit(1);
        }
    }

}
