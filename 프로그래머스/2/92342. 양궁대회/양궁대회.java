import java.util.*;

class Solution {
    private int[] bestLionResult = null;
    private int maxDiff = 0;
    
    public int[] solution(int n, int[] appeachResult) {
        int[] lionResult = new int[11];

        dfs(0, n, lionResult, appeachResult);
            
        if (bestLionResult == null) {
            return new int[]{-1};
        }
        return bestLionResult;
    }
        
    private void dfs(int idx, int remain, int[] lion, int[] appeach){ // idx는 지금 결정할 점수 칸. idx=0 -> 10점칸
        if(idx == 10){
            lion[idx] = remain; // 남은 화살 전부 0점칸에 넣음
            judgeScoreAndUpdate(lion, appeach);
            lion[idx] = 0;
            return;
        }
            
        for(int count = 0; count <= remain; count++){
            lion[idx] = count; // idx칸에 0발부터 남은화살전부까지 다 넣어봄.
            dfs(idx + 1, remain-count, lion, appeach); // 재귀로 다음칸 이동, 남은 화살수를 remain-count로 갱신
            lion[idx] = 0; // 재귀로 끝까지 화살배치,평가 끝났으면 끝부터 쭉 idx칸 0으로 만들어 다음 count 준비
        }
    }
        
    private void judgeScoreAndUpdate(int[] lion, int[] appeach){
        int lionScore = 0;
        int appeachScore = 0;
            
        for(int i=0; i<11; i++){
            int thisScore = 10 - i;
               
            if(lion[i] == 0 && appeach[i] == 0){
                continue;
            }
                
            if(lion[i] > appeach[i]){
                lionScore += thisScore;
            } else {
                appeachScore += thisScore;
            }
        }
            
        int thisDiff = lionScore - appeachScore;
        if (thisDiff <= 0) {
            return;
        }
        if(thisDiff > maxDiff){
            maxDiff = thisDiff;
            bestLionResult = lion.clone();
            return;
        }
        if(thisDiff == maxDiff){
            if (isBetterLion(lion)) {
                bestLionResult = lion.clone();
            }
        }
    }
        
    private boolean isBetterLion(int[] thisLion) {
        for (int i = 10; i >= 0; i--) {
            if (thisLion[i] != bestLionResult[i]) {
                return thisLion[i] > bestLionResult[i];
            }
        }
        return false;
    }
}