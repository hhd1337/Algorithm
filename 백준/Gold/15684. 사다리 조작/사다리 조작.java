/*
조건) 두 가로선은 연속하거나 서로 접하면 안됨

1. initMap(); 을 함 -> boolean[H+1][N+1] map만들고 a,b 읽어서 true 처리.
2. i번 줄에서 사다리타기 수행 로직
   map에서 i번 줄을 차례로, 끝까지 내려가면서(i,1..curH..H)
      - (curH,i-1)가 true면 i = i-1;
      - (curH,i)가 true면 i = i+1;   
   return 원래라인번호==끝까지 for문 돌고 난 라인번호;
3. 
   1) 가로선 0개 -> 원래 모든i -> i 이면 0반환
   2) 

*/

import java.io.*;
import java.util.*;

public class Main {
    private static boolean[][] initMap; 
    private static int N; // 세로선의 개수
    private static int M; // 가로선의 개수
    private static int H; // 높이
    
    private static final int LINE_ADD_LIMIT = 3;
    
    public static void main(String[] args) throws Exception {
        initMap();
        System.out.println(getMinHorizonLineCount());
    }
    
    private static int getMinHorizonLineCount(){
        if(isAllLineEndsSame(initMap)){ // 가로선 추가 안하고 돌려봄
            return 0;
        }
        
        for (int horLineCnt = 1; horLineCnt <= LINE_ADD_LIMIT; horLineCnt++){
            if(tryAddLines(initMap, 0, horLineCnt)){
                return horLineCnt;
            }
        }
        return -1;
    }
    
    private static boolean tryAddLines(boolean[][] map, int startIdx, int remainHorLine){
        if (remainHorLine == 0) { // 다 놨으면 확인
            return isAllLineEndsSame(map);
        }

        int totalPositionCount = (N - 1) * H; // 전체 칸을 일차원 인덱스로 본다.

        for(int idx = startIdx; idx < totalPositionCount; idx++){
            int curH = idx / (N - 1) + 1;
            int curLine = idx % (N - 1) + 1;

            if(!isLineAddable(map, curLine, curH)){
                continue;
            }
            
            map[curH][curLine] = true;

            // 다음은 idx+1부터 보면서 조합 중복 제거
            if(tryAddLines(map, idx+1, remainHorLine-1)){
                return true;
            }
            
            map[curH][curLine] = false; // 원복(백트래킹)
        }
        return false;
    }
    
    private static boolean isLineAddable(boolean[][] map, int curLine, int curH){ // 현재 가로선이 있거나 가로선의 양옆이면 스킵
        if(map[curH][curLine] == true){
            return false;
        }
        if(curLine > 1 && map[curH][curLine-1] == true){
            return false;
        }
        if(curLine < N-1 && map[curH][curLine+1] == true){
            return false;
        }
        return true;
    }
    
    private static boolean isAllLineEndsSame(boolean[][] map){
        for(int line=1; line<=N; line++){
            if(!isLineEndsSame(line, map)){
                return false;
            }
        }
        return true;
    }
    
    private static boolean isLineEndsSame(int line, boolean[][] map){
        int startLine = line;
        for(int curH=1; curH<=H; curH++){
            if(map[curH][line-1] == true){ // ==true 불필요하지만, 가독성 때문에 놔둠
                line -= 1;
            }
            else if(map[curH][line] == true){
                line += 1;
            }
        }
        return line == startLine;
    }
        
    private static void initMap() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());
        
        initMap = new boolean[H+1][N+1]; // N: 세로 길이, H: 가로 높이
        
        // 가로선 표시
        for(int i=1; i<=M; i++){
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            
            initMap[a][b] = true; // 항상 가로선의 왼쪽 점에다가 표시
        }
    }
}