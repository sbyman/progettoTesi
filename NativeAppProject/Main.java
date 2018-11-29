import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.System;

class Main {
  public static void main (String[] args){
    if(args.length != 0){
      System.out.println("Welcome! Your arg is equal to: " + args[0]);
      Response response = new Response(args[0]);
      String code = response.parseResponse();
      if(!response.isSaml()){
        TokenRequest request = new TokenRequest(code);
        String body = request.generateRequstBody();
        System.out.println("Here it is, your new whole new body:\n" + body);
        request.sendTokenRequest(body);
      }
      else if (response.isSaml()){
        System.out.println("User successfully authenticated!");
      }
      while(true){}
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Insert the name of the protocol do you want to use\n(oauth, openid, saml):\n");
    String s = "";
    try{
      s = reader.readLine();
    } catch (IOException e){
      e.printStackTrace();
    }
    System.out.println("Hello World! This is your arg: " + s);
    Browser browser = null;
    switch(s){
      case ("oauth"):
        System.out.println("Your arg is oauth");
        Oauth oauth = new Oauth("12345");
        System.out.println("Your uri is: " + oauth.getCompleteUrl());
        System.out.println("Let's proceed to run this on a new browser tab");
        browser = new Browser(oauth.getCompleteUrl());
        browser.execute();
        break;
      case ("openid"):
        System.out.println("Your arg is openid");
        Openid openid = new Openid("12345");
        System.out.println("Your url is: " + openid.getCompleteUrl());
        System.out.println("Let's proceed to run this on a new browser tab");
        browser = new Browser(openid.getCompleteUrl());
        browser.execute();
        break;
      case ("saml"):
        System.out.println("Your arg is saml");
        reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Insert the type of binding do you waat to use\n(redirect, post):\n");
        s = "";
        try{
          s = reader.readLine();
        } catch (IOException e){
          e.printStackTrace();
        }
        Saml saml = new Saml(s);
        browser = new Browser(saml.getCompleteUrl());
        browser.execute();
        break;
      default:
        System.err.println("Arg not recognized");
        System.exit(1);
    }
  }
}
