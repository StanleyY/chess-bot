package magnus;

class Bitboard {

  public long[][] board = null;
  public int color = -1;
  public int value;

  // Previous move information.
  public int last_piece;
  public int old_pos;
  public int new_pos;
  public int promo = -1;
  public boolean capture = false;

  // Useful utility boards
  public long ENEMY_SQUARES = 0L;
  public long EMPTY_SQUARES = 0L;
  public long ENEMY_AND_EMPTY_SQUARES = 0L;
  public long OCCUPIED_SQUARES = 0L;

  // Castling
  public boolean HAS_WHITE_KING_NOT_MOVED = true;
  public boolean HAS_WHITE_KING_ROOK_NOT_MOVED = true;
  public boolean HAS_WHITE_QUEEN_ROOK_NOT_MOVED = true;

  public boolean HAS_BLACK_KING_NOT_MOVED = true;
  public boolean HAS_BLACK_KING_ROOK_NOT_MOVED = true;
  public boolean HAS_BLACK_QUEEN_ROOK_NOT_MOVED = true;

  public static final int[] knight_pos_value = {-30, -20, 10, 20, 20, 10, -20, -30,
                                                -20, -10, 15, 25, 25, 15, -10, -20,
                                                -10,  20, 40, 45, 45, 40,  20, -10,
                                                  0,  30, 45, 50, 50, 45,  30,   0,
                                                  0,  30, 45, 50, 50, 45,  30,   0,
                                                -10,  20, 40, 45, 45, 40,  20, -10,
                                                -20, -10, 15, 25, 25, 15, -10, -20,
                                                -30, -20, 10, 20, 20, 10, -20, -30,};

  public static final int[] bishop_pos_value = {0, -20, 0, 0, 0, 0, -20, 0,
                                                20, 25, 40, 50, 50, 40, 25, 20,
                                                20, 30, 50, 50, 50, 50, 30, 20,
                                                30, 30, 50, 50, 50, 50, 30, 30,
                                                30, 30, 50, 50, 50, 50, 30, 30,
                                                20, 30, 50, 50, 50, 50, 30, 20,
                                                20, 25, 40, 50, 50, 40, 25, 20,
                                                0, -20, 0, 0, 0, 0, -20, 0};

  public static final int[] rook_pos_value = {0, 0, 0, 0, 0, 0, 0, 0,
                                               10, 10, 10, 10, 10, 10, 10, 10,
                                               0, 0, 0, 5, 5, 0, 0, 0,
                                               0, 0, 0, 10, 10, 0, 0, 0,
                                               0, 0, 0, 10, 10, 0, 0, 0,
                                               0, 0, 0, 5, 5, 0, 0, 0,
                                               10, 10, 10, 10, 10, 10, 10, 10,
                                               0, 0, 0, 0, 0, 0, 0, 0};

  public static final int[] queen_pos_value = {0, 0, 0, 0, 0, 0, 0, 0,
                                               0, 0, 0, 0, 0, 0, 0, 0,
                                               0, 0, 0, 5, 5, 0, 0, 0,
                                               0, 0, 0, 10, 10, 0, 0, 0,
                                               0, 0, 0, 10, 10, 0, 0, 0,
                                               0, 0, 0, 5, 5, 0, 0, 0,
                                               0, 0, 0, 0, 0, 0, 0, 0,
                                               0, 0, 0, 0, 0, 0, 0, 0};

  public static final int[][] king_pos_value = {{-50, -50, -50, -50, -50, -50, -50, -50,
                                                -50, -50, -50, -50, -50, -50, -50, -50,
                                                -10, -10, -10, -10, -10, -10, -10, -10,
                                                -10, -10, -20, -30, -30, -20, -10, -10,
                                                -10, -10, -20, -30, -30, -20, -10, -10,
                                                -10, -10, -10, -10, -10, -10, -10, -10,
                                                -10, -10, -10, -10, -10, -10, -10, -10,
                                                   0, 0, 40, 0, 0, 0, 50, 0},

                                               {0, 0, 40, 0, 0, 0, 50, 0,
                                                -10, -10, -10, -10, -10, -10, -10, -10,
                                                -10, -10, -20, -30, -30, -20, -10, -10,
                                                -10, -10, -20, -30, -30, -20, -10, -10,
                                                -10, -10, -10, -10, -10, -10, -10, -10,
                                                -10, -10, -10, -10, -10, -10, -10, -10,
                                                -50, -50, -50, -50, -50, -50, -50, -50,
                                                -50, -50, -50, -50, -50, -50, -50, -50}};

