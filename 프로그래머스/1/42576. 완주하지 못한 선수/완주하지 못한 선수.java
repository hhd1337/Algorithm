// [구현 아이디어 3]
// 두 배열을 정렬을 해서 처음으로 다른 지점이 미완주자가 된다.
// 정렬: O(n log n), 한 번 비교: O(n) == 총 O(n log n)

import java.util.*;
class Solution {
    public String solution(String[] participant, String[] completion) {
        Arrays.sort(participant);
        Arrays.sort(completion);
        
        for(int i=0; i<completion.length; i++){
            if(!participant[i].equals(completion[i])){
                return participant[i];
            }
        }
        
        return participant[participant.length - 1];
    }
}


// [구현 아이디어 1]
// - 동명이인이 있을 수 있으므로 Set 사용불가.
// 1. Map<String, Integer> 사용해서 participant를 저장
// 2. completion을 돌면서 해당 이름들을 Map에서 찾아 Integer(해당이름 사람 수)를 -1함.
// 3. 마지막에 Map에서 Integer != 0인 키값(이름)을 리턴함.

// import java.util.*;

// class Solution {
//     public String solution(String[] participant, String[] completion) {
//         Map<String, Integer> nameCountMap = new HashMap<>();
        
//         // 1. Map<String, Integer> 사용해서 participant를 저장
//         for(String p : participant){
//             if(nameCountMap.containsKey(p)){
//                 int count = nameCountMap.get(p);
//                 nameCountMap.replace(p, ++count);
//             }
//             else {
//                 nameCountMap.put(p, 1);
//             }
//         }
//         // 2. completion을 돌면서 해당 이름들을 Map에서 찾아 value값 --
//         for(String cn : completion){
//             int count = nameCountMap.get(cn);
//             nameCountMap.replace(cn, --count);
//         }
        
//         // 3. Map에서 Integer != 0인 키값(이름)을 리턴
//         String notCompletedParticipant = "";
//         for(Map.Entry<String, Integer> entry : nameCountMap.entrySet()){
//             if(entry.getValue() != 0){
//                 notCompletedParticipant = entry.getKey();
//                 break;
//             }
//         }
        
//         return notCompletedParticipant;
//     }
// }

// [구현 아이디어 2]
// 1. participant 배열로 participantList 생성
// 2. participantList에서 remove(Object o) 메소드 사용하여 completion에 있는 이름 다 제거
// (List.remove(Object o) 메소드는 리스트에 o가 중복 저장되어 있어도 앞에서부터 찾아서 하나만 제거함.)
// 하지만, 이 방법은 시간복잡도가 O(n²) 임. 따라서 효율성 테스트를 통과하지 못함.

// import java.util.*;
// class Solution {
//     public String solution(String[] participant, String[] completion) {
//         List<String> participantList = new ArrayList<>(Arrays.asList(participant));
        
//         for(String cn: completion){
//             participantList.remove(cn);
//         }
        
//         return participantList.get(0);
//     }
// }