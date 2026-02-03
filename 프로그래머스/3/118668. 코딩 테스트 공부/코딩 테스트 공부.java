// [문제 해석]
// 알고력/코딩력을 높이기 위한 3 방법
// 1. 알고리즘 공부 - 알고력+1 / 시간1
// 2. 코딩 공부 - 코딩력+1 / 시간1
// 3. 지금 풀 수 있는 문제 풀음 - 문제가 제공하는 (알고력+, 코딩력+) / 문제푸는데 걸리는 시간

/*
1차 풀이 시도에서 그리디로 접근했었고, 실패함.

[왜 while(그리디)로 풀면 안 되는가]
- 지금 가장 좋아 보이는 선택(공부 / 풀 수 있는 문제 중 효율 좋은 것)이 전체 최소 시간을 보장하지 않는다.
- 효율이 낮아 보여도, 이후에 고효율 문제를 열어주는 "중간 단계"가 될 수 있다.
- 알고력,코딩력 두 축이 동시에 증가하며 서로 영향을 주기 때문에 한 방향만 따라가는 while 방식은 최적 경로를 놓치기 쉽다. -> 이때 벌써 완탐, dp를 떠올렸어야 함.
- 즉, 로컬 최적 선택의 누적이 글로벌 최적이 아니다.

[왜 DP 문제인가]
- 1. 모든 상태를 고려해야 최소 시간이 보장된다 !!! -> 그리디 말고 완탐/DP 떠올렸어야 함.
- 2. 완탐으로 풀기에 경우의 수가 너무 많고, 경우의 수들에 중복연산이 너무 많다.
- 3. 상태는 '현재 알고력과 코딩력' -> 2차원 배열 인덱스를 알고력, 코딩력으로 쓰기 좋음.
- 4. 선택지는 공부하거나, 조건을 만족하는 문제를 푸는 것뿐이며, 각 선택마다 시간 비용이 고정되어 있다.
- 5. 목표 상태까지 가는 최소 시간을 구하는 문제 → 최단거리 DP 의심하자
*/

import java.util.*;

class Solution {
    public int solution(int alp, int cop, int[][] problems) {
        int maxNeedAlp = 0, maxNeedCop = 0;
        
        // 모든문제 풀 수 있기 위해 필요한 알고력,코딩력 구함
        for(int i=0; i<problems.length; i++){
            maxNeedAlp = Math.max(problems[i][0], maxNeedAlp);
            maxNeedCop = Math.max(problems[i][1], maxNeedCop);
        }
        // 시작값이 목표를 넘으면 dp범위 밖 접근하므로 한번 clamp
        alp = Math.min(alp, maxNeedAlp);
        cop = Math.min(cop, maxNeedCop);
        
        int[][] dp = new int[maxNeedAlp+1][maxNeedCop+1]; // 현재(알고력,코딩력)까지 도달한 최소시간 저장
        
        for(int i=0; i<=maxNeedAlp; i++){
            Arrays.fill(dp[i], 1000000); // 큰값으로 초기화
        }
        
        dp[alp][cop] = 0; // 시작상태의 최소시간은 0
        
        // dp[][] 돌면서, 현재 상태에서 알고공부, 코딩공부, 문제푼경우 전부 계산하며, 각 자리에 최소시간 남김
        for(int a = alp; a <= maxNeedAlp; a++){
            for(int c = cop; c <= maxNeedCop; c++){
                int currTime = dp[a][c];
                
                // 1. 알고공부 -> 알고력+1, 시간+1
                if (a + 1 <= maxNeedAlp) {
                    dp[a+1][c] = Math.min(dp[a+1][c], currTime + 1);
                }
                
                // 2. 코딩공부 -> 코딩력+1, 시간+1
                if (c + 1 <= maxNeedCop) {
                    dp[a][c+1] = Math.min(dp[a][c+1], currTime + 1);
                }
                
                // 3. 모든 풀 수 있는 문제 푼 경우 싹 계산
                for(int i=0; i<problems.length; i++){
                    if(a >= problems[i][0] && c >= problems[i][1]){ // problems 원소: [필요알고력, 필요코딩력, 증가알고력, 증가코딩력, 필요시간]
                        int nextAlp = Math.min(maxNeedAlp, a + problems[i][2]);
                        int nextCop = Math.min(maxNeedCop, c + problems[i][3]);
                        int needTime = problems[i][4];
                        
                        dp[nextAlp][nextCop] = Math.min(dp[nextAlp][nextCop], currTime + needTime);
                    }
                }
            }   
        }
        
        return dp[maxNeedAlp][maxNeedCop];
    }
}

// import java.util.*;

// class Solution {
//     int shortestTime = 0;
//     int maxNeedAlp = 0;
//     int maxNeedCop = 0;
//     int currAlp;
//     int currCop;
//     List<int[]> easyProblems = new ArrayList<>();
    
//     public int solution(int alp, int cop, int[][] problems) {
//         currAlp = alp;
//         currCop = cop;
        
//         // 모든문제 풀 수 있기 위해 필요한 알고력,코딩력 구함
//         for(int i=0; i<problems.length; i++){
//             if(problems[i][0] > maxNeedAlp) {
//                 maxNeedAlp = problems[i][0];
//             }
//             if(problems[i][1] > maxNeedCop) {
//                 maxNeedCop = problems[i][1];
//             }
//         }
        
//         // shortestTime을 구함, 어떻게..? ****
//         while(currAlp < maxNeedAlp && currCop < maxNeedCop){
//             int alpLack = maxNeedAlp - currAlp; // 현재 부족한 alp
//             int copLack = maxNeedCop - currCop; // 현재 부족한 cop
            
//             updateEasyProblems();
//             // 문제를 풀 수 없는 경우, 알고공부해야되냐 코딩공부해야되냐?
//             if(easyProblems.size() == 0){
                
//             }
                
//         }
        
//         return shortestTime;
//     }
    
//     private void studyAlgo(){
//         currAlp++;
//         shortestTime++;
//     }
    
//     private void studyCoding(){
//         currCop++;
//         shortestTime++;
//     }
//     // problems의 길이 ≤ 6 이라서 그냥 problems 다 돌면서 조건 검사하도록 함.
//     private void updateEasyProblems(int[][] problems){
//         easyProblems.clear();
        
//         for(int i=0; i<problems.length; i++){
//             if(currAlp >= problem[i][0] && currCop >= problem[i][1]){
//                 easyProblems.add(problem[i]);
//             }  
//         }
//     }
// }