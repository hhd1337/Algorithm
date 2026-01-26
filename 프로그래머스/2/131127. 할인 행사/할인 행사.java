// [구현 아이디어]
// 1. want, number 배열로 Map<String, Integer> 생성
// 2. discount 배열을 돌며 회원가입 시 10일 동안의 Map<String, Integer> 생성, 1번의 배열과 같다면 count
// 3. count한 개수 반환


import java.util.*;

class Solution {
    public int solution(String[] want, int[] number, String[] discount) {        
        // want, number 배열로 Map<String, Integer> 생성
        Map<String, Integer> wantCountMap = new HashMap<>();
        for(int i=0; i<want.length; i++){
            wantCountMap.put(want[i],number[i]);
        }
        
        int count = 0;
        
        for(int i=0; i<=discount.length-10; i++){
            Map<String, Integer> discountFor10days = new HashMap<>(); // 10일동안의 할인품목들
            
            for(int j = i; j < (i+10) ; j++){
                String key = discount[j];
                // key가 이미 존재한다면 
                if(discountFor10days.containsKey(key)){
                    int currCount = discountFor10days.get(key);
                    discountFor10days.replace(key, ++currCount);
                }
                else{ // key가 처음 추가된다면 
                    discountFor10days.put(key, 1);
                }
            }
            
            // 만약 이번 10일동안의 할인품목들이 wantCountMap과 같다면 카운트.
            if(discountFor10days.equals(wantCountMap)){
                count++;
            }
        
        }
        
        return count;
    }
}