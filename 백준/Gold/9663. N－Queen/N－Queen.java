/*
BOJ 9663 - N-Queen
[조건]
1) 퀸은 상,하,좌,우,우상,우하,좌상,좌하 총 8개의 방향으로 아무곳이나 이동가능.
2) 크기가 N×N인 체스판 위에 퀸 N개를 서로 공격할 수 없게 놓아야 함.
3) 퀸을 놓는 가능한 경우의 수를 구해야 함.
[접근]
처음에는 
1) 상,하,좌,우,우상,우하,좌상,좌하 순서로 dx, dy 만듦
    int[] dx = {0,0,-1,1,1,1,-1,-1}; // 좌우변화량 
    int[] dy = {1,-1,0,0,1,-1,1,-1}; // 상하변화량
2) dfs로 8방향 각각 맵의 끝까지 탐색하면서, 단 하나라도 visited이면 
   그 자리 탐색 종료, 다음 자리 알아봄
이런식으로 접근을 했는데, 체스판 위를 이동하는 '이동탐색'이 되어 버려서
"퀸을 어디에 배치할 것인가"를 구하는 선택탐색 문제에서 맞지 않는 풀이였다.
이 문제의 핵심은 현재 위치에서 어디로 이동할지를 정하는 것이 아니라
각 행마다 퀸을 놓을 수 있는 열을 하나씩 선택해 보는 백트래킹이다.

두번째 풀이는 
1)한 행에는 퀸을 1개만 놓을 수 있다. 따라서 row = 재귀의 depth 로 두고 
  현재 행에서 가능한 칸을 하나씩 선택해 나가는 방식. 
2)queen 하나 놓고, 놓은 위치를 기준으로 상하좌우대각선 위치를 
모두 방문불가처리하고, 재귀호출한다.
3)재귀호출 이후 원상복귀: queen 놓기전 백업해둔 visited를 복사하여 복구한다.
이렇게 접근했었는데, 한번 queen을 놓을 때 마다 N*N 보드 전체를 복사해서 백업본을 만드니
메모리 초과가 났다.

이후 접근이 어려워 정답을 봤고, 핵심은 
1)체스판 전체 상태를 visited[][]로 관리하지 않고,
  queenCol[row] = col 형태로 '이전 퀸들 좌표만' 저장하는 것이었다.
2)그리고 현재 위치에 퀸을 놓을 수 있는지는, 이전 행들에 놓인 퀸들을 하나씩 돌며
  이전 퀸과 같은 행이거나, 이전퀸에서 대각선에 있는 좌표는 놓을 수 없도록 
  canPlace 함수를 만들어 true/false를 반환하게 했다.
3)현재위치에 놓기가 가능하면, queenCol[row] = col 로 
  현재 행의 선택 결과를 저장한 뒤 다음 행으로 재귀호출한다.
4)재귀호출 이후 백트래킹 단계에서, 퀸 위치 배열에 저장했던 
  현 위치를 원상복구 하지 않아도 된다. 원래는 해주는게 맞는데, 
  현재 for문을 보면 for(int c=0; c<N; c++) 현재 행, 즉 queenCol의 인덱스는
  유지되면서, queenCol[row] = 다음 col 이렇게 덮어쓰기 때문이다.
  
[느낀점]
격자 문제에서 두 좌표 (r1, c1), (r2, c2)가 
|r1 - r2| == |c1 - c2|
이면 같은 대각선 위에 있다는 것을 처음 알게 되었다.

두 좌표가 대각선에 있다는 것은 격자 칸을 좌표로 봤을 때, 
두 좌표를 이은 직선의 기울기가 -1 혹은 1이면 된다.
즉, (r1-r2)/(c1-c2) = +-1이면 같은 대각선 위에 있다고 볼 수 있다.
따라서 |r1 - r2| == |c1 - c2| 이면 같은 대각선에 있다고 판단할 수 있다.

*/
import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int caseCount = 0;
    static int[] queenCol; // queenCol[row] = 해당 행(row)에 놓은 퀸의 열(col). visited 대신이걸 씀.
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine().trim());
        queenCol = new int[N];
 
        dfs(0);
        System.out.println(caseCount);
    }
    
    private static void dfs(int rowIdx){
        if(rowIdx == N) {
            caseCount++;
            return;
        }
        
        for(int c=0; c<N; c++){ // 현재 행의 모든 칸(열)을 확인.
            if(canPlace(rowIdx, c)){
                queenCol[rowIdx] = c; // (rowIdx, c)에 퀸 배치
                dfs(rowIdx+1); // 다음행으로 진행
                // 돌릴필요 없음!!! 다음 for문에서 어차피 같은 행의 다음칸으로 덮어씌워짐.
                // queenCol[rowIdx] = -1;
            }
        }
    }
    
    private static boolean canPlace(int r, int c){
        for(int pr = 0; pr<r; pr++){ //이전행 들에 놓인 퀸들과 비교
            int pc = queenCol[pr];
            
            if(pc==c) return false; //이전 퀸들과 같은 열은 안됨.
            if(Math.abs(r-pr) == Math.abs(c-pc)) return false; // 같은 대각선 안됨.
        }
        return true;
    }
}

// 풀이1 - 메모리초과: 
/*
import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static boolean[][] visited;
    static int caseCount = 0;
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine().trim());
        visited = new boolean[N][N];
 
        dfs(0);
        System.out.println(caseCount);
    }
    
    private static void dfs(int selectedCount){ 
        // 종료조건: N개 선택 시 종료
        if(selectedCount == N){
            caseCount++;
            return;
        }
        
        int rowIdx = selectedCount; //selectedCount와 현재 보는 행 idx는 똑같음.
        
        // 한 열 검사
        for(int i=0; i<N; i++){
            if(visited[rowIdx][i]) continue;
            
            boolean[][] backup = copyVisited(); // 이 점 방문처리 하기 전의 visited 복사
            
            visited[rowIdx][i] = true; // 해당 점 방문처리
            updateForbidden(rowIdx, i);// 해당점의 상하좌우 대각선 모두 visited 처리
            
            dfs(selectedCount+1);
            
            restoreVisited(backup);
        }
        
    }
    
    private static boolean[][] copyVisited(){
        boolean[][] temp = new boolean[N][N];
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                temp[i][j] = visited[i][j];
            }
        }
        return temp;
    }
    
    private static void restoreVisited(boolean[][] backup) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                visited[i][j] = backup[i][j];
            }
        }
    }
    
    private static void updateForbidden(int row, int col){
        for(int i=0; i<N; i++){
            visited[row][i] = true; // 가로 true처리
            visited[i][col] = true; // 세로 true처리
        }
        
        // 우상
        int cr = row;
        int cc = col;
        while(inRange(cr, cc)){
            visited[cr][cc] = true;
            cr++;
            cc++;
        }
        
        // 좌상
        cr = row;
        cc = col;
        while(inRange(cr, cc)){
            visited[cr][cc] = true;
            cr--;
            cc++;
        }
        
        // 우하
        cr = row;
        cc = col;
        while(inRange(cr, cc)){
            visited[cr][cc] = true;
            cr++;
            cc--;
        }
        
        // 좌하
        cr = row;
        cc = col;
        while(inRange(cr, cc)){
            visited[cr][cc] = true;
            cr--;
            cc--;
        }
        
    }
    
    private static boolean inRange(int x, int y){
        return x>=0 && x<N && y>=0 && y<N;
    }
    
}
*/