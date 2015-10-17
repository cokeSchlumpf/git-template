package ws;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Stateless
@Path("coin")
public class CoinService {
  @GET
  public String flip() {
    return Math.random() > 0.5 ? "Heads" : "Tail";
  }
}
