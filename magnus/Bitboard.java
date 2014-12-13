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

  // Castling
  public boolean HAS_WHITE_CASTLED = false;
  public boolean HAS_WHITE_KING_NOT_MOVED = true;
  public boolean HAS_WHITE_KING_ROOK_NOT_MOVED = true;
  public boolean HAS_WHITE_QUEEN_ROOK_NOT_MOVED = true;

  public boolean HAS_BLACK_CASTLED = false;
  public boolean HAS_BLACK_KING_NOT_MOVED = true;
  public boolean HAS_BLACK_KING_ROOK_NOT_MOVED = true;
  public boolean HAS_BLACK_QUEEN_ROOK_NOT_MOVED = true;


  public Bitboard(int player_color){
    this.color = player_color;
    this.board = generateNewBoard();
    generateUtilBoards();
  }

  public Bitboard(Bitboard bb){
    this.color = bb.color;
    this.board = cloneBoard(bb.board);
    generateUtilBoards();
  }

  public Bitboard(Bitboard old_bb, int piece_color, int piece, int old_pos, int new_pos){
    this.color = old_bb.color;
    this.board = makeMove(old_bb.board, piece_color, piece, old_pos, new_pos);
    generateUtilBoards();
  }

  public Bitboard(Bitboard old_bb, int piece_color, int piece, int new_piece, int old_pos, int new_pos){
    this.color = old_bb.color;
    this.board = makeMovePromotion(old_bb.board, piece_color, piece, new_piece, old_pos, new_pos);
    generateUtilBoards();
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

  private long[][] cloneBoard(long[][] original){
    long[][] output = new long[2][6];
    // Cloning bitboards
    for (int color = 0; color < 2; color++){
      for(int p = 0; p < 6; p++){
        output[color][p] = original[color][p];
      }
    }
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

  public Bitboard castle(int new_pos){
    Bitboard new_bb = new Bitboard(this); //Cloning
    new_bb.makeCastle(new_pos);
    return new_bb;
  }

  public void makeCastle(int new_pos){
    if(this.board[this.color][0] < (1L << new_pos)){
      //Kingside castle
      if(this.color == Player.WHITE){
        this.HAS_WHITE_CASTLED = true;
        this.board[Player.WHITE][0] = 0x0000000000000040L;
        this.board[Player.WHITE][2] = this.board[Player.WHITE][2] ^ 0x00000000000000A0L;
      }
      else{
        this.HAS_BLACK_CASTLED = true;
        this.board[Player.BLACK][0] = 0x4000000000000000L;
        this.board[Player.BLACK][2] = this.board[Player.BLACK][2] ^ 0xA000000000000000L;
      }
    }
    else{
      //Queenside
      if(this.color == Player.WHITE){
        this.HAS_WHITE_CASTLED = true;
        this.board[Player.WHITE][0] = 0x0000000000000004L;
        this.board[Player.WHITE][2] = this.board[Player.WHITE][2] ^ 0x0000000000000009L;
      }
      else{
        this.HAS_BLACK_CASTLED = true;
        this.board[Player.BLACK][0] = 0x0400000000000000L;
        this.board[Player.BLACK][2] = this.board[Player.BLACK][2] ^ 0x0900000000000000L;
      }
    }
    this.generateUtilBoards();
  }

  public void generateUtilBoards(){
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
      temp = temp | board[this.color ^ 1][piece];
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