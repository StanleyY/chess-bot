package magnus;

class Bitmap{

  public long[] king_xray = null;
  public long[] knight_xray = null;
  public long[][] pawn_single_xray = null;
  public long[][] pawn_double_xray = null;
  public long[][] pawn_capture_xray = null;

  public long[] up_board = null;
  public long[] down_board = null;
  public long[] left_board = null;
  public long[] right_board = null;

  public Bitmap(){
    generateBitmaps();
  }

  private void generateBitmaps(){
    this.king_xray = generateKingBitmap();
    this.knight_xray = generateKnightBitmap();
    this.pawn_single_xray = generatePawnSingleBitmap();
    this.pawn_double_xray = generatePawnDoubleBitmap();
    this.pawn_capture_xray = generatePawnCaptureBitmap();
    this.right_board = generateRightBitmap();
    this.left_board = generateLeftBitmap();
    this.up_board = generateUpBitmap();
  }

  private long[] generateKingBitmap(){
    long[] output = new long[64];
    long temp;
    long king_xray;
    for (int i = 1; i < 63; i++){
      // Ignore File A and H.
      if (i % 8 != 0 && i % 8 != 7) {
        temp = 1L << i;
        king_xray = 0L;
        //left and right
        king_xray = king_xray | (temp << 1) | (temp >>> 1);
        // up
        king_xray = king_xray | (temp << 7);
        king_xray = king_xray | (temp << 8);
        king_xray = king_xray | (temp << 9);
        // down
        king_xray = king_xray | (temp >>> 7);
        king_xray = king_xray | (temp >>> 8);
        king_xray = king_xray | (temp >>> 9);

        output[i] = king_xray;
      }
    }
    // File A
    for (int i = 8; i < 56; i = i + 8){
      temp = 1L << i;
      king_xray = 0L;
      //up and down
      king_xray = king_xray | (temp << 8) | (temp >>> 8);
      // right
      king_xray = king_xray | (temp >>> 7);
      king_xray = king_xray | (temp << 1);
      king_xray = king_xray | (temp << 9);

      output[i] = king_xray;
    }
    // File H
    for (int i = 15; i < 63; i = i + 8){
      temp = 1L << i;
      king_xray = 0L;
      //up and down
      king_xray = king_xray | (temp << 8) | (temp >>> 8);
      // left
      king_xray = king_xray | (temp >>> 9);
      king_xray = king_xray | (temp >>> 1);
      king_xray = king_xray | (temp << 7);

      output[i] = king_xray;
    }
    // Corners
    output[0] = (1L << 1) | (1L << 8) | (1L << 9);  // A1
    output[7] = (1L << 15) | (1L << 6) | (1L << 14);  // H1
    output[56] = (1L << 48) | (1L << 49) | (1L << 57); // A8
    output[63] = (1L << 54) | (1L << 55) | (1L << 62); // H8

    return output;
  }


