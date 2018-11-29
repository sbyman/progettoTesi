import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

public class Browser{

  private final String url;

  public Browser(String url){
    this.url = url;
  }

  public void execute(){
    if(Desktop.isDesktopSupported()){
      Desktop desktop = Desktop.getDesktop();
      try{
        System.out.println("Let's open this desktop browser!!");
        desktop.browse(new URI(url));
      } catch (IOException | URISyntaxException e){
        e.printStackTrace();
      }
    }
    else{
      Runtime runtime = Runtime.getRuntime();
      try{
        System.out.println("Let's open this runtime browser!!");
        runtime.exec("xdg-open " + url);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
