# Guida all'avvio del progetto

1. Importare il progetto su **Eclipse** come `Maven` -> `Existing Maven Project`.
2. Cliccare con il tasto destro sul progetto e scegliere -> `Run` -> `Run Configurations...`
   1. Sotto `Spring Boot App` selezionare il progetto `oauth-project`
   2. Scegliere la tab `Arguments`
   3. Inserire i comandi per importare i *cacerts* di java.
      - In generale il primo comando contiene la destionazione del cacerts e il secondo la password per accedervi.
      - In macchina Ubuntu con openjdk 8:
        ```
        -Djavax.net.ssl.trustStore=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts 
        -Djavax.net.ssl.trustStorePassword=changeit
        ```
3. Sostituire l'indirizzo IP in `src/main/java/resources/xml/metadata.xml` con il proprio indirizzo IP
4. Sostituire l'indirizzo IP nelle classi `ArtifactController`, `PostController` e `RedirectController` con il proprio indirizzo IP
5. Per sfruttare le API sostituire l'indirizzo `https://soap-server.com:8443` con quello dell'API
6. Avviare il progetto come `Spring Boot App` e andare alla pagina https://localhost:8443 del browser.
