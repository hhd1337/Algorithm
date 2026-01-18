import java.util.*;

class Solution {
    public int solution(int[][] board, int[] lanes) {
        StackBasket basket = new StackBasket();
        Board gameBoard = new Board(board);

        for(int lane : lanes){
            Integer doll = gameBoard.takeOutDollOrNull(lane);
            if(doll != null){
                basket.pushDoll(doll);
            }
        }

        return basket.getPoppedDollCount();
    }
}

class Board {
    private static final int EMPTY_CELL = 0;
    private int[][] board;

    public Board(int[][] board){
        this.board = board;
    }

    public Integer takeOutDollOrNull(int verticalLane){
        for(int i=0; i<board.length; i++){
            int cell = board[i][verticalLane - 1];
            if(cell == EMPTY_CELL){
                continue;
            }
            board[i][verticalLane - 1] = 0; // 해당 cell을 0으로 바꿈.
            return cell; // cellNum이 곧 doll임.
        }
        return null;
    }

}

class StackBasket {
    private ArrayDeque<Integer> stackBasket;
    private int poppedDollCount;

    public StackBasket() {
        this.stackBasket = new ArrayDeque<>();
        this.poppedDollCount = 0;
    }

    public void pushDoll(int doll){
        if(stackBasket.isEmpty()){
            stackBasket.push(doll);
            return;
        }

        if(stackBasket.peek() == doll){
            stackBasket.pop();
            poppedDollCount = poppedDollCount + 2;
            return;
        }

        stackBasket.push(doll);
    }

    public int getPoppedDollCount() {
        return poppedDollCount;
    }
}