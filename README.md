# Protocolli di autorizzazione e autenticazione per web application e API: un'analisi comparativa

I progetti all'interno di questa repository sono stati creati con il semplice scopo di soddisfare
le specifiche dei vari protocolli che si vanno ad analizzare. Lo scopo ultimo è stato quello di
verificare i pro e i contro di ogni protocollo di autorizzazione e autenticazione.

I vari progetto sono stati realizzati in JavaEE con l'ausilio dell'IDE [Eclipse](https://www.eclipse.org/). Tranne per il progetto NativeAppProject che è stato realizzato in Java senza l'ausilio di IDE particolari.

All'interno dei vari progetti è presente un file che spiega come eseguirli.

Di seguito una legenda sui progetti contenuti nelle cartelle:
 - **SAMLProject**: Contiene il progetto SAML in cui si implementano i vari tipi di SSO iniziati da SP come specificato da [SAML Technical Overview](https://www.oasis-open.org/committees/download.php/27819/sstc-saml-tech-overview-2.0-cd-02.pdf). Si implementa inoltre la chiamata a API sfruttando [WSS-SAML-Token-Profile-V1.1.1](http://docs.oasis-open.org/wss-m/wss/v1.1.1/wss-SAMLTokenProfile-v1.1.1.html)
 - **OpenIDProject**: Contiene il progetto OpenID Connect in cui si implementa l'SSO iniziato da SP e la chiamata a API
 - **OAuthProject**: Contiene il progetto OAuth 2.0 in cui si implementa la sola autorizzazione.
 - **NativeAppProject**: Contiene il progetto di una native application in grado di eseguire SSO, con SAML e OpenID Connect, o solo autorizzazione con OAuth 2.0.
 - **RestApi**: Contiene il progetto per una API RESTlike in grado di ricevere un Access Token, verificarne la validità e restituire le informazioni ottenute.
 - **SoapServer**: Contiene il progetto per un Web Service in grado di ricevere una richiesta assieme a un token di sicurezza, verificare la validità del token e restituire determinate informazioni in base ai dati letti. 
