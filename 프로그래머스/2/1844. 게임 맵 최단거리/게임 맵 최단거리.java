/*
PGS 1844 - 게임 맵 최단거리

[조건]
1) 시작 시 캐릭터는 (0,0)에 위치함, 도달지점은 (n-1, m-1)
2) 0은 벽이 있는 자리, 1은 벽이 없는 자리

[접근]
격자 그래프에서의 최단거리 bfs문제.

[느낀점]
격자 그래프에서의 최단거리 bfs문제 중 아주 기본문제였다.
bfs 코드를 작성을 더 숙달시키면 더 빨리 풀 수 있겠다.

*/

import java.util.*;

class Solution {
    private int n, m; 
    private int[] dr = {-1,1,0,0};
    private int[] dc = {0,0,-1,1};
    
    public int solution(int[][] maps) {
        n = maps.length;
        m = maps[0].length;
        
        return bfs(maps);
    }
    
    private int bfs(int[][] maps){
        Queue<int[]> q = new ArrayDeque<>();
        int[][] dist = new int[n][m];
        for(int[] arr : dist) Arrays.fill(arr, -1);
        
        q.offer(new int[]{0,0});
        dist[0][0] = 1; // 문제 조건에 시작칸도 포함
        
        while(!q.isEmpty()){
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];
            
            for(int i=0; i<4; i++){
                int nr = r + dr[i];
                int nc = c + dc[i];

                if(nr<0||nc<0||nr>n-1||nc>m-1) continue; //범위검사
                if(dist[nr][nc] != -1) continue; //방문검사
                if(maps[nr][nc] == 0) continue; //벽검사

                if(nr == n-1 && nc == m-1){
                    return dist[r][c] + 1;
                }

                q.offer(new int[]{nr,nc});
                dist[nr][nc] = dist[r][c] + 1;
            }
        }
        
        return -1;
    }
}