  private long[] generateKnightBitmap(){
    long[] output = new long[64];
    long temp;
    long knight_xray;
    for (int i = 1; i < 63; i++){
      // Ignore File A, B, G, and H.
      if (i % 8 != 0 && i % 8 != 1 && i % 8 != 6 && i % 8 != 7) {
        temp = 1L << i;
        knight_xray = 0L;
        // up
        knight_xray = knight_xray | (temp << 6);
        knight_xray = knight_xray | (temp << 10);
        knight_xray = knight_xray | (temp << 15);
        knight_xray = knight_xray | (temp << 17);
        // down
        knight_xray = knight_xray | (temp >>> 6);
        knight_xray = knight_xray | (temp >>> 10);
        knight_xray = knight_xray | (temp >>> 15);
        knight_xray = knight_xray | (temp >>> 17);

        output[i] = knight_xray;
      }
    }
    // File A
    for (int i = 8; i < 56; i = i + 8){
      temp = 1L << i;
      knight_xray = 0L;
      // up
      knight_xray = knight_xray | (temp << 10);
      knight_xray = knight_xray | (temp << 17);
      // down
      knight_xray = knight_xray | (temp >>> 6);
      knight_xray = knight_xray | (temp >>> 15);

      output[i] = knight_xray;
    }
    // File B
    for (int i = 1; i < 58; i = i + 8){
      temp = 1L << i;
      knight_xray = 0L;
      // up
      knight_xray = knight_xray | (temp << 10);
      knight_xray = knight_xray | (temp << 15);
      knight_xray = knight_xray | (temp << 17);
      // down
      knight_xray = knight_xray | (temp >>> 6);
      knight_xray = knight_xray | (temp >>> 15);
      knight_xray = knight_xray | (temp >>> 17);

      output[i] = knight_xray;
    }
    // File G
    for (int i = 6; i < 63; i = i + 8){
      temp = 1L << i;
      knight_xray = 0L;
      // up
      knight_xray = knight_xray | (temp << 6);
      knight_xray = knight_xray | (temp << 15);
      knight_xray = knight_xray | (temp << 17);
      // down
      knight_xray = knight_xray | (temp >>> 10);
      knight_xray = knight_xray | (temp >>> 15);
      knight_xray = knight_xray | (temp >>> 17);

      output[i] = knight_xray;
    }
    // File H
    for (int i = 15; i < 63; i = i + 8){
      temp = 1L << i;
      knight_xray = 0L;
      // up
      knight_xray = knight_xray | (temp << 6);
      knight_xray = knight_xray | (temp << 15);
      // down
      knight_xray = knight_xray | (temp >>> 10);
      knight_xray = knight_xray | (temp >>> 17);

      output[i] = knight_xray;
    }

    // Corners
    output[0] = (1L << 10) | (1L << 17);  // A1
    output[7] = (1L << 13) | (1L << 22);  // H1
    output[56] = (1L << 41) | (1L << 50); // A8
    output[63] = (1L << 46) | (1L << 53); // H8
    return output;
  }


  private long[][] generatePawnSingleBitmap(){
    // Need seperate boards for black and white pawns.
    long[][] output = new long[2][64];
    int color = 0;
    long temp = 0L;
    long pawn_xray = 0L;
    // White Pawns
    for(int i = 8; i < 56; i++){
      temp = 1L << i;
      output[Player.WHITE][i] = (temp << 8);
      output[Player.BLACK][i] = (temp >>> 8);
    }
    return output;
  }

  private long[][] generatePawnDoubleBitmap(){
    long[][] output = new long[2][8];
    for (int i = 0; i < 8; i++){
      output[Player.WHITE][i] = (1L << (24 + i));
      output[Player.BLACK][i] = (1L << (32 + i));
    }
    return output;
  }

  private long[][] generatePawnCaptureBitmap(){
    // Need seperate boards for black and white pawns.
    long[][] output = new long[2][64];
    long temp = 0L;
    for(int i = 9; i < 56; i++){
      if (i % 8 != 0 && i % 8 != 7) {
        temp = 1L << i;
        output[Player.WHITE][i] = (temp << 7) | (temp << 9);
        output[Player.BLACK][i] = (temp >>> 7) | (temp >>> 9);
      }
    }
    for(int i = 8; i < 56; i = i + 8){
      temp = 1L << i;
      output[Player.WHITE][i] = (temp << 9);
      output[Player.BLACK][i] = (temp >>> 7);
    }
    for(int i = 15; i < 63; i = i + 8){
      temp = 1L << i;
      output[Player.WHITE][i] = (temp << 7);
      output[Player.BLACK][i] = (temp >>> 9);
    }
    return output;
  }

  private long[] generateRightBitmap(){
    long[] output = new long[64];
    long temp = 0x00000000000000FFL;
    for(int i = 0; i < 64; i++){
      temp = temp ^ (1L << i);
      output[i] = temp;
      if(i % 8 == 7){
        temp = 0x00000000000000FFL << (i + 1);
      }
    }
    return output;
  }

