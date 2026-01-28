// [구현 아이디어]
// 1. 신고자, 신고당한사람 리스트로 구성된 HashMap 만듦
// 2. 유저, 이 유저가 신고당한 횟수 Map 만듦 -> (조건: 이사람이 이 유저를 신고 n번 했다면 신고 횟수는 1회로 처리)
// 3. 신고당한 횟수가 k 이상인 유저들만 Set에 담음
// 4. id_list를 돌면서 1번 Map Set돌면서 메일 몇개받을지 배열에 담음
//    (자신이 신고한 사람이 3번Set에 몇명 포함되어 있는지)

import java.util.*;

class Solution {
    public int[] solution(String[] id_list, String[] report, int k) {
        Map<String, Set<String>> userReportMap = new HashMap<>();
        Map<String, Integer> userReportedCountMap = new HashMap<>();
        Set<String> blackListSet = new HashSet<>();
        
        // 0. 미리 Map에 모든 유저 키를 만들어둠
        for (String id : id_list){
            userReportMap.put(id, new HashSet<>());
        } 
        
        // 1. 신고자, 신고당한사람 리스트로 구성된 HashMap 만듦
        for(String r : report){
            String[] thisReport = r.split(" ");
            String reporter = thisReport[0];
            String reported = thisReport[1];
            
            Set<String> reportedSet = userReportMap.get(reporter);
            boolean isFirstAdd = reportedSet.add(reported); // ** Set.add() 는 처음 add 시 true, 처음이아니라 add가 안되면 false반환
            userReportMap.put(reporter, reportedSet);
            
            // 2. 유저, 이 유저가 신고당한 횟수 Map 만듦
            // (조건: 이사람이 이 유저를 신고 n번 했다면 신고 횟수는 1회로 처리)
            if(isFirstAdd){
                userReportedCountMap.put(reported, userReportedCountMap.getOrDefault(reported,0) + 1);
            }
        }
        
        // 3. 신고당한 횟수가 k 이상인 유저들만 Set에 담음
        for(Map.Entry<String, Integer> entry : userReportedCountMap.entrySet()){
            if(entry.getValue() >= k){
                blackListSet.add(entry.getKey());
            }
        }
        
        // 4. id_list를 돌면서 1번 Map Set돌면서 메일 몇개받을지 배열에 담음
        int[] answer = new int[id_list.length];
        
        for(int i=0; i<id_list.length; i++){
            int mailCount = 0;
            //** 미리 Map에 모든 유저 키를 만들어두는 0번과정이 없었다면, 
            // report를 한번도 안한 유저면 userReportMap.get() 했을때 NPE 터짐. -> Collections.emptySet()으로 빈 Set 받아도 되긴함
            // Set<String> reportedSet = userReportMap.getOrDefault(id_list[i], Collections.emptySet());
            Set<String> reportedSet = userReportMap.get(id_list[i]);
            
            for(String name : reportedSet){
                if(blackListSet.contains(name)){
                    mailCount++;
                }
            }
            answer[i] = mailCount;
        }
        
        return answer;
    }
}