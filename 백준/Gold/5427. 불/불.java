/*
BOJ 5427 - 불
[조건]
1)불은 동서남북 방향으로 인접한 빈 공간으로 퍼짐.
2)벽에는 불이 안붙음.
3)상근이는 동서남북 한칸 이동가능, 1초걸림
4)상근이는 벽, 불칸, 이제 불 붙으려는 칸 이동 못함
[접근]
- 불이 한점에서 시작해서 확산: BFS
[느낀점]
"불이 각 칸에 언제 도착하는지를 먼저 BFS로 구해놓고, 
그 다음 상근이 이동 BFS를 하면서
상근 도착시간 < 불 도착시간 인 경우만 이동한다" 이 생각을 쉽게 떠올리지 못했다.
처음에는 상근이 BFS를 돌리면서 중간중간 매초마다 불의 확산을 map에 반영해야 하나 고민했는데,
언제 불을 갱신할지 시점을 잡기 되게 어려웠다.
'불의 도착 시간표를 먼저 만든다'라고 생각하는 것이 훨씬 깔끔했다.
그리고 불 도착 시간표를 만들면서, BFS에서 시작점이 여러개이면 큐에 모든 시작점을 다 넣고 시작하면 된다는 것도 배웠다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static int[] dr = {1,-1,0,0};
    static int[] dc = {0,0,-1,1};
    static int escapeTime = Integer.MAX_VALUE;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int testCaseCount = Integer.parseInt(br.readLine().trim());
        
        StringTokenizer st;
        for(int count=0; count<testCaseCount; count++){
            escapeTime = Integer.MAX_VALUE;
            
            st = new StringTokenizer(br.readLine());
            // map 초기화
            int w = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
           
            char[][] map = new char[h][w];
            int sr = -1, sc = -1;
            for(int i=0; i<h; i++){
                String row = br.readLine().trim();
                for(int j=0; j<w; j++){
                    char ch = row.charAt(j);
                    map[i][j] = ch;
                    if(ch == '@'){
                        sr = i;
                        sc = j;
                    }
                }
            }
            // 불 도착 시간표
            int[][] fireTimeMap = calFireTime(map); 

            bfsEscape(map, fireTimeMap, sr, sc);
            printResult();
        }
    }
    
    private static int[][] calFireTime(char[][] map) {
        int h = map.length;
        int w = map[0].length;

        Queue<int[]> q = new ArrayDeque<>();
        int[][] fireTimeMap = new int[h][w]; //변수명 dist라 안하고 의미상 fireTimeMap으로 함
        for (int[] arr : fireTimeMap) {
            Arrays.fill(arr, -1);
        }

        //모든 불 시작점을 큐에 넣고 시작
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                if (map[r][c] == '*') {
                    q.add(new int[]{r, c});
                    fireTimeMap[r][c] = 0;
                }
            }
        }

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];

            for (int i = 0; i < 4; i++) {
                int nr = r+dr[i];
                int nc = c+dc[i];
                
                if (nr<0 || nr>=h || nc<0 || nc>=w) continue; //범위검사
                if (map[nr][nc] == '#') continue; //벽검사
                if (fireTimeMap[nr][nc] != -1) continue; //불이 이미 도착했으면 넘어감

                fireTimeMap[nr][nc] = fireTimeMap[r][c] + 1;
                q.add(new int[]{nr, nc});
            }
        }
        return fireTimeMap;
    }
    
    private static void printResult(){
        if(escapeTime == Integer.MAX_VALUE){
            System.out.println("IMPOSSIBLE");
            return;
        }
        System.out.println(escapeTime);
    }
    
    private static void bfsEscape(char[][] map, int[][] fireTimeMap, int sr, int sc){
        Queue<int[]> q = new ArrayDeque<>();
        int[][] dist = new int[map.length][map[0].length];
        for(int[] arr : dist){
            Arrays.fill(arr,-1);// 미방문 시 -1
        }
        
        //시작점 처리
        q.add(new int[]{sr,sc});
        dist[sr][sc] = 0;
        
        while(!q.isEmpty()){
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];
            
            if(r==0 || r==map.length-1 || c==0 || c==map[0].length-1) {
                escapeTime = dist[r][c] + 1; //예시보면 한번 더 해서 맵 밖으로 나가는것 까지 더해줘야함
                break;
            }
            
            for(int i=0; i<4; i++){
                int nr = r+dr[i];
                int nc = c+dc[i];
                
                if(dist[nr][nc] != -1) continue; //방문검사
                if(map[nr][nc] == '#') continue; //벽검사
                //불검사: (nr,nc)에 불 도착시간 > 상근 도착시간 인지 검사
                if(fireTimeMap[nr][nc] != -1 && dist[r][c]+1 >= fireTimeMap[nr][nc]) continue; 
                
                q.add(new int[]{nr,nc});
                dist[nr][nc] = dist[r][c] + 1;
            }
        }
    }
    
}