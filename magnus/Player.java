package magnus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Player {

  public static final int WHITE = 0;
  public static final int BLACK = 1;

  public static final int KING = 0;
  public static final int QUEEN = 1;
  public static final int ROOK = 2;
  public static final int BISHOP = 3;
  public static final int KNIGHT = 4;
  public static final int PAWN = 5;

  public static final char[] FILE_NAME = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
  public static final String[] PIECE_NAME = new String[]{"K", "Q", "R", "B", "N", "P"};

  public static void main(String[] args) throws Exception{
    if (args.length < 4) {
      System.out.println("Usage: java Player GAME_ID TEAM_NUMBER TEAM_SECRET MY_COLOR(0 or 1)");
      System.exit(1);
    }
    int GAME_ID = Integer.parseInt(args[0]);
    int TEAM_NUMBER = Integer.parseInt(args[1]);
    String TEAM_SECRET = args[2];
    int MY_COLOR = Integer.parseInt(args[3]);
    int ENEMY_COLOR = -1;

    if (MY_COLOR == WHITE) {ENEMY_COLOR = BLACK;}
    else {ENEMY_COLOR = WHITE;}

    Bitmap bitmap = new Bitmap();

    System.out.printf("ID: %d, NUM: %d, SECRET: %s\n", GAME_ID, TEAM_NUMBER, TEAM_SECRET);
    //sendGet("http://www.bencarle.com/chess/display/" + GAME_ID);
    Bitboard b = new Bitboard(MY_COLOR);

    b.printPieceBitboards();
    System.out.println("Occupied Board\n");
    b.printBitboard(b.OCCUPIED_SQUARES);
    System.out.println("\nEnemy Board\n");
    b.printBitboard(b.ENEMY_SQUARES);
    System.out.println("\nEmpty Board\n");
    b.printBitboard(b.EMPTY_SQUARES);

    Move[] move_list = bitmap.generateMoves(b);

    int index = 0;
    System.out.println("List of Moves:");
    while(move_list[index] != null){
      System.out.printf("Piece: %s, Original: %s, New: %s\n", PIECE_NAME[move_list[index].piece], translateMove(move_list[index].old_pos), translateMove(move_list[index].new_pos));
      //b.printBitboard(move_list[index].board.OCCUPIED_SQUARES);
      index++;
    }

    for (int i = 0; i < 64; i++){
      System.out.printf("Position: %d, %s\n", i, translateMove(i));
      b.printBitboard(bitmap.deg45_board[i]);
    }
  }


  public static String translateMove(int move){
    return  FILE_NAME[move % 8] + "" + ((move / 8) + 1);
  }


  static String sendGet(String url) throws Exception {
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

    return response.toString();

  }
}