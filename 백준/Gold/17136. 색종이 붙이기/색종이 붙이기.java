/*
BOJ 17136 - 색종이 붙이기

[조건]
1)모든 1을 덮어야 함, 0은 덮으면 안됨
2)사용하는 색종이 수의 최소를 구해야 한다
3)불가능하면 -1

[접근]
1. 목표: 최소개수
→ 모든 1을 덮는 데 필요한 색종이의 최소 개수
2. [BFS/다익스트라] 최단거리인가? X
→ 이동/거리/시간 같은 그래프 최단경로 문제 아님.
3. [백트래킹] 하나 선택하고 다음재귀로 넘김, 재귀 끝나면 되돌려야 하나? O 
→ 색종이 크기를 여러 가지로 붙여볼 수 있고, 붙였다가 막히면 다른 크기로 다시 시도해야 함
4. [완전탐색] N이 작아서 전부 시도 가능한가? △
→ 가능하지만 경우 많음 → 가지치기 필요
5. [DP] 이전 답으로 현재 답을 만들 수 있나? X
6. [그리디] 지금 최선 선택이 항상 맞는가? X
→ (0,0)부터 큰 색종이 들어가는 곳에 먼저 붙이는 것이 항상 최적해는 아닐 것.
7. [DFS/BFS/유니온 파인드] 연결 여부/방문이 핵심인가? X
8. [이분탐색] 가능/불가능 판정으로 답을 좁히는가? X
9. [누적합/투 포인터/슬라이딩 윈도우] 연속 구간/부분합이 핵심인가? X
=> 알고리즘 후보: 백트래킹

1)아직 덮지 않은 첫번째 1을 찾는다.
2)그 위치에 5*5 ~ 1*1을 붙여본다.
3)붙이고 재귀
4)떼고 복구

[느낀점]
처음에는 이렇게 큰 색종이 부터 덮이는 곳에 무조건 붙이고 넘어가는, 그리디방식으로 풀었다.
1)모든자리에서, 5*5색종이를 갖다 대봄, 모두 1로만 돼있으면 붙임.
2)다시 처음으로 돌아와서, 모든자리에 4*4색종이를 갖다 대봄, 모두 1로만 돼있으면 붙임.
3)다시 처음으로 돌아와서, 모든 자리에 3*3색종이 갖다 대봄, 모두 1로만 되어있으면 붙임.

현재 이렇게 접근방식이 틀린 경우가 많다. 
문제를 푸는 핵심 알고리즘을 잡아내지 못하는 것 같다.
(특히 많은 문제를 그리디로 풀려는 경향이 있다.)
따라서 앞으로 구현 전에 "왜 이 알고리즘인지" 따져보고, 확신이 없으면 구현을 시작하지 말아야겠다. 
*/

import java.io.*;
import java.util.*;

public class Main {
    static int[][] map = new int[10][10];
    static int[] paperCount = {-1,5,5,5,5,5};
    static int minUsedCount = Integer.MAX_VALUE;
    
    public static void main(String[] args) throws Exception {
        init();
        backtracking(0);
        
        if(minUsedCount == Integer.MAX_VALUE){
            System.out.println(-1);
        }else{
            System.out.println(minUsedCount);
        }
    }
    
    private static void backtracking(int usedCount){
        //[가지치기]
        if (usedCount >= minUsedCount) return;
        
        //[다음 분기지점 찾기]아직 덮지 않은 첫번째 1 찾기
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (map[i][j] == 1) {
                    
                    //[매 재귀의 선택지]: 5*5 ~ 1*1 중 붙일 수 있는 선택지 다 해본다.
                    for(int size=5; size>=1; size--){
                        if(paperCount[size]>0 && canAttachHere(i,j,size)){
                            //[선택]먼저 이 재귀에서는 선택지 하나를 선택하고 다음 재귀로 넘긴다.(붙임)
                            attach(i, j, size, 0); // 붙였으니 1->0
                            paperCount[size]--;
                            
                            //[다음단계(다음재귀)로 넘김]
                            backtracking(usedCount+1);
                            
                            //[선택 복구]이 재귀가 선택했던 선택을 복구하고, 
                            attach(i, j, size, 1); // 붙였던거 다시 복구 0->1
                            paperCount[size]++; // 색종이 수도 복구
                        }
                    }
                    // 첫번째 1 하나에 대해서만 분기하도록 해줌.
                    // 첫번째 1에대한 모든 선택지 시도했으므로, 나머지는 다음재귀/상위재귀가 처리하도록 종료
                    return;
                }
            }
        }
        // 끝까지 for문을 다 돌았는데 1을 한번도 못찾았다: 전부 덮은 상태.
        minUsedCount = Math.min(minUsedCount, usedCount);
    }
    
    private static void attach(int r, int c, int size, int value) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[r + i][c + j] = value;
            }
        }
    }
    
    private static boolean canAttachHere(int r, int c, int size){
        if (r+size > 10 || c+size > 10) return false;
        
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(map[r+i][c+j]==0){
                    return false;
                }
            }
        }
        return true;
    }
    
    private static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        // map 초기화
        for(int i=0; i<10; i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<10; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }
}



// [처음 오답풀이 - 그리디로 잘못 접근]
// 큰 색종이부터 가능한 곳에 전부 붙이는 그리디 전략이 항상 최적해를 만들지 않음.
/*
import java.io.*;
import java.util.*;

public class Main {
    static Integer[] countNbyN = {null, 5,5,5,5,5}; // idx:N value:count
    static int[][] map = new int[10][10];
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        // map 초기화
        for(int i=0; i<10; i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<10; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        // 모든 좌표 5바퀴 돌면서 5*5부터 1*1 대봄.  
        int N = 5;
        while(countNbyN[N] > 0){
            for(int i=0; i<11-N; i++){ // 검사는 10*10 전체 좌표에서 다 보는게 아니라 (10-N+1)*(10-N+1) 까지만 보면 된다.
                for(int j=0; j<11-N; j++){
                    if(isNbyNPossibleHere(i,j,N)){
                        updateMap(i,j,N);
                        countNbyN[N]--;
                    }
                }
            }
            if(N>1 && countNbyN[N]==0){
                N--;
            }
        }
        
        if(allOneCovered()){        
            System.out.println(calPaperCount());
        }else{
            System.out.println(-1);
        }
        
    }
    
    private static int calPaperCount(){
        int remain = 0;
        for(int i=1; i<=5; i++){
            remain += countNbyN[i];
        }
        return 25-remain;
    }
    
    private static boolean allOneCovered(){
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(map[i][j]==1){
                    return false;
                }
            }
        }
        return true;
    }
    
    private static void updateMap(int r, int c, int N){
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                map[r+i][c+j]=0;
            }
        }
    }
    
    private static boolean isNbyNPossibleHere(int r, int c, int N){
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                if(map[r+i][c+j]==0){
                    return false;
                }
            }
        }
        return true;
    }
}
*/