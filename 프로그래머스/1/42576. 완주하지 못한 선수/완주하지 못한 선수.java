// [구현 아이디어 1]
// - 동명이인이 있을 수 있으므로 Set 사용불가.
// 1. Map<String, Integer> 사용해서 participant를 저장
// 2. completion을 돌면서 해당 이름들을 Map에서 찾아 Integer(해당이름 사람 수)를 -1함.
// 3. 마지막에 Map에서 Integer != 0인 키값(이름)을 리턴함.

import java.util.*;

class Solution {
    public String solution(String[] participant, String[] completion) {
        Map<String, Integer> nameCountMap = new HashMap<>();
        
        // 1. Map<String, Integer> 사용해서 participant를 저장
        for(String p : participant){
            if(nameCountMap.containsKey(p)){
                int count = nameCountMap.get(p);
                nameCountMap.replace(p, ++count);
            }
            else {
                nameCountMap.put(p, 1);
            }
        }
        // 2. completion을 돌면서 해당 이름들을 Map에서 찾아 value값 --
        for(String cn : completion){
            int count = nameCountMap.get(cn);
            nameCountMap.replace(cn, --count);
        }
        
        // 3. Map에서 Integer != 0인 키값(이름)을 리턴
        String notCompletedParticipant = "";
        for(Map.Entry<String, Integer> entry : nameCountMap.entrySet()){
            if(entry.getValue() != 0){
                notCompletedParticipant = entry.getKey();
                break;
            }
        }
        
        return notCompletedParticipant;
    }
}