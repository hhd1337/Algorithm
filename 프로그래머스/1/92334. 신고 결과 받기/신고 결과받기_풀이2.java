// [구현 아이디어]
// 1. 신고당한놈을 기준으로 신고한 놈들을 Map에 저장한다. (Set.size()와 k를 비교하기만 하면 됨.)
// 2. 메일받을 놈을 기준으로 메일 몇개받을지 count하는 Map을 만든다.
// 3. 신고당한놈 Map을 돌면서, 신고당할놈이 정지당할놈이면,
//    정지당할놈을 신고한놈들 Set을 돌면서, 메일받을놈 Map에서 신고한놈들 찾아 count++

import java.util.*;

class Solution {
    public int[] solution(String[] id_list, String[] report, int k) {
        // key: 신고당한놈, value: 신고한 놈들
        Map<String, Set<String>> map = new HashMap<>();

        for(String r : report){
            String[] splited = r.split(" ");
            Set<String> set = map.getOrDefault(map.get(splited[1]), new HashSet<>());
            set.add(splited[0]);
            map.put(splited[1], set);
        }

        // key: 메일받을 놈, value: 메일 몇개받을지
        Map<String, Integer> countMap = new LinkedHashMap<>();

        for(String id : id_list){
            countMap.put(id, 0);
        }

        for(Map.Entry<String, Set<String>> entry : map.entrySet()){
            if(entry.getValue().size() >= k) { // 정지당할놈
                for(String value: entry.getValue()){
                    countMap.put(value, countMap.get(value) + 1); // 싹 0으로 초기화했기 때문에 getOrDefault 안해도됨.
                }
            }
        }

        return countMap.values().stream().mapToInt(Integer::intValue).toArray();
    }
}