  private long[] generateLeftBitmap(){
    long[] output = new long[64];
    long temp = 0L;
    for(int i = 1; i < 64; i++){
      temp = temp ^ (1L << (i - 1));
      output[i] = temp;
      if(i % 8 == 7){
        temp = 0L;
        i++;
      }
    }
    return output;
  }

  private long[] generateUpBitmap(){
    long[] output = new long[64];
    long temp = 0x101010101010100L;
    for(int i = 0; i < 56; i++){
      output[i] = temp << i;
    }
    return output;
  }

  static int count_set_bits(long n){
    int count = 0; // count accumulates the total bits set
    while(n != 0){
        n &= (n-1); // clear the least significant bit set
        count++;
    }
    return count;
  }

  public Move[] generateMoves(Bitboard bb){
    Move[] output = new Move[500];
    int index = 0;
    long piece_bitboard = 0L;
    // TODO: switch back to normal for loop once all pieces implemented.
    for (int piece : new int[]{0, 4, 5}){
      piece_bitboard = bb.board[bb.color][piece];

      // Iterate through the existing piece locations.
      while(piece_bitboard > 0){
        int old_pos = Long.numberOfTrailingZeros(piece_bitboard);
        piece_bitboard = piece_bitboard ^ (1L << (old_pos) ); // Turning off rightmost bit.

        long possible_moves = generatePieceMoves(bb, piece, old_pos);
        // Iterate through the current piece's possible moves.
        while(possible_moves > 0){
          int new_pos = Long.numberOfTrailingZeros(possible_moves);
          possible_moves = possible_moves ^ (1L << (new_pos)); // Turning off rightmost bit.
          output[index] = new Move(new Bitboard(bb, piece, old_pos, new_pos) , piece, old_pos, new_pos);
          index++;
        }
      }
    }
    return output;
  }

  public long generatePieceMoves(Bitboard bb, int piece, int pos){
    switch (piece) {
      case Player.KING:
        return king_xray[pos] & bb.ENEMY_AND_EMPTY_SQUARES;

      case Player.QUEEN:
        return 0L;

      case Player.ROOK:
        return 0L;

      case Player.BISHOP:
        return 0L;

      case Player.KNIGHT:
        return knight_xray[pos] & bb.ENEMY_AND_EMPTY_SQUARES;

      case Player.PAWN:
        return generatePawnMoves(bb, piece, pos);

      default: return 0L;
    }
  }

  private long generatePawnMoves(Bitboard bb, int piece, int pos){
    if(bb.color == Player.WHITE && pos < 16 && pos > 7){
      if ((pawn_single_xray[Player.WHITE][pos] & bb.EMPTY_SQUARES) > 0){
        return (pawn_single_xray[Player.WHITE][pos] & bb.EMPTY_SQUARES) | (pawn_double_xray[Player.WHITE][pos - 8] & bb.EMPTY_SQUARES) | (pawn_capture_xray[Player.WHITE][pos] & bb.ENEMY_SQUARES);
      }
      else return (pawn_capture_xray[Player.WHITE][pos] & bb.ENEMY_SQUARES);
    }
    else if (bb.color == Player.BLACK && pos > 47 && pos < 56) {
      if ((pawn_single_xray[Player.BLACK][pos] & bb.EMPTY_SQUARES) > 0){
        return ((pawn_single_xray[Player.BLACK][pos] & bb.EMPTY_SQUARES) | (pawn_double_xray[Player.BLACK][pos - 48] & bb.EMPTY_SQUARES) | (pawn_capture_xray[Player.BLACK][pos] & bb.ENEMY_SQUARES));
      }
      else return (pawn_capture_xray[Player.BLACK][pos] & bb.ENEMY_SQUARES);
    }
    return (pawn_single_xray[bb.color][pos] & bb.EMPTY_SQUARES) | (pawn_capture_xray[bb.color][pos] & bb.ENEMY_SQUARES);
  }

}