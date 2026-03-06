/*
BOJ 17471 - 게리맨더링
[조건]
1)선거구는 최소 1개의 구역(노드) 포함.
2)한 선거구 안의 구역은 모두 연결되어있어야 함.
[접근]
tree면 간선 하나씩 끊어보면서 두 그룹으로 분리하고, 인구차이 계산 할 텐데 
graph라 간선하나를 끊는다고 두 그룹으로 분리되지 않는다.
내 문제 접근은 이랬다.
1)A,B그룹으로 나눔 (각 그룹은 1개 이상 구역 포함)
2)A가 모두 연결됐나 확인
3)B가 모두 연결됐나 확인
4)다 만족 시 인구차이 계산, 업데이트
[느낀점]
개체들을 적은 개수의 그룹으로 분류할 때,
'subset 재귀'를 사용해 완전탐색 할 수 있다는걸 배웠다.
같은 상황 다른 풀이로는 '비트마스크' 사용이 있던데,
개체들의 선택 상태를 비트로 표현하여 모든 경우를 탐색하는 방법이다.
주말에 파 봐야겠다.
*/

import java.util.*;
import java.io.*;

public class Main {
    private static int N; // 구역 개수
    private static int[] pop; // idx:구역번호 value:구역인구
    private static List<Integer>[] graph; // idx:구역번호 value:각 구역 인접 구역번호리스트
    private static boolean[] isGroupA; // idx:구역번호 value: A그룹 true/B그룹 false
    private static int popDiff = Integer.MAX_VALUE;
    
    public static void main(String[] args) throws Exception{
        init();
        subSet(1); // subSetAndUpdatePopDiff(1); 로 하고싶었는데 재귀호출부가 좀 이상해서 놔둠
        
        if(popDiff == Integer.MAX_VALUE){
            System.out.println(-1);
        } else {
            System.out.println(popDiff);
        }
    }
    
    // 이번 노드를 true로 두고 파생되는 모든 세계관을 먼저 전부 탐색한 다음, 돌아와서 false로 두는 세계관들을 전부 탐색.
    private static void subSet(int idx){
        // 모든 경우들의 종료시점: 이때 A와 B가 각각 연결되어 있는지 확인, 인구차이 업데이트
        if(idx == N+1){ // isGroupA[N] 까지는 =true/false 설정해야 하므로 idx==N+1일때 종료 
            if(isAConnected() && isBConnected()){
                popDiff = Math.min(popDiff, calPopDiff());
            }
            return;
        }
        
        isGroupA[idx] = true; // 이번노드 A그룹으로 둠
        subSet(idx+1); // 세계관 전부 탐색
        
        isGroupA[idx] = false; // 이번노드 B그룹으로 둠
        subSet(idx+1); // 세계관 전부 탐색(검사)
    }
    
    private static int calPopDiff(){
        int popA = 0;
        int popB = 0;
        
        for(int i=1; i<=N; i++){
            if(isGroupA[i]) popA += pop[i];
            if(!isGroupA[i]) popB += pop[i];
        }
        
        return Math.abs(popA - popB);
    }
    //bfsA
    private static boolean isAConnected(){
        Queue<Integer> q = new ArrayDeque<Integer>();
        boolean visited[] = new boolean[N+1];
        int aCount = 0; // A 그룹에 속한 전체 노드 개수를 세기 위한 변수
        int visitedCount = 1; // 실제 방문한 노드 수
        
        // A의 아무 점 시작점으로 해서 큐에넣기
        int s = -1; 
        for(int i=1; i<=N; i++){
            if(isGroupA[i]){
                aCount++; // A 그룹 노드 수 카운트
                s = i; // 그냥 계속 대입하다가 마지막 A부터 탐색시작
            }
        }
        if(s == -1) return false;
        q.add(s); 
        visited[s] = true; // 시작점 방문처리
        
        while(!q.isEmpty()){
            int c = q.poll();
            
            for(int n : graph[c]){
                // 1) 범위검사
                // if(n < 1 || n > N) continue;
                // 2) 방문검사
                if(visited[n]) continue;
                // 3) 벽검사: 필요 없음
                // 4) 문제 조건: A 그룹 노드만 이동 가능
                if (!isGroupA[n]) continue;
                
                // final: 방문처리, 큐에 추가
                visited[n] = true;
                q.add(n);
                visitedCount++;
            }
        }
        //실제 방문한 노드 수가 A 그룹에 속한 전체 노드 개수와 같은지 확인. 
        //BFS가 A 그룹 노드만 따라가도록 짜여 있으므로 충분한 검증이 됨.
        if(aCount == visitedCount){
            return true;
        }else{
            return false;
        }
    }
    //bfsB
    private static boolean isBConnected(){
        Queue<Integer> q = new ArrayDeque<Integer>();
        boolean visited[] = new boolean[N+1];
        int bCount = 0, visitedCount = 1;
        
        // B의 아무 점 시작점으로 해서 큐에넣기
        int s = -1;
        for(int i=1; i<=N; i++){
            if(!isGroupA[i]){
                s = i;
                bCount++;
            }
        }
        if(s == -1) return false;
        q.add(s);
        visited[s] = true; // 시작점 방문처리
        
        while(!q.isEmpty()){
            int c = q.poll();    
            for(int n : graph[c]){
                // 1) 범위검사: 필요없음
                // if(n < 1 || n > N) continue;
                // 2) 방문검사
                if(visited[n]) continue;
                // 3) 벽검사: 필요 없음
                // 4) 문제조건 : B노드로만 이동 가능.
                if(isGroupA[n]) continue;
                
                // final: 방문처리, 큐에 추가
                visited[n] = true;
                q.add(n);
                visitedCount++;
            }
        }
        if(bCount == visitedCount){
            return true;
        }else{
            return false;
        }
        
    }
    
    private static void init() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine().trim());
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        pop = new int[N+1];
        for(int i=1; i<=N; i++){
            pop[i] = Integer.parseInt(st.nextToken());
        }
        
        graph = new ArrayList[N+1];
        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for(int i=1; i<=N; i++){
            st = new StringTokenizer(br.readLine()); // 이 줄(i구역 정보) 읽음
            int len = Integer.parseInt(st.nextToken());
            
            for(int j=0; j<len; j++){
                graph[i].add(Integer.parseInt(st.nextToken()));
            }
        }
        
        isGroupA = new boolean[N+1];
    }
}