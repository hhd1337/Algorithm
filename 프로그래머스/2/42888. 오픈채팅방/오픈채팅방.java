// [구현 아이디어]
// 1. record 배열을 돌면서 요소를 split(" ")하여,
// 1-1. splited[0].equals("Enter")이면 uid, nickname Map<Stirng, String> 에 담는다. 
// 1-2. splited[0].equals("Change")이면 해당 uid 엔트리를 찾아서 nickname을 replace한다.

// 2. record 배열을 다시 돌면서 resultMsg 배열에 각 메시지 넣고 반환

import java.util.*;

class Solution {
    public String[] solution(String[] record) {
        Map<String, String> nicknameByUid = new HashMap<>();
        // 1. record 배열을 돌면서 uid, nickname을 Map에 저장한다.(나갔다고 remove()하지말기)
        for(String r : record){
            String[] splited = r.split(" ");
            String cmd = splited[0];
            String uid = splited[1];
            
            if(cmd.equals("Enter") || cmd.equals("Change")){
                nicknameByUid.put(uid, splited[2]);
            }
        }
        
        // 2. record 배열을 다시 돌면서 resultMsg 배열에 각 메시지 넣음
        String[] resultMsg = new String[record.length];
        
        for(int i=0; i<record.length; i++) {
            String[] splited = record[i].split(" ");
            String cmd = splited[0];
            String uid = splited[1];
            
            if(cmd.equals("Enter")){
                resultMsg[i] = nicknameByUid.get(uid) + "님이 들어왔습니다.";
            }
            else if(cmd.equals("Leave")){
                resultMsg[i] = nicknameByUid.get(uid) + "님이 나갔습니다.";
            }
            else{
                continue;
            }
        } // 이러면 resultMsg 배열에서 record[i]가 Change였던 인덱스는 null로 남아있음.
        
        // 3. null 제거하고 반환
        return Arrays.stream(resultMsg).filter(s -> s != null).toArray(String[]::new); 
    }
}