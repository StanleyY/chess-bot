package magnus;

import java.util.Stack;

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

  public long[] deg45_board = null;
  public long[] deg135_board = null;
  public long[] deg225_board = null;
  public long[] deg315_board = null;

  public int[] PAWN_FINAL_RANK = new int[]{Player.RANK_7, Player.RANK_2};

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
    this.down_board = generateDownBitmap();

    this.deg45_board = generate45degBitmap();
    this.deg135_board = generate135degBitmap();
    this.deg225_board = generate225degBitmap();
    this.deg315_board = generate315degBitmap();
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

  private long[] generateDownBitmap(){
    long[] output = new long[64];
    long temp = 0x80808080808080L;
    for(int i = 63; i > 7; i--){
      output[i] = temp >>> (63 - i);
    }
    return output;
  }

  private long[] generate45degBitmap(){
    long[] files = generateLeftFileBitmap();
    long[] output = new long[64];
    long temp = 0x8040201008040200L;
    for(int i = 0; i < 56; i++){
      output[i] = (temp << i) & ~files[i % 8];
    }
    return output;
  }

  private long[] generate135degBitmap(){
    long[] files = generateLeftFileBitmap();
    long[] output = new long[64];
    long temp = 0x0002040810204080L;
    for(int i = 0; i < 56; i++){
      output[i] = temp & files[i % 8];
      temp = temp << 1;
    }
    return output;
  }

  private long[] generate225degBitmap(){
    long[] files = generateLeftFileBitmap();
    long[] output = new long[64];
    long temp = 0x0040201008040201L;
    for(int i = 63; i > 7; i--){
      output[i] = temp & files[i % 8];
      temp = temp >>> 1;
    }
    return output;
  }

  private long[] generate315degBitmap(){
    long[] files = generateLeftFileBitmap();
    long[] output = new long[64];
    long temp = 1L;
    for(int i = 8; i < 63; i++){
      temp = temp << 1;
      if (i % 8 == 0) temp = temp | (temp >>> 7);
      output[i] = temp & ~files[i % 8];
    }
    return output;
  }

  public long[] generateFileBitmaps(){
    // [FileA, FileB, FileC, FileD, FileE, FileF, FileG, FileH]
    return new long[]{0x0101010101010101L, 0x0202020202020202L, 0x0404040404040404L, 0x0808080808080808L,
                      0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L,};
  }

  public long[] generateLeftFileBitmap(){
    return new long[]{0x0101010101010101L, 0x0303030303030303L, 0x0707070707070707L, 0x0F0F0F0F0F0F0F0FL,
                      0x1F1F1F1F1F1F1F1FL, 0x3F3F3F3F3F3F3F3FL, 0x7F7F7F7F7F7F7F7FL, 0xFFFFFFFFFFFFFFFFL};
  }

  static int count_set_bits(long n){
    int count = 0; // count accumulates the total bits set
    while(n != 0){
        n &= (n-1); // clear the least significant bit set
        count++;
    }
    return count;
  }

  public Stack<Move> generateMoves(Bitboard bb){
    Stack<Move> output = new Stack<Move>();
    long piece_bitboard = 0L;

    for (int piece = 0; piece < 5; piece++){
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
          output.push(new Move(new Bitboard(bb, bb.color, piece, old_pos, new_pos) , piece, old_pos, new_pos));
        }
      }
    }
    // Pawn move generation.
    generateMovesHelper(bb, output);
    return output;
  }

  private void generateMovesHelper(Bitboard bb, Stack<Move> output){
    long piece_bitboard = bb.board[bb.color][Player.PAWN];
    while(piece_bitboard > 0){
      int old_pos = Long.numberOfTrailingZeros(piece_bitboard);
      piece_bitboard = piece_bitboard ^ (1L << (old_pos) ); // Turning off rightmost bit.

      long possible_moves = generatePawnMoves(bb, old_pos);
      // Iterate through the current pawn's possible moves.
      if(possible_moves > 0 && old_pos / 8 == PAWN_FINAL_RANK[bb.color]){
        while(possible_moves > 0){
          int new_pos = Long.numberOfTrailingZeros(possible_moves);
          possible_moves = possible_moves ^ (1L << (new_pos)); // Turning off rightmost bit.
          // Promotion to queen -> knight
          for(int new_piece = 1; new_piece < 5; new_piece++){
            output.push(new Move(new Bitboard(bb, bb.color, Player.PAWN, new_piece, old_pos, new_pos) , Player.PAWN, new_piece, old_pos, new_pos));
          }
        }
      }
      else{
        while(possible_moves > 0){
          int new_pos = Long.numberOfTrailingZeros(possible_moves);
          possible_moves = possible_moves ^ (1L << (new_pos)); // Turning off rightmost bit.
          output.push(new Move(new Bitboard(bb, bb.color, Player.PAWN, old_pos, new_pos) , Player.PAWN, old_pos, new_pos));
        }
      }
    }
  }

  public long generatePieceMoves(Bitboard bb, int piece, int pos){
    switch (piece) {
      case Player.KING:
        return king_xray[pos] & bb.ENEMY_AND_EMPTY_SQUARES;

      case Player.QUEEN:
        return generateRookMoves(bb, pos) | generateBishopMoves(bb, pos);

      case Player.ROOK:
        return generateRookMoves(bb, pos);

      case Player.BISHOP:
        return generateBishopMoves(bb, pos);

      case Player.KNIGHT:
        return knight_xray[pos] & bb.ENEMY_AND_EMPTY_SQUARES;

      case Player.PAWN:
        return generatePawnMoves(bb, pos);

      default: return 0L;
    }
  }

  private long generateRookMoves(Bitboard bb, int pos){
    long up_moves = up_board[pos] & bb.OCCUPIED_SQUARES;
    long down_moves = down_board[pos] & bb.OCCUPIED_SQUARES;
    long left_moves = left_board[pos] & bb.OCCUPIED_SQUARES;
    long right_moves = right_board[pos] & bb.OCCUPIED_SQUARES;
    /*
    The following steps:
    1. Find the first piece in contact. (done above)
    2. Shift to that bit.
    3. Remove overflow by AND.
    4. XOR find all possible moves.
    4. Ensure the obstacle piece is non-friendly by AND.
    */
    up_moves = ((up_moves << 8) | (up_moves << 16) | (up_moves << 24) |
                (up_moves << 32) | (up_moves << 40) | (up_moves << 48));
    up_moves = up_moves & up_board[pos];
    up_moves = (up_moves ^ up_board[pos]) & bb.ENEMY_AND_EMPTY_SQUARES;

    down_moves = ((down_moves >>> 8) | (down_moves >>> 16) | (down_moves >>> 24) |
                (down_moves >>> 32) | (down_moves >>> 40) | (down_moves >>> 48));
    down_moves = down_moves & down_board[pos];
    down_moves = (down_moves ^ down_board[pos]) & bb.ENEMY_AND_EMPTY_SQUARES;

    left_moves = ((left_moves >>> 1) | (left_moves >>> 2) | (left_moves >>> 3) |
                (left_moves >>> 6) | (left_moves >>> 5) | (left_moves >>> 6));
    left_moves = left_moves & left_board[pos];
    left_moves = (left_moves ^ left_board[pos]) & bb.ENEMY_AND_EMPTY_SQUARES;

    right_moves = ((right_moves << 1) | (right_moves << 2) | (right_moves << 3) |
                (right_moves << 6) | (right_moves << 5) | (right_moves << 6));
    right_moves = right_moves & right_board[pos];
    right_moves = (right_moves ^ right_board[pos]) & bb.ENEMY_AND_EMPTY_SQUARES;

    return up_moves | down_moves | left_moves | right_moves;
  }

  private long generateBishopMoves(Bitboard bb, int pos){
    long deg45_moves = deg45_board[pos] & bb.OCCUPIED_SQUARES;
    long deg135_moves = deg135_board[pos] & bb.OCCUPIED_SQUARES;
    long deg225_moves = deg225_board[pos] & bb.OCCUPIED_SQUARES;
    long deg315_moves = deg315_board[pos] & bb.OCCUPIED_SQUARES;
    /*
    The following steps:
    1. Find the first piece in contact. (done above)
    2. Shift to that bit.
    3. Remove overflow by AND.
    4. XOR find all possible moves.
    4. Ensure the obstacle piece is non-friendly by AND.
    */
    deg45_moves = ((deg45_moves << 9) | (deg45_moves << 18) | (deg45_moves << 27) |
                (deg45_moves << 36) | (deg45_moves << 45) | (deg45_moves << 54));
    deg45_moves = deg45_moves & deg45_board[pos];
    deg45_moves = (deg45_moves ^ deg45_board[pos]) & bb.ENEMY_AND_EMPTY_SQUARES;

    deg135_moves = ((deg135_moves << 7) | (deg135_moves << 14) | (deg135_moves << 21) |
                (deg135_moves << 28) | (deg135_moves << 35) | (deg135_moves << 42));
    deg135_moves = deg135_moves & deg135_board[pos];
    deg135_moves = (deg135_moves ^ deg135_board[pos]) & bb.ENEMY_AND_EMPTY_SQUARES;

    deg225_moves = ((deg225_moves >>> 9) | (deg225_moves >>> 18) | (deg225_moves >>> 27) |
                (deg225_moves >>> 36) | (deg225_moves >>> 45) | (deg225_moves >>> 54));
    deg225_moves = deg225_moves & deg225_board[pos];
    deg225_moves = (deg225_moves ^ deg225_board[pos]) & bb.ENEMY_AND_EMPTY_SQUARES;

    deg315_moves = ((deg315_moves >>> 7) | (deg315_moves >>> 14) | (deg315_moves >>> 21) |
                (deg315_moves >>> 28) | (deg315_moves >>> 35) | (deg315_moves >>> 42));
    deg315_moves = deg315_moves & deg315_board[pos];
    deg315_moves = (deg315_moves ^ deg315_board[pos]) & bb.ENEMY_AND_EMPTY_SQUARES;

    return deg45_moves | deg135_moves | deg225_moves | deg315_moves;
  }

  private long generatePawnMoves(Bitboard bb, int pos){
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