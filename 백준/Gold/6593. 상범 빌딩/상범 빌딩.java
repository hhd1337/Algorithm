/*
BOJ 6593 - 상범빌딩
[접근]
1) 3차원격자: 그래프의 일종
2) 시작지점에서부터 끝지점까지 최단거리
3) 한번 이동(그래프 간선 가중치)이 1분으로 일정함
: 3차원 BFS
[느낀점]
3차원 BFS라는것 외에 특별할 게 없는 생각보다 간단한 문제였다!
*/

import java.io.*;
import java.util.*;

public class Main {
    static int L, R, C; //L:층수, R:행수, C열수
    static char[][][] building;
    
    // 동서남북상하
    static int[] dl = {0,0,0,0,1,-1};
    static int[] dr = {0,0,1,-1,0,0};
    static int[] dc = {1,-1,0,0,0,0};
    
    static int sl, sr, sc; // 시작점
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        while(true){
            st = new StringTokenizer(br.readLine());
            
            L = Integer.parseInt(st.nextToken());
            R = Integer.parseInt(st.nextToken());
            C = Integer.parseInt(st.nextToken());
            
            if(L==0 && R==0 && C==0) break;
            
            initBuilding(br);
            Integer escapeTime = bfs();
            
            print(escapeTime);
        }
    }
    
    private static void print(Integer escapeTime){
        if(escapeTime == null) {
            System.out.println("Trapped!");
        } else{
            System.out.println("Escaped in " + escapeTime + " minute(s).");
        }
    }
    
     // 최단시간 반환, 없으면 null 반환
    private static Integer bfs(){
        Queue<int[]> q = new ArrayDeque<>();
        int[][][] dist = new int[L][R][C];
        for(int l=0; l<L; l++){
            for(int r=0; r<R; r++){
                Arrays.fill(dist[l][r], -1);
            }
        }
        
        q.add(new int[]{sl,sr,sc});
        dist[sl][sr][sc] = 0;
        
        while(!q.isEmpty()){
            int[] cur = q.poll();
            int l = cur[0];
            int r = cur[1];
            int c = cur[2];
            
            for(int i=0; i<6; i++){
                int nl = l+dl[i];
                int nr = r+dr[i];
                int nc = c+dc[i];
                
                if(nl<0 || nr<0 || nc<0 || nl>L-1 || nr>R-1 || nc>C-1) continue; //범위검사
                if(dist[nl][nr][nc] != -1) continue; //방문검사
                if(building[nl][nr][nc] == '#') continue; //벽검사
                
                if(building[nl][nr][nc] == 'E'){
                    return dist[l][r][c] + 1;
                }
                
                q.add(new int[]{nl,nr,nc});
                dist[nl][nr][nc] = dist[l][r][c] + 1;
            }
        }
        
        return null;
    }
    
    private static void initBuilding(BufferedReader br) throws IOException {       
        building = new char[L][R][C];
        
        for(int l=0; l<L; l++){
            for(int r=0; r<R; r++){
                String line = br.readLine();
                
                for(int c=0; c<C; c++){
                    char ch = line.charAt(c);
                    saveIfS(ch, l, r, c); //S 위치 저장
                    building[l][r][c] = ch;
                }
            }
            br.readLine();
        }
    }
    
    private static void saveIfS(char ch, int l, int r, int c){
        if(ch == 'S') {
            sl = l;
            sr = r;
            sc = c;
        }
    }
}