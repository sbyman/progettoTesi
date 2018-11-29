import java.net.URI;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import javax.net.ssl.HttpsURLConnection;

public class TokenRequest{

  private final String authCode;
  private final String redirectUri = "myapp://callback";
  private final String grantType = "authorization_code";
  private final String clientId =
                "@!5F6A.FD87.8CA7.3E66!0001!7D5C.026A!0008!8C34.D3DC.E59E.B127";

  public TokenRequest(String authCode){
    this.authCode = authCode;
  }

  public String generateRequstBody(){
      URI uri = null;
      try{
        uri = new URI(redirectUri);
      } catch (URISyntaxException e){
        e.printStackTrace();
      }
      return "grant_type=" + grantType
            + "&code=" + authCode
            + "&client_id=" + clientId
            + "&state=" + state
            + "&redirect_uri=" + redirectUri;
  }

  public void sendTokenRequest(String body){
    try{
      String tokenEndpoint =
                "https://gluu-server.us-east1-b.c.test1-212904.internal"
                + "/oxauth/restv1/token";
      URL urlTokenEndpoint = new URL(tokenEndpoint);
      HttpsURLConnection connection =
                      (HttpsURLConnection) urlTokenEndpoint.openConnection();

      //add request header
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type",
                                        "application/x-www-form-urlencoded");

      // Send post request
      connection.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
      wr.writeBytes(body);
      wr.flush();
      wr.close();

      int responseCode = connection.getResponseCode();
      System.out.println("\nSending 'POST' request to URL:\n" + tokenEndpoint);
      System.out.println("Post parameters:\n" + body);
      System.out.println("Response Code: " + responseCode);

      InputStreamReader stream = new InputStreamReader(connection.getInputStream());

      BufferedReader in = new BufferedReader(stream);
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      //print result
      System.out.println(response.toString());
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}
