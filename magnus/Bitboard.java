package magnus;

class Bitboard {

  public static long[] masks = null;

  public Bitboard(){
    masks = generateMasks();
  }

  private long[] generateMasks(){
    long[] output = new long[64];
    for (int i = 0; i < output.length; i++){
      output[i] = 1L << i;
    }
    return output;
  }
}