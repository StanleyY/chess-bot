package magnus;

class Bitboard {

  public long[][] board = null;
  public int color = -1;
  public long king_moves = 0L;
  public long knight_moves = 0L;

  // Useful utility boards
  private long FULL_BOARD = 0xFFFFFFFFFFFFFFFFL;

  public long ENEMY_SQUARES = 0L;
  public long EMPTY_SQUARES = 0L;
  public long ENEMY_AND_EMPTY_SQUARES = 0L;
  public long OCCUPIED_SQUARES = 0L;

  public Bitboard(int player_color){
    this.color = player_color;
    this.board = generateNewBoard();
    if (color == Player.WHITE) generateUtilBoards(Player.BLACK);
    else generateUtilBoards(Player.WHITE);
  }

  public Bitboard(Bitboard old_bb, int piece_color, int piece, int old_pos, int new_pos){
    this.color = old_bb.color;
    this.board = makeMove(old_bb.board, piece_color, piece, old_pos, new_pos);
    if (color == Player.WHITE) generateUtilBoards(Player.BLACK);
    else generateUtilBoards(Player.WHITE);
  }

  public Bitboard(Bitboard old_bb, int piece_color, int piece, int new_piece, int old_pos, int new_pos){
    this.color = old_bb.color;
    this.board = makeMovePromotion(old_bb.board, piece_color, piece, new_piece, old_pos, new_pos);
    if (color == Player.WHITE) generateUtilBoards(Player.BLACK);
    else generateUtilBoards(Player.WHITE);
  }


  private long[][] generateNewBoard(){
    // 0 = King, 1 = Queen, 2 = Rook, 3 = Bishop, 4 = Knight, 5 = Pawn
    long[][] output = new long[2][6];

    output[Player.WHITE][Player.KING] = 0x0000000000000010L;
    output[Player.WHITE][Player.QUEEN] = 0x0000000000000008L;
    output[Player.WHITE][Player.ROOK] = 0x0000000000000081L;
    output[Player.WHITE][Player.BISHOP] = 0x0000000000000024L;
    output[Player.WHITE][Player.KNIGHT] = 0x0000000000000042L;
    output[Player.WHITE][Player.PAWN] = 0x000000000000FF00L;

    output[Player.BLACK][Player.KING] = 0x1000000000000000L;
    output[Player.BLACK][Player.QUEEN] = 0x0800000000000000L;
    output[Player.BLACK][Player.ROOK] = 0x8100000000000000L;
    output[Player.BLACK][Player.BISHOP] = 0x2400000000000000L;
    output[Player.BLACK][Player.KNIGHT] = 0x4200000000000000L;
    output[Player.BLACK][Player.PAWN] = 0x00FF000000000000L;

    return output;
  }


  private long[][] makeMove(long[][] bb, int piece_color, int piece, int old_pos, int new_pos){
    long[][] output = new long[2][6];
    // Cloning bitboards
    for (int color = 0; color < 2; color++){
      for(int p = 0; p < 6; p++){
        output[color][p] = bb[color][p];
      }
    }
    long ending_pos = (1L << new_pos );
    output[piece_color][piece] = (output[piece_color][piece] ^ (1L << old_pos )) | ending_pos;
    piece_color = piece_color ^ 1; // Convert to other color.
    // Checking for captures
    for(int p = 0; p < 6; p++){
      if ((ending_pos & output[piece_color][p]) > 0) {
        output[piece_color][p] = output[piece_color][p] ^ ending_pos;
      }
    }
    return output;
  }

  private long[][] makeMovePromotion(long[][] bb, int piece_color, int piece, int new_piece, int old_pos, int new_pos){
    long[][] output = new long[2][6];
    // Cloning bitboards
    for (int color = 0; color < 2; color++){
      for(int p = 0; p < 6; p++){
        output[color][p] = bb[color][p];
      }
    }
    long ending_pos = (1L << new_pos );
    output[piece_color][piece] = (output[piece_color][piece] ^ (1L << old_pos ));
    output[piece_color][new_piece] = output[piece_color][new_piece] | ending_pos;
    piece_color = piece_color ^ 1; // Convert to other color.
    // Checking for captures
    for(int p = 0; p < 6; p++){
      if ((ending_pos & output[piece_color][p]) > 0) {
        output[piece_color][p] = output[piece_color][p] ^ ending_pos;
      }
    }
    return output;
  }

  public void generateUtilBoards(int enemy_color){
    long temp = 0L;
    for (int color = 0; color < 2; color++){
      for(int piece = 0; piece < 6; piece++){
        temp = temp | this.board[color][piece];
      }
    }
    this.OCCUPIED_SQUARES = temp;
    this.EMPTY_SQUARES = ~temp;
    temp = 0L;

    for (int piece = 0; piece < 6; piece++){
      temp = temp | board[enemy_color][piece];
    }
    this.ENEMY_SQUARES = temp;

    this.ENEMY_AND_EMPTY_SQUARES = this.EMPTY_SQUARES | temp;
  }


  public void printBitboard(long board){
    String line = "";
    int rank = 8;
    for(int i = 7; i > -1; i--){
      for(int j = 0; j < 8; j++) {
        if( ((1L << ((i * 8) + j)) & board) == 0){
          line = line + "-";
        }
        else{
          line = line + "X";
        }
      }
      System.out.println(rank + line);
      rank--;
      line = "";
    }
    System.out.println(" ABCDEFGH\n");
  }

  public void printPieceBitboards(){
    String[] lookup = new String[] {"King", "Queen", "Rook", "Bishop", "Knight", "Pawn"};
    System.out.println("\n--White Pieces--\n");
    int color = 0;
    for(int piece = 0; piece < 6; piece++){
      System.out.println("Piece: " + lookup[piece]);
      printBitboard(this.board[color][piece]);
      System.out.println("");
    }
    System.out.println("--Black Pieces--\n");
    color = 1;
    for(int piece = 0; piece < 6; piece++){
      System.out.println("Piece: " + lookup[piece]);
      printBitboard(this.board[color][piece]);
      System.out.println("");
    }
  }

}