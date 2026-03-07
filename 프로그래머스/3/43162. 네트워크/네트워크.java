/*
PGS 43162 - 네트워크
[조건]
1) computer[i][i]는 항상 1이다. 
2) computer[i][j] = computer[j][i]이다. 
[접근]
1) 전체 컴퓨터를 순회하면서, 아직 방문하지 않은 컴퓨터를 시작점으로 bfs를 수행한다.
2) bfs 한 번이 네트워크 1개를 전부 탐색하는 것
3) 따라서 아직 방문하지 않은 컴퓨터를 새로 만날 때마다 bfs를 시작하고,
   bfs가 끝날 때마다 네트워크 개수를 1 증가시킨다.
4) 모든 컴퓨터를 확인한 뒤, 누적합한 네트워크 개수를 리턴한다.

[느낀점]
지금 computer[i][i]는 항상 1이고, i=j 대칭이므로
i>j 인경우만 고려해도 될듯하다.
그리고 굳이 그래프를 사용하지 않고 computers 배열 자체를 바로 쓰는게 더 간단했을 것 같긴 하다.
*/

import java.util.*;
  
class Solution {
    private boolean[] visitedGlobal;
    private List<Integer>[] graph;
    private int netCount = 0;

    public int solution(int n, int[][] computers) {  
        visitedGlobal = new boolean[n];
        
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // graph 채움
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && computers[i][j] == 1) {
                    graph[i].add(j);
                }
            }
        }

        // 아직 방문하지 않은 컴퓨터를 시작점으로 bfs
        for (int i = 0; i < n; i++) {
            if (!visitedGlobal[i]) {
                bfs(i);
            }
        }

        return netCount;
    }

    private void bfs(int s) {
        Queue<Integer> q = new ArrayDeque<>();

        q.add(s);
        visitedGlobal[s] = true;

        while (!q.isEmpty()) {
            int c = q.poll();

            for (int n : graph[c]) {
                if (visitedGlobal[n]) continue;

                visitedGlobal[n] = true;
                q.add(n);
            }
        }

        // 하나의 네트워크 탐색 완료했으니 카운트함
        netCount++;  
    }
}