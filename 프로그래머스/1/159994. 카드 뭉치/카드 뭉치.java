// [구현 아이디어]
// 1. cards1Index, cards2Index를 0으로 초기화
// 2. goal을 돌면서 지금 단어가 cards1Index, cards2Index 자리에 있는지 검사. 아니면 return "No"
// 3. 있다면 cards1Index or cards2Index 를 ++

import java.util.*;

class Solution {
    public String solution(String[] cards1, String[] cards2, String[] goal) {
        int index1 = 0;
        int index2 = 0;
        
        for(int i=0; i<goal.length; i++){
            if(index1 < cards1.length && goal[i].equals(cards1[index1])){
                index1++;
                continue;
            }
            if(index2 < cards2.length && goal[i].equals(cards2[index2])){
                index2++;
                continue;
            }
            return "No";
        }
        
        return "Yes";
    }
}