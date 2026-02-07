/*
[구현 아이디어]
1. 그냥 s에 있는 모든 숫자들을 숫자/빈도수 Map으로 만듦.
2. 빈도수(value) 기준으로 Map.Entry 정렬
3. 정렬된 상태에서 순서대로 key를 int[]로 파싱해서 반환
*/

import java.util.*;

class Solution {
    public int[] solution(String s) {
        // key: 숫자, value: 빈도수
        Map<Integer, Integer> numberCount = new HashMap<>();
        
        s = s.substring(2, s.length() - 2); // 2},{2,1},{2,1,3},{2,1,3,4
        String[] groups = s.split("\\},\\{"); // [2], [2,1], [2,1,3], [2,1,3,4]
        List<Integer> numbers = new ArrayList<>(); // 2,2,1,2,1,3,2,1,3,4
        
        // s에 있는 모든 숫자들 List 만듦
        for(String g : groups){
            String[] numStrings = g.split(",");
            for(String numString : numStrings){
                numbers.add(Integer.parseInt(numString));
            }
        }
        
        // List 돌면서 숫자/빈도수 Map 만듦
        for(int num : numbers){
            numberCount.put(num, numberCount.getOrDefault(num, 0) + 1);
        }
        
        // 빈도수(value) 기준으로 Map.Entry 정렬
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(numberCount.entrySet());
        entryList.sort((a,b) -> b.getValue() - a.getValue());
        
        // 정렬한 순서대로 key를 int[]에 넣고 반환
        int[] answer = new int[entryList.size()];
        for(int i=0; i<entryList.size(); i++){
            answer[i] = entryList.get(i).getKey();
        }
        return answer;
    }
}