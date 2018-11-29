import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;

public class Response{

  private final String response;
  HashMap<String, String> token = new HashMap<String, String>();
  private boolean isSaml;

  public Response(String response){
    this.response = response;
  }

  public String parseResponse(){
    String[] responseArray = response.split("\\?");
    String[] queryElements = responseArray[1].split("\\&");
    for(int i = 0; i < queryElements.length; i++){
      String[] elements = queryElements[i].split("\\=");
      token.put(elements[0], elements[1]);
    }
    if(token.containsKey("code")){
      System.out.println("Those are the elements contained in the query: ");
      System.out.println("Code: " + token.get("code") + "\nState: " + token.get("state"));
      this.isSaml = false;
      return token.get("code");
    }
    else if (token.containsKey("id")) {
      System.out.println("You have choosen SAML so those are the elements in response: ");
      System.out.println("Subject: " + token.get("id") + "\nUser id: " + token.get("user"));
      this.isSaml = true;
      return token.get("id");
    }
    else {
      System.err.println("Error: token not recognized!");
      System.exit(1);
    }
    return null;
  }

  public boolean isSaml(){
    return isSaml;
  }

}
