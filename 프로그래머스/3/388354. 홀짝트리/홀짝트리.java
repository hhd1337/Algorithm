/*
- 루트 노드 설정안된 트리들이 모인, 포레스트가 있음
- 모든 노드는 다른 번호이고, 각 노드는 다음 중 하나.
  1. 홀수 (홀수, 자식 홀수개)
  2. 짝수 (짝수, 자식 짝수개)
  3. 역홀수 (홀수, 자식 짝수개)
  4. 역짝수 (짝수, 자식 홀수개)
- 트리는 다음중 하나가 될 수 있음. 
  1. 홀짝트리: 홀수 노드와 짝수노드로만 구성됨
  2. 역홀짝트리: 역홀수노드와 역짝수노드로만 구성됨

*/

import java.util.*;

class Solution {
    public int[] solution(int[] nodes, int[][] edges) {
        int nodesLength = nodes.length;
        // 노드 번호가 연속이 아니므로, 번호를 인덱스로 바꿔주기 위한 맵
        // key: 노드 번호(nodes[i]) value: 그 노드가 저장된 인덱스(i)
        Map<Integer, Integer> indexByNodeId = new HashMap<>();

        for (int i = 0; i < nodesLength; i++) {
            indexByNodeId.put(nodes[i], i);
        }

        // 인접 리스트 배열 생성
        List<Integer>[] graph = new ArrayList[nodesLength];
        for (int i = 0; i < nodesLength; i++) {
            graph[i] = new ArrayList<>();
        }

        int[] edgeCount = new int[nodesLength];

        // 간선을 보고 노드 연결 (각 노드별 인접노드 추가)
        for (int[] edge : edges) {
            int n1 = indexByNodeId.get(edge[0]);
            int n2 = indexByNodeId.get(edge[1]);

            graph[n1].add(n2);
            edgeCount[n1]++; // 노드에 간선이 하나 붙었으니 edgeCount++

            graph[n2].add(n1);
            edgeCount[n2]++;
        }

        // 각 트리를 BFS로 하나씩 처리
        boolean[] visited = new boolean[nodesLength];
        ArrayDeque<Integer> q = new ArrayDeque<>();

        int oddEvenTreeCount = 0;
        int revOddEvenTreeCount = 0;

        for (int start = 0; start < nodesLength; start++) {
            if (visited[start]) {
                continue;
            }

            visited[start] = true;
            q.add(start);

            int treeSize = 0;
            int oddOddOrEvenEvenNodeCount = 0; // 트리에서 노드번호,자식노드수가 홀,홀 이거나 짝,짝인 노드 개수

            while (!q.isEmpty()) {
                int v = q.poll();
                treeSize++; // 노드 하나를 방문했으므로 트리 크기 +1

                boolean nodeNumIsOdd = (nodes[v] % 2 != 0);
                boolean childNodeCountIsOdd = (edgeCount[v] % 2 != 0);

                if (nodeNumIsOdd == childNodeCountIsOdd) {
                    oddOddOrEvenEvenNodeCount++;
                }

                for (int nx : graph[v]) {
                    if (!visited[nx]) {
                        visited[nx] = true;
                        q.add(nx);
                    }
                }
            }
            // 홀짝트리 가능조건: oddOddOrEvenEvenNodeCount가 1개면 그걸 루트로 해서 홀짝트리 만듦
            if (oddOddOrEvenEvenNodeCount == 1) {
                oddEvenTreeCount++;
            }
            // 역홀짝트리 가능조건: treeSize-oddOddOrEvenEvenNodeCount가 1개면 그걸 루트로 해서 역홀짝트리 만듦
            if (treeSize - oddOddOrEvenEvenNodeCount == 1) {
                revOddEvenTreeCount++;
            }
        }

        return new int[]{oddEvenTreeCount, revOddEvenTreeCount};
    }
}
