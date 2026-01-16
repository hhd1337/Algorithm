import java.util.*;
import java.util.stream.*;

class Solution {
    public int solution(String dirs) {
        Moves moves = new Moves(new ArrayList<>());
        moves.makeMoves(dirs);
        
        return moves.countDistinctMoves();
    }
}

class Moves {
    private List<Move> moveList;
    
    public Moves(List<Move> moveList){
        this.moveList = moveList;
    }
    
    public void makeMoves(String dirs){
        char[] moves = dirs.toCharArray();
        int currX = 0;
        int currY = 0; 
        
        for(char move : moves){
            if(move == 'U'){
                if(currY + 1 > 5) continue; // 조건) 좌표평면의 경계를 넘어가는 명령어는 무시
                moveList.add(new Move(currX, currY, currX, currY + 1));
                currY++;
            }
            if(move == 'D'){
                if(currY - 1 < -5) continue;
                moveList.add(new Move(currX, currY, currX, currY - 1));
                currY--;
            }
            if(move == 'R'){
                if(currX + 1 > 5) continue;
                moveList.add(new Move(currX, currY, currX + 1, currY));
                currX++;
            }
            if(move == 'L'){
                if(currX - 1 < -5) continue;
                moveList.add(new Move(currX, currY, currX - 1, currY));
                currX--;
            }
        }
    }
    
    public int countDistinctMoves(){
        List<Move> distinctMoveList = moveList.stream()
                                            .distinct()
                                            .collect(Collectors.toList());
        
        int oppositeExistsMoveDoubleCount = (int)distinctMoveList.stream()
                                    .filter(move->isOppositeMoveExists(move))
                                    .count();
        
        int oppositeExistsMoveCount = oppositeExistsMoveDoubleCount / 2;
        
        return distinctMoveList.size() - oppositeExistsMoveCount;
    }
    
    public boolean isOppositeMoveExists(Move move_){
        int startX = move_.getStartX();
        int startY = move_.getStartY();
        int endX = move_.getEndX();
        int endY = move_.getEndY();
            
        return moveList.stream()
                    .anyMatch(move->
                        move.getStartX() == endX &&
                        move.getStartY() == endY &&
                        move.getEndX() == startX &&
                        move.getEndY() == startY
                    );
    }
    
}

class Move {
    private int startX;
    private int startY; 
    private int endX;
    private int endY;
    
    public Move(int startX, int startY, int endX, int endY){
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }
    
    public int getStartX(){
        return this.startX;
    }
    public int getStartY(){
        return this.startY;
    }
    public int getEndX(){
        return this.endX;
    }
    public int getEndY(){
        return this.endY;
    }
    
    // 내용기반 동일 규칙으로 재정의
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        
        Move move = (Move) o;
        return startX == move.startX &&
               startY == move.startY &&
               endX == move.endX &&
               endY == move.endY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startX, startY, endX, endY);
    }
}