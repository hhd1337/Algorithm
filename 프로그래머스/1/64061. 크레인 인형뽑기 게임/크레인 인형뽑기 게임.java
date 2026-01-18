import java.util.*;

class Solution {
    public int solution(int[][] board, int[] moves) {
        int popCount = 0;
        
        ArrayDeque<Integer> stackBasket = new ArrayDeque<>();
        
        for(int move : moves){
            for(int i = 0; i < board.length; i++){
                int cellNum = board[i][move-1];
                
                if(cellNum != 0){
                    if(stackBasket.isEmpty()){
                        stackBasket.push(cellNum);
                        board[i][move-1] = 0;
                        break;
                    }
                    if(stackBasket.peek() != cellNum){
                        stackBasket.push(cellNum);
                        board[i][move-1] = 0;
                        break;
                    }
                    if(stackBasket.peek() == cellNum) {
                        stackBasket.pop();
                        popCount++;
                        board[i][move-1] = 0;
                        break;
                    }
                }
            }
            
        }
        
        return popCount * 2;
    }
}