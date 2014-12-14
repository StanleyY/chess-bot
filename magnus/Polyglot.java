package magnus;
import java.io.*;

/*
key    uint64 8
move   uint16 2
weight uint16 2
learn  uint32 4
*/

class Polyglot{
  public static byte[] book = null;
  public static final char[] FILE_NAME = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
  public static final String[] PIECE_NAME = new String[]{"K", "Q", "R", "B", "N", "P"};

  public static void main(String[] args){
    read();
    System.out.println("Size: " + book.length);
    System.out.println("Entries: " + (book.length / 16));
    int found = 0;
    for (int i = 0; i < book.length; i = i + 16){
      long key =  0L;
      byte[] by = new byte[]{book[i], book[i+1], book[i+2], book[i+3], book[i+4], book[i+5], book[i+6], book[i+7]};
      for (int j = 0; j < by.length; j++){
        key = (key << 8) + (by[j] & 0xff);
      }
      int move = 0;
      move = (move << 8) + (book[i + 8] & 0xff);
      move = (move << 8) + (book[i + 9] & 0xff);

      char old_file = FILE_NAME[(move & 7)];
      int old_rank = ((int)(move >>> 3) & 7) + 1;
      char new_file = FILE_NAME[(int)((move >>> 6) & 7)];
      int new_rank = ((int)(move >>> 9) & 7) + 1;

      int weight = 0;
      weight = (weight << 8) + (book[i + 10] & 0xff);
      weight = (weight << 8) + (book[i + 11] & 0xff);

      long test_key = 0x823c9b50fd114196L;
      //long test_key = 0x463b96181691fc9cL;
      if(test_key == key){
        found++;
        System.out.println("move: " + Integer.toBinaryString(move));
        System.out.printf("move %c%d, %c%d\n", old_file, old_rank, new_file, new_rank);
        System.out.println("weight: " + weight);
        System.out.println("");
      }
    }
    System.out.println("FOUND MATCHES: " + found);
  }

  static void read(){
    try {
      FileInputStream input = new FileInputStream("performance");
      book = new byte[input.available()];
      input.read(book);
      input.close();
    } catch (java.io.IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}