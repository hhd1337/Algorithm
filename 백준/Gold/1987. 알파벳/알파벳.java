/*
BOJ 1987 - 알파벳

[조건]
1)말은 상하좌우로 인접한 네 칸 중의 한 칸으로 이동가능
2)말은 지금까지 지나온 알파벳이 적힌 칸으로는 이동할 수 없음.

[접근]
백트래킹으로 모든 경우를 탐색하며 maxMoveCount 갱신.

[느낀점]
전형적인 백트래킹 문제였지만, 종료조건을 잡는데 조금 걸렸다. 
for문이 종료되었을 때 아무데도 못갔다면 거기가 마지막인 것이므로, 
moved라는 플래그를 만들어 종료조건 검사에 이용했다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static int R, C;
    static char[][] map;
    static int maxMoveCount;
    static Set<Character> usedAlphabets = new HashSet<>();

    static int[] dr = {-1,1,0,0};
    static int[] dc = {0,0,-1,1};

    public static void main(String[] args) throws IOException {
        init();
        
        usedAlphabets.add(map[0][0]); // 시작 알파벳 Set에 추가
        backtrack(0,0,1);
        
        System.out.println(maxMoveCount);
    }

    private static void backtrack(int cr, int cc, int moveCount){
        boolean moved = false;
        
        //이 재귀의 선택지
        for(int i=0; i<4; i++){
            int nr = cr+dr[i];
            int nc = cc+dc[i];

            if(nr<0||nc<0||nr>R-1||nc>C-1) continue; //범위검사
            if(usedAlphabets.contains(map[nr][nc])) continue; //방문검사 할 필요없이, 지나온 알파벳이면 갈수없음

            moved = true;
            
            //선택
            usedAlphabets.add(map[nr][nc]);
            //재귀호출
            backtrack(nr,nc,moveCount+1);
            //선택취소
            usedAlphabets.remove(map[nr][nc]);
        }

        //종료조건
        if (!moved) {
            maxMoveCount = Math.max(maxMoveCount, moveCount);
            return;
        }
    }

    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        map = new char[R][C]; 
        for(int r=0; r<R; r++){
            String str = br.readLine();
            for(int c=0; c<C; c++){
                map[r][c] = str.charAt(c);
            }
        }
    }
}