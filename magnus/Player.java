package magnus;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


class Player {

  public static final int WHITE = 0;
  public static final int BLACK = 1;

  public static final int KING = 0;
  public static final int QUEEN = 1;
  public static final int ROOK = 2;
  public static final int BISHOP = 3;
  public static final int KNIGHT = 4;
  public static final int PAWN = 5;

  public static final int RANK_1 = 0;
  public static final int RANK_2 = 1;
  public static final int RANK_3 = 2;
  public static final int RANK_4 = 3;
  public static final int RANK_5 = 4;
  public static final int RANK_6 = 5;
  public static final int RANK_7 = 6;
  public static final int RANK_8 = 7;

  public static final ArrayList<Character> FILE_NAME = new ArrayList<Character>(8);
  public static final ArrayList<Character> PIECE_NAME = new ArrayList<Character>(6);

  public static String POLL_URL = "";
  public static String MOVE_URL = "";

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
    System.out.printf("ID: %d, NUM: %d, SECRET: %s\n", GAME_ID, TEAM_NUMBER, TEAM_SECRET);
    POLL_URL = String.format("http://www.bencarle.com/chess/poll/%d/%d/%s", GAME_ID, TEAM_NUMBER, TEAM_SECRET);
    MOVE_URL = String.format("http://www.bencarle.com/chess/move/%d/%d/%s/", GAME_ID, TEAM_NUMBER, TEAM_SECRET);

    for(char c: "abcdefgh".toCharArray()){
      FILE_NAME.add(c);
    }
    for(char c: "KQRBNP".toCharArray()){
      PIECE_NAME.add(c);
    }

    if (MY_COLOR == WHITE) {ENEMY_COLOR = BLACK;}
    else {ENEMY_COLOR = WHITE;}

    JSONParser parser = new JSONParser();
    Bitmap bitmap = new Bitmap();
    Bitboard b = new Bitboard(MY_COLOR);

    try {
      boolean ready = false;
      String lastmove = "";
      String response = "";
      JSONObject json = null;
      int move_num = 0;
      while(true){
        System.out.printf("\n\n#######Move Number: %d#######\n\n", move_num);
        System.out.println("POLLING ATTEMPT");
        while(!ready){
          Thread.sleep(5000);
          response = sendGet(POLL_URL);
          json = (JSONObject) parser.parse(response);
          ready = (boolean) json.get("ready");
        }
        System.out.printf("\nPOLLING RESPONSE %s\n\n", response);
        lastmove = (String) json.get("lastmove");
        if(lastmove.length() > 4){
          char[] temp = lastmove.toCharArray();
          int piece = PIECE_NAME.indexOf(temp[0]);
          int old_pos =  FILE_NAME.indexOf(temp[1]) + ((Character.getNumericValue(temp[2]) - 1) * 8);
          int new_pos = FILE_NAME.indexOf(temp[3]) + ((Character.getNumericValue(temp[4]) - 1) * 8);
          System.out.println("OPPONENT'S MOVE WAS: " + lastmove);

          if(lastmove.length() < 6){
            b = new Bitboard(b, ENEMY_COLOR, piece, old_pos, new_pos);
          }
          else{
            b = new Bitboard(b, ENEMY_COLOR, piece, PIECE_NAME.indexOf(temp[5]), old_pos, new_pos);
          }
          b.printBitboard(b.OCCUPIED_SQUARES);
          b.printBitboard(b.ENEMY_SQUARES);
        }

        Stack<Move> move_list = bitmap.generateMoves(b);
        b = sendMove(move_list);
        System.out.printf("\n#########AFTER MOVE NUM: %d#########\n\n", move_num);
        b.printBitboard(b.OCCUPIED_SQUARES);
        b.printBitboard(b.ENEMY_SQUARES);
        ready = false;
        move_num++;
      }
    }catch(ParseException e){
      System.out.println(e);
    }
    /*
    int index = 0;
    System.out.println("List of Moves:");
    while(move_list[index] != null){
      System.out.printf("Piece: %s, Original: %s, New: %s\n", PIECE_NAME.get(move_list[index].piece), translateMove(move_list[index].old_pos), translateMove(move_list[index].new_pos));
      //b.printBitboard(move_list[index].board.OCCUPIED_SQUARES);
      index++;
    }*/
/*
    for (int i = 0; i < 64; i++){
      System.out.printf("Position: %d, %s\n", i, translateMove(i));
      b.printBitboard(bitmap.deg45_board[i] | bitmap.deg135_board[i] | bitmap.deg225_board[i] | bitmap.deg315_board[i]);
    }*/
  }


  public static String translateMove(int move){
    return  FILE_NAME.get(move % 8) + "" + ((move / 8) + 1);
  }

  static String sendGet(String url) {
    try{
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setRequestProperty("User-Agent", "Mozilla/5.0");

      int responseCode = con.getResponseCode();
      System.out.println("\nSending 'GET' request to URL : " + url);

      BufferedReader in = new BufferedReader(
              new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      return response.toString();
    } catch (Exception e){
      System.out.println(e);
    }
    return null;
  }

  public static Bitboard sendMove(Stack<Move> move_list){
    Random rand = new Random();
    int randomNum = rand.nextInt((move_list.size() - 3) + 1);
    while(randomNum-- > 0) move_list.pop();
    Move move = move_list.pop();
    String URL = MOVE_URL + PIECE_NAME.get(move.piece) + translateMove(move.old_pos) + translateMove(move.new_pos);
    if(move.promo > 0){
      URL = URL + PIECE_NAME.get(move.promo);
    }
    String response = sendGet(URL);
    while(response.indexOf("false") > -1){
      System.out.println("INVALID MOVE: " + URL);
      System.out.println("ERROR RESPONSE: " + response);
      move = move_list.pop();
      URL = MOVE_URL + PIECE_NAME.get(move.piece) + translateMove(move.old_pos) + translateMove(move.new_pos);
      if(move.promo > 0){
        URL = URL + PIECE_NAME.get(move.promo);
      }
      response = sendGet(URL);
    }
    return move.board;
  }
}