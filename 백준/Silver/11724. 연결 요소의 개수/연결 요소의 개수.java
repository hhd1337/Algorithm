/*
BOJ 11724 - 연결 요소의 개수
[조건]
1)방향 없는 그래프
[접근]
DFS, BFS 둘다 가능하지만, 덜 숙련된 DFS로 풀어본다.
[느낀점]
그래프 탐색 DFS(재귀,스택), BFS(visited, dist) 기본코드들을 눈감고도 칠 수 있을 정도로 더 숙달해야겠다.
지금 다 외워서 적긴 하는데 원리를 생각하면서 더듬더듬 작성한다.
그냥 코드를 통째로 달달 외워야 할 것 같다.
*/

import java.io.*;
import java.util.*;

public class Main {
    private static List<Integer>[] graph; // idx: 노드번호 value: 인접노드번호 리스트
    private static int N;
    private static int M;
    private static boolean[] visited;
    private static ArrayDeque<Integer> stack = new ArrayDeque<>();
    private static int startNode = 1; // startNode가 절대 뒤로가지 않도록 함
    private static int componentCount = 0;
    
    public static void main(String[] args) throws Exception {
        init();
        
        //visited가 모두 true일때 까지 계속 dfs
        while(!everyNodeVisited()){
            dfs(startNode);
        }
        
        System.out.println(componentCount);
    }
    
    private static boolean everyNodeVisited(){
        for(int i=startNode; i<=N; i++){ // startNode 이전까지는 무조건 다 방문한것.
            if(visited[i] == false){
                startNode = i; // 다음 dfs인자로 넣을 시작점노드 
                return false;
            }
        }
        return true;
    }
    
    private static void dfs(int s){
        stack.push(s);
        visited[s] = true;
        
        while(!stack.isEmpty()){
            int c = stack.pop();
            
            for(int n : graph[c]){
                if(visited[n]) continue; // 방문검사
                
                stack.push(n);
                visited[n] = true;
            }
        }   
        componentCount++;
    }
    
    private static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        
        // 그래프 생성
        graph = new List[N+1];
        for(int i=1; i<=N; i++){
            graph[i] = new ArrayList<Integer>();
        }
        
        // 그래프 초기화
        for(int i=0; i<M; i++){
            st = new StringTokenizer(br.readLine());
            
            int n1 = Integer.parseInt(st.nextToken());
            int n2 = Integer.parseInt(st.nextToken());
            
            graph[n1].add(n2);
            graph[n2].add(n1);
        }
        
        // 방문배열 초기화
        visited = new boolean[N+1];
    }
}