  public Bitboard(int player_color){
    this.color = player_color;
    this.board = generateNewBoard();
    generateUtilBoards();
    //this.value = eval();
  }

  public Bitboard(Bitboard bb){
    this.color = bb.color;
    this.HAS_WHITE_KING_NOT_MOVED = bb.HAS_WHITE_KING_NOT_MOVED;
    this.HAS_WHITE_KING_ROOK_NOT_MOVED = bb.HAS_WHITE_KING_ROOK_NOT_MOVED;
    this.HAS_WHITE_QUEEN_ROOK_NOT_MOVED = bb.HAS_WHITE_QUEEN_ROOK_NOT_MOVED;

    this.HAS_BLACK_KING_NOT_MOVED = bb.HAS_BLACK_KING_NOT_MOVED;
    this.HAS_BLACK_KING_ROOK_NOT_MOVED = bb.HAS_BLACK_KING_ROOK_NOT_MOVED;
    this.HAS_BLACK_QUEEN_ROOK_NOT_MOVED = bb.HAS_BLACK_QUEEN_ROOK_NOT_MOVED;
    this.board = cloneBoard(bb.board);
    generateUtilBoards();
    //this.value = eval();
  }

  public Bitboard(Bitboard old_bb, int piece_color, int piece, int old_pos, int new_pos){
    this.color = old_bb.color;
    this.HAS_WHITE_KING_NOT_MOVED = old_bb.HAS_WHITE_KING_NOT_MOVED;
    this.HAS_WHITE_KING_ROOK_NOT_MOVED = old_bb.HAS_WHITE_KING_ROOK_NOT_MOVED;
    this.HAS_WHITE_QUEEN_ROOK_NOT_MOVED = old_bb.HAS_WHITE_QUEEN_ROOK_NOT_MOVED;

    this.HAS_BLACK_KING_NOT_MOVED = old_bb.HAS_BLACK_KING_NOT_MOVED;
    this.HAS_BLACK_KING_ROOK_NOT_MOVED = old_bb.HAS_BLACK_KING_ROOK_NOT_MOVED;
    this.HAS_BLACK_QUEEN_ROOK_NOT_MOVED = old_bb.HAS_BLACK_QUEEN_ROOK_NOT_MOVED;

    this.board = makeMove(old_bb.board, piece_color, piece, old_pos, new_pos);
    this.last_piece = piece;
    this.old_pos = old_pos;
    this.new_pos = new_pos;

    generateUtilBoards();
    //this.value = eval();
  }

