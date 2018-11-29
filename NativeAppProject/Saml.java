
public class Saml{

  private final String spLocation = "https://localhost:8443/native/request";
  private final String desktopHandler = "myapp://callback";
  private String protocolBinding;
  private String destination;

  public Saml(String type){
    if(type.equals("redirect")){
      this.protocolBinding = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST";
      this.destination = "https://gluu-server.us-east1-b.c.test1-212904.internal/idp/profile/SAML2/Redirect/SSO";
    }
    else if (type.equals("post")){
      this.protocolBinding = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact";
      this.destination = "https://gluu-server.us-east1-b.c.test1-212904.internal/idp/profile/SAML2/POST/SSO";
    }
    else {
      System.err.println("Error: selected type doesn't corrispond :(");
      System.exit(1);
    }
  }

  public String getCompleteUrl(){
    return spLocation + "?desktopHandler=" + desktopHandler + "&protocolBinding=" + protocolBinding + "&destination=" + destination;
  }

}
