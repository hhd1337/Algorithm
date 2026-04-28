/*
PGS 43165 - 타겟넘버

[조건]
1) n개의 정수가 있음. 
2) 모든 주어진 정수를 빼거나 더해서 타겟넘버로 만들 수 있는경우의수를 구하라.

[접근]
- 매 재귀의 선택지: 음수/양수
- 선택: 음수한번, 양수한번
- 재귀호출
- 선택복구: 음/양 반대로
- 종료조건: numbers배열이 끝나면 종료
- 연산 수: n <= 20이니, 완전탐색을 한다면 2^20가지 경우가 나옴. 
          2^10 = 10^3 정도라고 보면 10^6 = 백만 정도로 완전탐색을 해도 연산량이 매우 작음.
==> 백트래킹

[느낀점]
전형적인 백트래킹 문제였던 것 같다.
*/
import java.util.*;
 
class Solution {
    int N;
    int target;
    int[] numbers;
    int matchCount;
    int[] multiplyValue = {-1,1};
    
    public int solution(int[] numbers, int target) {
        N = numbers.length;
        this.target = target;
        this.numbers = numbers;
        
        backtrack(0,0);
        
        return matchCount;
        
    }
    
    private void backtrack(int sum, int idx) {
        //종료조건
        if(idx > N-1){
            if(sum == target) matchCount++;
            return;
        }
        
        for(int i=0; i<=1; i++){
            //선택: 이번 숫자 빼거나 더함
            sum += numbers[idx] * multiplyValue[i];
        
            //재귀호출
            backtrack(sum, idx+1);
        
            //선택취소
            sum -= numbers[idx] * multiplyValue[i];
        }
    }
}