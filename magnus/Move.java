package magnus;

class Move{

  // The new board
  Bitboard board;
  int piece;
  int old_pos;
  int new_pos;

  public Move(Bitboard bb, int piece, int old_pos, int new_pos){
    this.piece = piece;
    this.old_pos = old_pos;
    this.new_pos = new_pos;
    this.board = bb;
  }

}