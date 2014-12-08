package magnus;

class Bitboard {

  public static long[] masks = null;
  public static long[][] board = null;

  public Bitboard(){
    masks = generateMasks();
    board = generateNewBoard();
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
    System.out.println("My Pieces");
    for(int color = 0; color < 2; color++){
      for(int piece = 0; piece < 6; piece++){
        System.out.println("Piece: " + lookup[piece]);
        printBitboard(this.board[color][piece]);
        System.out.println("");
      }
      System.out.println("Opponent Pieces");
    }
  }

  public void printWholeBitboard(){
    String[] lookup = new String[] {"King", "Queen", "Rook", "Bishop", "Knight", "Pawn"};
    long whole_board = 0L;
    for (int color = 0; color < 2; color++){
      for(int piece = 0; piece < 6; piece++){
        whole_board = whole_board ^ this.board[color][piece];
      }
    }
    printBitboard(whole_board);
  }

}