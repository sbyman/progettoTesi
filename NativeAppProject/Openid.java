import java.net.URI;
import java.net.URISyntaxException;

public class Openid{
  private final String clientId = "@!5F6A.FD87.8CA7.3E66!0001!7D5C.026A!0008!8C34.D3DC.E59E.B127";
  private final String authEndpoint = "https://gluu-server.us-east1-b.c.test1-212904.internal/oxauth/restv1/authorize";
  private final String tokenEndpoint = "https://gluu-server.us-east1-b.c.test1-212904.internal/oxauth/restv1/token";
  private final String redirectUri = "myapp://callback";
  private final String scope = "openid+email+uma_protection";
  private final String responseType = "code";
  private String state;

  public Openid(String state){
    this.state = state;
  }

  public String getClientId(){
    return clientId;
  }

  public String getAuthEndpoint(){
    return authEndpoint;
  }

  public String getTokenEndpoint(){
    return tokenEndpoint;
  }

  public String getRedirectUri(){
    return redirectUri;
  }

  public String getScope(){
    return scope;
  }

  public String getResponseType(){
    return responseType;
  }

  public String getState(){
    return state;
  }

  public String getCompleteUrl(){
    URI uri = null;
    try{
      uri = new URI(redirectUri);
    } catch (URISyntaxException e){
      e.printStackTrace();
    }
    return authEndpoint + "?client_id=" + clientId + "&redirect_uri=" + uri + "&response_type=" + responseType + "&scope=" + scope + "&state=" + state;
  }

}
