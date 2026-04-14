/*
BOJ 2573 - 빙산

[조건]
1)높이는 1년마다 동서남북의 0칸 개수만큼 줄어든다
2)바닷물은 호수처럼 빙산에 둘러싸여 있을 수도 있다
3)빙산이 두 덩어리 이상으로 분리되는 최소 년수 구하라.
4)두덩어리 이상 분리안되고 끝까지 한덩어리로 있다가 다녹으면 0출력.

[접근]
1)지금 map의 빙산 수를 구함.
=> map전체에 그래프 여러개 다 도는법: 
map완전탐색하면서 visited==false인 빙산나오면 거기서 bfs돌림, 전역 visited에 빙산노드 방문처리
2)빙산수 2이상이면 바로 년수 출력 / 빙산수 0이면 바로 0출력
3)빙산수 1이면 map업데이트, 년수 ++ 하고 다음루프로 넘어감
(빙산녹임=>이때 빙산수1이면 그래프 1개라는 뜻이므로 bfs돌려도 되긴 하지만, 구현상 이중for문으로 하면 실수 없고 편할듯.)

[느낀점]
map전체에 그래프가 여러개 있을 수도 있는 구조에서는 
map의 모든 노드를 완전탐색(여기서는 격자map이라 이중for문으로) 하며 
!visited && !null인 노드가 나오면 그 노드를 시작점으로 bfs를 돌리고, 
또 다음노드부터 끝노드까지 완전탐색으로 돌며 !visited && !null인 노드를 찾는식으로
그래프 모두를 순회할 수 있다는 패턴을 확실히 익혔다. 
그 과정에서 bfs를 한 횟수가 map 내 그래프의 개수라는 것도 확실히 알았다. 
*/

import java.io.*;
import java.util.*;

public class Main {
    static int R, C; // 행, 열
    static int[][] map;
    static int yearCount = 0;
    
    static int[] dr = {0,0,1,-1};
    static int[] dc = {-1,1,0,0};
    
    static boolean[][] visited;
    
    public static void main(String[] args) throws IOException {
        init();
        
        while(true){
            int icebergCount = countIceberg();
            
            if(yearCount > 0 && icebergCount >= 2){
                System.out.println(yearCount);
                break;
            }
            if(icebergCount == 0){
                System.out.println(0);
                break;
            }
            
            // visited 초기화. 기존 visited배열은 GC대상이긴 하지만 메모리 사용에는 잡힐 듯..
            visited = new boolean[R][C];
            
            // map 업데이트
            updateMap();
            yearCount++;
        }
        
    }
    
    private static void updateMap(){
        int[][] nextMap = new int[R][C];
        
        for(int r=0; r<R; r++){
            for(int c=0; c<C; c++){
                if(map[r][c]==0) continue; // 0인칸은 그냥 0이므로 건너뛰자
                
                int zeroCount = 0;
                
                for(int i=0; i<4; i++){
                    int nr = r+dr[i];
                    int nc = c+dc[i];
                    if(inRange(nr,nc) && map[nr][nc] == 0) zeroCount++;
                }
                nextMap[r][c] = Math.max(0, map[r][c] - zeroCount);
            }
        }
        map = nextMap;
    }
    
    private static boolean inRange(int r, int c){
        return r>=0 && c>=0 && r<=R-1 && c<=C-1;
    }
    
    // map 내 그래프 개수 세는 메서드
    private static int countIceberg(){
        int icebergCount = 0;
        
        for(int r=0; r<R; r++){
            for(int c=0; c<C; c++){
                if(!visited[r][c] && map[r][c] != 0){
                    bfs(r,c);
                    icebergCount++;
                }
            }
        }
        return icebergCount;
    }
    
    // 빙산 한개 방문처리용 bfs
    private static void bfs(int sr, int sc){
        Queue<int[]> q = new ArrayDeque<>();
        
        visited[sr][sc] = true;
        q.offer(new int[]{sr,sc});
        
        while(!q.isEmpty()){
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];
            
            for(int i=0; i<4; i++){
                int nr = r+dr[i];
                int nc = c+dc[i];
                
                if(!inRange(nr, nc)) continue; // 범위검사
                if(visited[nr][nc] == true) continue; // 방문검사
                if(map[nr][nc] == 0) continue; // 벽(물)검사
                
                visited[nr][nc] = true;
                q.offer(new int[]{nr,nc});
            }
        }
    }
    
    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        
        map = new int[R][C];
        for(int i=0; i<R; i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<C; j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        visited = new boolean[R][C];
    }
}