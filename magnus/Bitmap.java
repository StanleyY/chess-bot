package magnus;

class Bitmap{

  public long[] king_xray = null;

  public Bitmap(){
    generateBitmaps();
  }

  private void generateBitmaps(){
    this.king_xray = generateKingBitmap();
  }

  private long[] generateKingBitmap(){
    long[] output = new long[64];
    long temp;
    long king_xray;
    for (int i = 9; i < 55; i++){
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
    // Rank 1
    for (int i = 1; i < 7; i++){
      temp = 1L << i;
      king_xray = 0L;
      //left and right
      king_xray = king_xray | (temp << 1) | (temp >>> 1);
      // up
      king_xray = king_xray | (temp << 7);
      king_xray = king_xray | (temp << 8);
      king_xray = king_xray | (temp << 9);

      output[i] = king_xray;
    }
    // Rank 8
    for (int i = 57; i < 63; i++){
      temp = 1L << i;
      king_xray = 0L;
      //left and right
      king_xray = king_xray | (temp << 1) | (temp >>> 1);
      // down
      king_xray = king_xray | (temp >>> 7);
      king_xray = king_xray | (temp >>> 8);
      king_xray = king_xray | (temp >>> 9);

      output[i] = king_xray;
    }
    // Corners
    output[0] = (1L << 1) | (1L << 8) | (1L << 9);  // A1
    output[7] = (1L << 15) | (1L << 6) | (1L << 14);  // H1
    output[56] = (1L << 48) | (1L << 49) | (1L << 57); // A8
    output[63] = (1L << 54) | (1L << 55) | (1L << 62); // H8

    return output;
  }

}