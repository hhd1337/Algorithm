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
1)백트래킹에서 선택지를 자연스럽게 순회하기 위해 for문을 쓰는 것에 익숙하다 보니, 
int[] multiplyValue = {-1,1}; 를 만들어 for문으로 순회하게 했고, 이 방법은 불필요하게 복잡했다. 
해결책은 그냥 backtrack() 메서드를 두번 호출하면 되는 것이었다. 
이번재귀에서, 선택지들을 모두 한번씩 선택해서 다시 트리 리프방향으로 돌려보내주기만 하면 된다는 감을 확실히 익혔다. 

2)또, 현재 인덱스를 건드리지 않고 인자로 +-연산을 통해 계산된 값을 넘긴다면, 
선택복구를 할 필요도 없다는것을 확실히 익혔다.
*/
import java.util.*;
 
class Solution {
    int N;
    int target;
    int[] numbers;
    int matchCount;
    //int[] multiplyValue = {-1,1}; // 레거시 코드
    
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
        // 수정한 코드
        backtrack(sum-numbers[idx], idx+1);
        backtrack(sum+numbers[idx], idx+1);
        
        /* 레거시 코드
        for(int i=0; i<=1; i++){
            //선택: 이번 숫자 빼거나 더함
            sum += numbers[idx] * multiplyValue[i];
        
            //재귀호출
            backtrack(sum, idx+1);
        
            //선택취소
            sum -= numbers[idx] * multiplyValue[i];
        }
        */
    }
}