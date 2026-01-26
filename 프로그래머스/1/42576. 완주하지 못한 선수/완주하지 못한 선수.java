import java.util.*;

class Solution {
    public String solution(String[] participant, String[] completion) {
        Map<String, Integer> countMap = new HashMap<>();

        // 참가자 이름별 등장 횟수 세기
        for (String name : participant) {
            countMap.put(name, countMap.getOrDefault(name, 0) + 1);
        }

        // 완주자 이름별 등장 횟수 차감
        for (String name : completion) {
            countMap.put(name, countMap.get(name) - 1);
        }

        // 남은 값이 1인 사람 -> 완주 못한 사람
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() != 0) {
                return entry.getKey();
            }
        }
        
        return "";
    }
}
