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

import java.util.Date;

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
  public static int MY_COLOR = -1;
  public static int ENEMY_COLOR = -1;

  static Bitmap bitmap = new Bitmap();
  static Polyglot poly = new Polyglot();

  public static void main(String[] args) throws Exception{
    if (args.length < 4) {
      System.out.println("Usage: java Player GAME_ID TEAM_NUMBER TEAM_SECRET MY_COLOR(0 or 1)");
      System.exit(1);
    }
    int GAME_ID = Integer.parseInt(args[0]);
    int TEAM_NUMBER = Integer.parseInt(args[1]);
    String TEAM_SECRET = args[2];
    MY_COLOR = Integer.parseInt(args[3]);
    ENEMY_COLOR = MY_COLOR ^ 1;

    System.out.printf("ID: %d, NUM: %d, SECRET: %s\n", GAME_ID, TEAM_NUMBER, TEAM_SECRET);
    POLL_URL = String.format("http://www.bencarle.com/chess/poll/%d/%d/%s", GAME_ID, TEAM_NUMBER, TEAM_SECRET);
    MOVE_URL = String.format("http://www.bencarle.com/chess/move/%d/%d/%s/", GAME_ID, TEAM_NUMBER, TEAM_SECRET);

    for(char c: "abcdefgh".toCharArray()){
      FILE_NAME.add(c);
    }
    for(char c: "KQRBNP".toCharArray()){
      PIECE_NAME.add(c);
    }

    JSONParser parser = new JSONParser();
    Bitboard b = new Bitboard(MY_COLOR);
    poly.read();
    try {
      boolean ready = false;
      String lastmove = "";
      String response = "";
      JSONObject json = null;
      int move_num = 0;
      while(true){
        System.out.printf("\n\n####### Move Number: %d #######\n\n", move_num);
        System.out.println("POLLING ATTEMPT");
        while(!ready){
          Thread.sleep(2500);
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
            if(piece == KING && Math.abs((new_pos % 8) - (old_pos % 8)) > 1){
              // Swap colors, castle, swap again.
              b.color = b.color ^ 1;
              b = b.castle(new_pos);
              b.color = b.color ^ 1;
            }
            else if(piece == PAWN && new_pos % 8 != old_pos % 8 && (b.EMPTY_SQUARES & (1L << new_pos)) != 0){
              // Swap colors, castle, swap again.
              b.color = b.color ^ 1;
              b = b.makeEnPassant(old_pos, new_pos);
              b.color = b.color ^ 1;
            }
            else{
              b = new Bitboard(b, ENEMY_COLOR, piece, old_pos, new_pos);
            }
          }
          else{
            b = new Bitboard(b, ENEMY_COLOR, piece, PIECE_NAME.indexOf(temp[5]), old_pos, new_pos);
          }
          b.printBitboard(b.OCCUPIED_SQUARES);
          b.printBitboard(b.ENEMY_SQUARES);
        }
        int[] poly_move = poly.search(b);
        boolean poly_success = false;
        if(move_num < 20 && poly_move != null){
          System.out.printf("Poly Move: %s, %s \n", translateMove(poly_move[0]), translateMove(poly_move[1]));
          b = b.makePolyMove(poly_move[0], poly_move[1]);
          poly_success = sendMove(b);
        }
        if(!poly_success){
          System.out.println("No move was found, Beginning Search");
          Stack<Bitboard> move_list = bitmap.generateMoves(b);
          Bitboard pv = search(move_list, bitmap.count_set_bits(b.OCCUPIED_SQUARES));
          //Move pv = move_list.pop();
          b = sendMove(pv, move_list);
        }
        System.out.printf("\n######### AFTER MOVE NUM: %d #########\n\n", move_num);
        System.out.println("MY COLORS IS: " + MY_COLOR);
        System.out.println("OCCUPIED_SQUARES");
        b.printBitboard(b.OCCUPIED_SQUARES);
        System.out.println("ENEMY_SQUARES");
        b.printBitboard(b.ENEMY_SQUARES);
        System.out.println("MY_SQUARES");
        b.printBitboard(~b.ENEMY_AND_EMPTY_SQUARES);
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

  public static Bitboard search(Stack<Bitboard> move_list, int number_of_pieces){
    Bitboard pv = null;
    Bitboard current_move;
    int best_value = -40000;
    int temp;
    int depth = 5;
    if (number_of_pieces < 15) depth = 7;
    if (number_of_pieces < 6) depth = 9;
    System.out.println("Start time: " + new java.util.Date());
    while(move_list.size() > 0){
      current_move = move_list.pop();
      //System.out.printf("!!!!!!!!!!!!!TRYING: Piece: %s, Original: %s, New: %s !!!!!!!!!! \n\n", PIECE_NAME.get(current_move.last_piece), translateMove(current_move.old_pos), translateMove(current_move.new_pos));
      temp = alphabeta(current_move, 5, best_value, 40000, false);
      //System.out.println("Temp" + temp);
      if(temp > best_value){
        //System.out.printf("BEST VALUE UPDATED: Piece: %s, Original: %s, New: %s, Score: %d \n\n", PIECE_NAME.get(current_move.last_piece), translateMove(current_move.old_pos), translateMove(current_move.new_pos), temp);
        pv = current_move;
        best_value = temp;
      }
    }
    System.out.println("End time: " + new java.util.Date() + "\n");
    System.out.println("Best Score: " + best_value);
    if (pv.color != MY_COLOR) {pv.color = pv.color ^ 1; pv.generateUtilBoards();}
    return pv;
  }

  public static int alphabeta(Bitboard node, int depth, int alpha, int beta, boolean max){
    if (depth == 0 || node.board[WHITE][0] == 0 || node.board[BLACK][0] == 0){
      //System.out.printf("TERMINAL MOVE: Color: %d Piece: %s, Original: %s to %s, Score: %d\n", node.color, PIECE_NAME.get(node.last_piece), translateMove(node.old_pos), translateMove(node.new_pos), node.eval(MY_COLOR));
      return node.eval(MY_COLOR);
    }
    int score;
    node.color = node.color ^ 1;
    node.generateUtilBoards();
    Stack<Bitboard> children = bitmap.generateMoves(node);
    Bitboard child;
    if(max){
      while(children.size() > 0){
        child = children.pop();
        score = alphabeta(child, depth - 1, alpha, beta, false);
        if(score > alpha) alpha = score;
        if(beta <= alpha) break;
      }
      return alpha;
    }
    else{
      while(children.size() > 0){
        child = children.pop();
        score = alphabeta(child, depth - 1, alpha, beta, true);
        if(score < beta) beta =  score;
        if(beta <= alpha) break;
      }
      return beta;
    }
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

  public static Bitboard sendMove(Bitboard pv, Stack<Bitboard> move_list){
    Bitboard move = pv;
    String URL = MOVE_URL + PIECE_NAME.get(move.last_piece) + translateMove(move.old_pos) + translateMove(move.new_pos);
    if(move.promo > 0){
      URL = URL + PIECE_NAME.get(move.promo);
    }
    String response = sendGet(URL);
    while(response.indexOf("false") > -1){
      System.out.println("INVALID MOVE: " + URL);
      System.out.println("ERROR RESPONSE: " + response);
      move = move_list.pop();
      URL = MOVE_URL + PIECE_NAME.get(move.last_piece) + translateMove(move.old_pos) + translateMove(move.new_pos);
      if(move.promo > 0){
        URL = URL + PIECE_NAME.get(move.promo);
      }
      response = sendGet(URL);
    }
    return move;
  }

  public static boolean sendMove(Bitboard move){
    String URL = MOVE_URL + PIECE_NAME.get(move.last_piece) + translateMove(move.old_pos) + translateMove(move.new_pos);
    if(move.promo > 0){
      URL = URL + PIECE_NAME.get(move.promo);
    }
    String response = sendGet(URL);
    return !(response.indexOf("false") > -1);
  }
}