import java.util.*;

/*
등산코스 : 출입구는 처음과 끝에 한 번씩, 산봉우리는 한 번만 포함
intensity 최소가 되는 등산코스를 구해야 함. (여러개면 산봉우리 번호가 가장 낮은 등산코스 선택)

이 문제는 사실상 “gate → summit”만 minimax로 최소화하면 끝이다.
왜냐면 무방향 그래프에서 summit에서 같은 경로로 되돌아오면 intensity가 변하지 않기 떄문.
*/

class Solution {
    private Node[] nodes;
    private List<Edge>[] adj;
    private int[] dist;
    private PriorityQueue<State> pq;

    private static class State {
        int node; // 현재 노드
        int intensity;// 현재 노드까지의 intensity
        
        State(int node, int intensity){
            this.node = node;
            this.intensity = intensity;
        }
    }

    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        init(n, paths, gates, summits);
        runMinimaxDijkstra(n, gates);
        return pickAnswer(summits);
    }

    private void init(int n, int[][] paths, int[] gates, int[] summits){
        // 노드 생성
        nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++){
            nodes[i] = new Node(i);
            nodes[i].type = NodeType.NORMAL;
        }

        // gate, summit 타입 넣기
        for (int i : gates){
            nodes[i].type = NodeType.GATE;
        }
        for (int i : summits){
            nodes[i].type = NodeType.SUMMIT;
        }

        // 그래프 구성
        adj = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) adj[i] = new ArrayList<>();

        for (int[] path : paths) {
            int node1 = path[0];
            int node2 = path[1];
            int length = path[2];

            // 양방향 인접 리스트
            adj[node1].add(new Edge(node2, length));
            adj[node2].add(new Edge(node1, length));
        }
    }

    private void runMinimaxDijkstra(int n, int[] gates){
        dist = new int[n + 1]; // 어떤 출입구에서 x까지 갈 때, 최소 intensity 값
        Arrays.fill(dist, Integer.MAX_VALUE); // 처음엔 어디든 최선값을 모르니 무한대로 채움

        pq = new PriorityQueue<>((a, b) -> a.intensity - b.intensity); //intensity가 작은 상태부터 꺼내기 위한 도구

        // 출입구가 여러 개이므로, 모든 출입구를 시작점으로 동시에 시작한다
        // 각 gate에서 gate까지 intensity는 0 (아직 간선을 안 탔으니까)
        for(int gate : gates){
            dist[gate] = 0;
            pq.add(new State(gate, 0));
        }

        while(!pq.isEmpty()){
            State cur = pq.poll();
            int curNode = cur.node;
            int curIntensity = cur.intensity;

            // PQ에 들어있던 오래된(더 나쁜) 상태면 스킵
            if(curIntensity != dist[curNode]) continue;

            // summit은 도착점만 허용: 중간 경유지로 쓰지 않게 확장 중단
            if(nodes[curNode].isSummit()) continue;

            // 인접 정점 relax
            for(Edge e : adj[curNode]){
                int nextNode = e.to;
                int edgeLength = e.length;

                // 중간에 다른 출입구 방문 금지: gate로 들어가는 이동 차단
                if(nodes[nextNode].isGate()) continue;
                
                // nextNode까지의 intensity는 '지금까지 경로의 intensity'와 '이번 간선길이' 중 큰 값
                // 즉, 경로에서 가장 긴 구간 시간이 intensity로 유지된다.
                int nextIntensity = Math.max(curIntensity, edgeLength);

                if(dist[nextNode] > nextIntensity){
                    dist[nextNode] = nextIntensity;
                    pq.add(new State(nextNode, nextIntensity));
                }
            }
        }
    }
    
    // 정답 규칙:
    // 1) intensity가 최소인 summit 선택
    // 2) intensity가 같으면 summit 번호가 더 작은 것 선택
    // summits를 오름차순 정렬해두면, 2번 규칙이 자동으로 만족되게 만들 수 있다.
    private int[] pickAnswer(int[] summits){
        Arrays.sort(summits); // 동률이면 summit 번호 작은 것 우선

        int bestSummit = -1;
        int bestIntensity = Integer.MAX_VALUE;
        
        // 모든 summit 후보를 보면서 가장 좋은 것을 고른다
        // dist가 같을 때는 갱신하지 않음: summits가 오름차순이므로 번호 작은 summit이 자연스럽게 유지되도록.
        for(int summit : summits){
            if(dist[summit] < bestIntensity){ 
                bestIntensity = dist[summit];
                bestSummit = summit;
            }
        }
        return new int[]{bestSummit, bestIntensity}; // [산봉우리 번호, 최소 intensity] 반환
    }
    
}

class Node {
    int num;
    NodeType type;

    public Node(int num){
        this.num = num;
    }
    boolean isGate(){
        return type == NodeType.GATE; 
    }
    boolean isSummit(){
        return type == NodeType.SUMMIT; 
    }
}

class Edge {
    int to;
    int length;

    public Edge(int to, int length) {
        this.to = to;
        this.length = length;
    }
}

enum NodeType {
    NORMAL, GATE, SUMMIT;
}
