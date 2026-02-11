/*
[구현 아이디어]
- 집은 3개 이상 1,000,000개 이하니까 완전탐색은 아니고, 배열을 돌면서 
  각 칸에 해당집까지 훔칠 수 있는 최댓값을 넣는 식으로, dp로 풀어야 할 듯.
  1) i번째 칸을 안 턴다 -> dp[i-1] 그대로
  2) i번째 칸을 턴다(i-1은 못 털음) -> dp[i-2] + money[i]
  
- 원형이기 때문에 0번 집과 n-1번 집이 인접이라 둘을 동시에 털 수 없음.
  그래서 원형을 선형으로 생각하고, 0번 집을 터는경우, 0번 집을 안터는 경우 이렇게 생각해야할듯
  즉, 0번 집을 털면 마지막 집은 절대 털 수 없고, 0번 집을 안 털면 마지막 집은 털수도/안 털수도 있다.
  (처음에는 0번 집을 터는경우, n-1번 집을 터는 경우로 나눠볼까 했는데 그러면 둘 다 안터는 경우를 고려하지 못함.)
*/

import java.util.*;

class Solution {  
    public int solution(int[] money) {
        int n = money.length;
        // 0번집 고려, n-1번집 안털음
        int caseA = getMaxRobInRange(money, 0, n - 2);
        // 0번집 안털고 n-1번집까지 고려
        int caseB = getMaxRobInRange(money, 1, n - 1);
        
        return Math.max(caseA, caseB);
    }
    
    private int getMaxRobInRange(int[] money, int start, int end){
        int previous2 = 0; // 2칸 전
        int previous1 = 0; // 바로 전
        
        for (int i=start; i<=end; i++) {
            int curr = Math.max(previous1, previous2 + money[i]);
            previous2 = previous1;
            previous1 = curr;
        }
        return previous1;
    }
}