  public Bitboard(Bitboard old_bb, int piece_color, int piece, int new_piece, int old_pos, int new_pos){
    this.color = old_bb.color;
    this.HAS_WHITE_KING_NOT_MOVED = old_bb.HAS_WHITE_KING_NOT_MOVED;
    this.HAS_WHITE_KING_ROOK_NOT_MOVED = old_bb.HAS_WHITE_KING_ROOK_NOT_MOVED;
    this.HAS_WHITE_QUEEN_ROOK_NOT_MOVED = old_bb.HAS_WHITE_QUEEN_ROOK_NOT_MOVED;

    this.HAS_BLACK_KING_NOT_MOVED = old_bb.HAS_BLACK_KING_NOT_MOVED;
    this.HAS_BLACK_KING_ROOK_NOT_MOVED = old_bb.HAS_BLACK_KING_ROOK_NOT_MOVED;
    this.HAS_BLACK_QUEEN_ROOK_NOT_MOVED = old_bb.HAS_BLACK_QUEEN_ROOK_NOT_MOVED;

    this.board = makeMovePromotion(old_bb.board, piece_color, piece, new_piece, old_pos, new_pos);
    this.last_piece = piece;
    this.old_pos = old_pos;
    this.new_pos = new_pos;
    this.promo = new_piece;

    generateUtilBoards();
    //this.value = eval();
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

  public Bitboard makePolyMove(int old_pos, int new_pos){
    long temp = 1L << old_pos;
    int piece = -1;
    for(int p = 0; p < 6; p++){
      if((this.board[this.color][p] & temp) != 0) {
        piece = p;
        break;
      }
    }
    if(piece == Player.KING && (old_pos % 8) == 4){
      if(new_pos == (0 + 63 * this.color) || new_pos == (7 + 63 * this.color)){
        if(old_pos / 8 > 0){ // Black Castle
          if(new_pos == 56) return this.castle(58);
          else return this.castle(62);
        }
        else{
          if(new_pos == 0) return this.castle(2);
          else return this.castle(6);
        }
      }
    }
    return new Bitboard(this, this.color, piece, old_pos, new_pos);
  }

  private long[][] makeMove(long[][] bb, int piece_color, int piece, int old_pos, int new_pos){
    long[][] output = new long[2][6];
    // Cloning bitboards
    for (int color = 0; color < 2; color++){
      for(int p = 0; p < 6; p++){
        output[color][p] = bb[color][p];
      }
    }
    long ending_pos = (1L << new_pos);
    // Castling updates.
    if(piece_color == Player.WHITE){
      if(this.HAS_WHITE_KING_NOT_MOVED && (piece == Player.KING || piece == Player.BISHOP)){
        if(piece == Player.KING) this.HAS_WHITE_KING_NOT_MOVED = false;
        else {
          if(old_pos == 0L) HAS_WHITE_QUEEN_ROOK_NOT_MOVED = false;
          else if(old_pos ==  1L << 7) HAS_WHITE_KING_ROOK_NOT_MOVED = false;
        }
      }
      else{
        if(this.HAS_BLACK_KING_NOT_MOVED && (piece == Player.KING || piece == Player.BISHOP)){
          if(piece == Player.KING) this.HAS_BLACK_KING_NOT_MOVED = false;
          else{
            if(old_pos == 1L << 56) HAS_BLACK_QUEEN_ROOK_NOT_MOVED = false;
            else if(old_pos ==  1L << 63) HAS_BLACK_KING_ROOK_NOT_MOVED = false;
          }
        }
      }
    }
    output[piece_color][piece] = (output[piece_color][piece] ^ (1L << old_pos )) | ending_pos;
    piece_color = piece_color ^ 1; // Convert to other color.
    // Checking for captures
    for(int p = 0; p < 6; p++){
      if ((ending_pos & output[piece_color][p]) != 0) {
        output[piece_color][p] = output[piece_color][p] ^ ending_pos;
        this.capture = true;
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
      if ((ending_pos & output[piece_color][p]) != 0) {
        output[piece_color][p] = output[piece_color][p] ^ ending_pos;
        this.capture = true;
      }
    }
    return output;
  }

  public Bitboard castle(int new_pos){
    Bitboard new_bb = new Bitboard(this); //Cloning
    new_bb.makeCastle(new_pos);
    new_bb.last_piece = 0;
    new_bb.new_pos = new_pos;
    return new_bb;
  }

  public void makeCastle(int new_pos){
    if(this.board[this.color][0] < (1L << new_pos)){
      //Kingside castle
      if(this.color == Player.WHITE){
        this.old_pos = 4;
        this.HAS_WHITE_KING_NOT_MOVED = false;
        this.board[Player.WHITE][0] = 0x0000000000000040L;
        this.board[Player.WHITE][2] = this.board[Player.WHITE][2] ^ 0x00000000000000A0L;
      }
      else{
        this.old_pos = 60;
        this.HAS_BLACK_KING_NOT_MOVED = false;
        this.board[Player.BLACK][0] = 0x4000000000000000L;
        this.board[Player.BLACK][2] = this.board[Player.BLACK][2] ^ 0xA000000000000000L;
      }
    }
    else{
      //Queenside
      if(this.color == Player.WHITE){
        this.old_pos = 4;
        this.HAS_WHITE_KING_NOT_MOVED = false;
        this.board[Player.WHITE][0] = 0x0000000000000004L;
        this.board[Player.WHITE][2] = this.board[Player.WHITE][2] ^ 0x0000000000000009L;
      }
      else{
        this.old_pos = 60;
        this.HAS_BLACK_KING_NOT_MOVED = false;
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

  // TODO: Make actual evaluation function.
  public int eval(int color){
    return materialValue(color) + queen_value(color) + knight_value(color) + rook_value(color) + bishop_value(color) + king_value(color);
  }

  private int materialValue(int color){
    int total = 30000 * (Bitmap.count_set_bits(this.board[color][0]) - Bitmap.count_set_bits(this.board[color ^ 1][0]))
              + 900 * (Bitmap.count_set_bits(this.board[color][1]) - Bitmap.count_set_bits(this.board[color ^ 1][1]))
              + 700 * (Bitmap.count_set_bits(this.board[color][2]) - Bitmap.count_set_bits(this.board[color ^ 1][2]))
              + 480 * (Bitmap.count_set_bits(this.board[color][3]) - Bitmap.count_set_bits(this.board[color ^ 1][3]))
              + 450 * (Bitmap.count_set_bits(this.board[color][4]) - Bitmap.count_set_bits(this.board[color ^ 1][4]))
              + 250 * (Bitmap.count_set_bits(this.board[color][5]) - Bitmap.count_set_bits(this.board[color ^ 1][5]));
    return total;
  }

  private int king_value(int color){
    int my_king_pos = Long.numberOfTrailingZeros(this.board[color][0]);
    int val = 0;
    if (my_king_pos < 64) val += king_pos_value[color][my_king_pos];

    int enemy_king_pos = Long.numberOfTrailingZeros(this.board[color ^ 1][0]);
    if (enemy_king_pos < 64) val -= king_pos_value[color ^ 1][enemy_king_pos];
    return val;
  }

  private int queen_value(int color){
    long my_queen_bitboard = this.board[color][1];
    long enemy_queen_bitboard = this.board[color ^ 1][1];
    int val = 0;
    while(my_queen_bitboard != 0){
      int pos = Long.numberOfTrailingZeros(my_queen_bitboard);
      my_queen_bitboard = my_queen_bitboard ^ (1L << (pos) );
      val += queen_pos_value[pos];
    }
    while(enemy_queen_bitboard != 0){
      int pos = Long.numberOfTrailingZeros(enemy_queen_bitboard);
      enemy_queen_bitboard = enemy_queen_bitboard ^ (1L << (pos) );
      val -= queen_pos_value[pos];
    }
    return val;
  }

  private int rook_value(int color){
    long my_rook_bitboard = this.board[color][2];
    long enemy_rook_bitboard = this.board[color ^ 1][2];
    int val = 0;
    while(my_rook_bitboard != 0){
      int pos = Long.numberOfTrailingZeros(my_rook_bitboard);
      my_rook_bitboard = my_rook_bitboard ^ (1L << (pos) );
      val += rook_pos_value[pos];
    }
    while(enemy_rook_bitboard != 0){
      int pos = Long.numberOfTrailingZeros(enemy_rook_bitboard);
      enemy_rook_bitboard = enemy_rook_bitboard ^ (1L << (pos) );
      val -= rook_pos_value[pos];
    }
    return val;
  }

  private int bishop_value(int color){
    long my_bishop_bitboard = this.board[color][3];
    long enemy_bishop_bitboard = this.board[color ^ 1][3];
    int val = 0;
    while(my_bishop_bitboard != 0){
      int pos = Long.numberOfTrailingZeros(my_bishop_bitboard);
      my_bishop_bitboard = my_bishop_bitboard ^ (1L << (pos) );
      val += bishop_pos_value[pos];
    }
    while(enemy_bishop_bitboard != 0){
      int pos = Long.numberOfTrailingZeros(enemy_bishop_bitboard);
      enemy_bishop_bitboard = enemy_bishop_bitboard ^ (1L << (pos) );
      val -= bishop_pos_value[pos];
    }
    return val;
  }

  private int knight_value(int color){
    long my_knight_bitboard = this.board[color][4];
    long enemy_knight_bitboard = this.board[color ^ 1][4];
    int val = 0;
    while(my_knight_bitboard != 0){
      int pos = Long.numberOfTrailingZeros(my_knight_bitboard);
      my_knight_bitboard = my_knight_bitboard ^ (1L << (pos) );
      val += knight_pos_value[pos];
    }
    while(enemy_knight_bitboard != 0){
      int pos = Long.numberOfTrailingZeros(enemy_knight_bitboard);
      enemy_knight_bitboard = enemy_knight_bitboard ^ (1L << (pos) );
      val -= knight_pos_value[pos];
    }
    return val;
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