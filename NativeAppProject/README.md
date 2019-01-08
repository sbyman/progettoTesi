# Guida all'avvio del progetto

**NOTA**: La procedura seguente è valida per OS Ubuntu 18.04. Questo comporta:
   - l'utilizzo di Java 11 anziché Java 8 per la compilazione del progetto.
   - la realizzazione del custom handler potrebbe variare su distribuzioni differenti dello stesso OS.

1. Creare un handler per le richieste all'applicazione:
   1. Inserire `x-scheme-handler/myapp=myapp.desktop` in `/usr/share/applications/default.list`
   2. Creare un desktop file contenente:
      ```
      [Desktop Entry]
      Type=Application
      Encoding=UTF-8
      Name=MyNativeApp
      Comment=A native application
      Exec=/home/NativeAppProject/myapp.sh %U
      Icon=/home/NativeAppProject/icon/icon.png
      Terminal=true
      MimeType=x-scheme-handler/myapp;
      ```
      Dove **Exec** e **Icon** devono indicare la cartella per il file `myapp.sh` e per il file `icon.png` rispettivamente.
      Salvare il file in `/usr/share/applications/` con nome **myapp.desktop**
   3. Eseguire da terminale i comandi:
      ```
      $ xdg-mime default myapp.desktop x-scheme-handler/myapp
      $ xdg-mime query default x-scheme-handler/myapp
      $ update-desktop-database .local/share/applications/myapp.desktop
      $ update-desktop-database .config
      ```
   4. Riavviare la macchina.
   
2. Verificare gli indirizzi per **Authorization Endpoint** e **Token Endpoint** nei file `Openid.java` e `Oauth.java` 
3. Verificarela l'indirizzo del **Service Provider** in `Saml.java`
   - Modificare l'indirizzo IP in `Issuer` con il proprio nei file **NativeRequestController** e **NativeArtifactController**
4. Avviare il progetto tramite l'applicazione MyNativeApp. Nel caso si voglia usufruire di SAML 2.0 per l'autenticazione, avviare prima l'SP con Eclipse.
