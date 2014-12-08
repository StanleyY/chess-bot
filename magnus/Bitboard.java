package magnus;

class Bitboard {

  public static long[] masks = null;
  public static long[][] board = null;

  // Useful boards
  private static long FULL_BOARD = 0xFFFFFFFFFFFFFFFFL;

  public static long ENEMY_SQUARES = 0L;
  public static long EMPTY_SQUARES = 0L;
  public static long ENEMY_AND_EMPTY_SQUARES = 0L;
  public static long OCCUPIED_SQUARES = 0L;

  public Bitboard(int color){
    masks = generateMasks();
    board = generateNewBoard();
    if (color == 0) generateUtilBoards(1);
    else generateUtilBoards(0);
  }

  private long[] generateMasks(){
    long[] output = new long[64];
    for (int i = 0; i < output.length; i++){
      output[i] = 1L << i;
    }
    return output;
  }


  private long[][] generateNewBoard(){
    // 0 = King, 1 = Queen, 2 = Rook, 3 = Bishop, 4 = Knight, 5 = Pawn
    long[][] output = new long[2][6];

    output[0][0] = 0x0000000000000010L;
    output[0][1] = 0x0000000000000008L;
    output[0][2] = 0x0000000000000081L;
    output[0][3] = 0x0000000000000024L;
    output[0][4] = 0x0000000000000042L;
    output[0][5] = 0x000000000000FF00L;

    output[1][0] = 0x1000000000000000L;
    output[1][1] = 0x0800000000000000L;
    output[1][2] = 0x8100000000000000L;
    output[1][3] = 0x2400000000000000L;
    output[1][4] = 0x4200000000000000L;
    output[1][5] = 0x00FF000000000000L;

    return output;
  }


  private void generateUtilBoards(int enemy_color){
    long temp = 0L;
    for (int color = 0; color < 2; color++){
      for(int piece = 0; piece < 6; piece++){
        temp = temp | this.board[color][piece];
      }
    }
    this.OCCUPIED_SQUARES = temp;
    this.EMPTY_SQUARES = temp ^ this.FULL_BOARD;
    temp = 0L;

    for (int piece = 0; piece < 6; piece++){
      temp = temp | board[enemy_color][piece];
    }
    this.ENEMY_SQUARES = temp;

    this.ENEMY_AND_EMPTY_SQUARES = this.EMPTY_SQUARES | temp;
  }


  public void printBitboard(long board){
    String line = "";
    for(int i = 7; i > -1; i--){
      for(int j = 0; j < 8; j++) {
        if( ((1L << ((i * 8) + j)) & board) == 0){
          line = line + "-";
        }
        else{
          line = line + "X";
        }
      }
      System.out.println(line);
      line = "";
    }
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