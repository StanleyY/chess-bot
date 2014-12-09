package magnus;

class Bitmap{

  public long[] king_xray = null;
  public long[] knight_xray = null;

  public Bitmap(){
    generateBitmaps();
  }

  private void generateBitmaps(){
    this.king_xray = generateKingBitmap();
    this.knight_xray = generateKnightBitmap();
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

}