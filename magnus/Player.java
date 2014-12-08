package magnus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Player {

  public static void main(String[] args) throws Exception{
    if (args.length < 4) {
      System.out.println("Usage: java Player GAME_ID TEAM_NUMBER TEAM_SECRET MY_COLOR(0 or 1)");
      System.exit(1);
    }
    int GAME_ID = Integer.parseInt(args[0]);
    int TEAM_NUMBER = Integer.parseInt(args[1]);
    int TEAM_SECRET = Integer.parseInt(args[2]);
    int MY_COLOR = Integer.parseInt(args[3]);

    if (MY_COLOR == 0) {int ENEMY_COLOR = 1;}
    else {int ENEMY_COLOR = 0;}

    System.out.printf("ID: %d, NUM: %d, SECRET: %d\n", GAME_ID, TEAM_NUMBER, TEAM_SECRET);
    //sendGet("http://www.bencarle.com/chess/display/" + GAME_ID);
    Bitboard b = new Bitboard(MY_COLOR);
    b.printPieceBitboards();
    System.out.println("Occupied Board\n");
    b.printBitboard(b.OCCUPIED_SQUARES);
    System.out.println("\nEnemy Board\n");
    b.printBitboard(b.ENEMY_SQUARES);
  }

  static void sendGet(String url) throws Exception {
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    con.setRequestProperty("User-Agent", "Mozilla/5.0");

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    System.out.println(response.toString());

  }
}