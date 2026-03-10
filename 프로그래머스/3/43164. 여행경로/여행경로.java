/*
PGS 43164 - 여행경로
[조건]
1) 항상 ICN에서 시작
2) 주어진 항공권은 모두 사용해야 함
3) 가능한 경로가 2개 이상이면 알파벳 순서가 앞서는 경로를 반환

[접근]
공항수가 최대 만개임. tickets는 최대 몇개인지 모름.
그런데 일단 모든 티켓이 연결된다는 걸 봐서, 
무식하게 모든 티켓 배치를 완전탐색하는 문제는 아닌것 같음. 주어진 정보를 못쓰는 느낌.

1) DFS, 모든 가능한 경우 완전탐색 후 -> 알파벳순서로 가장 먼저인 경로 반환.
DFS로 ICN에서 시작해서, 계속 재귀호출 하다가 
종료조건으로 티켓이 모두 쓰였는지 확인.
DFS 중에 tickets 경로를 쭉 이어붙여서 Stirng 배열로 가지고 있음.
그리고 종료했는데 티켓이 모두 쓰였다면, 해당 Stirng 배열을 List<String[]> possibleWays에 추가

DFS 종료 후 List.length > 1이면 for 루프 돌면서 인덱스별로 문자열 비교하다가, 
다른 문자열이 나오면 사전순서대로 해서 경로 배열 하나를 특정.
해당 경로배열을 반환

2) DFS순서를 항상 사전순으로 맞춰놓으면, 첫번째 찾은 경로가 정답.
근데 가능한 정답 후보를 다 모아놓고 나중에 고르는 건 뭔가 아쉬움.
DFS순서를 사전순으로 하도록 조정하면 될듯.

[느낀점]
이 문제는,
1) 그래프에 티켓의 인덱스를 저장해야 한다.
2) 공항 기준 방문체크(visited)가 아니라 티켓을 기준으로 방문체크를 해야한다
는 걸 생각하기 쉽지 않았음.
일반 dfs에 비해 dfs 구성요소들의 타입이 특이해서 직관적으로 풀기 어려웠던 문제.
결과를 사전순으로 반환해야 할 때 DFS 탐색 순서를 사전순으로 하도록 하는 것도 외워둘 만한 것 같다.
*/
import java.util.*;

class Solution {
    private Map<String, List<Integer>> graph = new HashMap<>(); // key: 시작항공 문자열, value: 시작항공출발 티켓 인덱스 리스트.
    private boolean[] visited; // i번째 티켓 사용여부
    private String[] path;
    private String[][] tickets;
    
    public String[] solution(String[][] tickets) {
        this.tickets = tickets;
        visited = new boolean[tickets.length];
        path = new String[tickets.length + 1]; // 티켓 개수 + 처음 ICN이 곧 경로 길이

        // 그래프 만들기: 출발 공항 -> 티켓 인덱스들
        for (int i = 0; i < tickets.length; i++) {
            String from = tickets[i][0];
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(i);
        }

        // 사전순 DFS를 위해 도착 공항 기준 정렬
        for (String key : graph.keySet()) {
            graph.get(key).sort((a, b) -> tickets[a][1].compareTo(tickets[b][1]));
        }

        path[0] = "ICN";
        dfs("ICN", 0);

        return path;
    }
    
    private boolean dfs(String cur, int usedCount) {
        // 모든 티켓을 다 썼으면 path가 곧 정답
        if (usedCount == tickets.length) {
            return true;
        }

        // 현재 공항에서 출발 가능한 티켓이 없으면 실패
        if (!graph.containsKey(cur)) {
            return false;
        }

        for (int idx : graph.get(cur)) {
            if (visited[idx]) continue;

            visited[idx] = true;

            String next = tickets[idx][1];
            path[usedCount + 1] = next;

            if (dfs(next, usedCount + 1)) {
                return true;
            }

            visited[idx] = false;
        }

        return false;
    }
        
}