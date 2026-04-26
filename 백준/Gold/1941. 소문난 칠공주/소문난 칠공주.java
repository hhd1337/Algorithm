/*
BOJ 1941 - 소문난 칠공주

[조건]
1)S와 Y로만 이뤄진 5*5 보드에서 인접한 7칸을 선택했을 때, S가 Y보다 많은 경우의 수를 구하라.

[접근]
1)순서 상관없이 25자리중 7개를 뽑고, 인접검사, S>Y검사 를 하여 count한다.
-> 순서 상관없이, 25개 중 7개 뽑는 방식은 25C7 = 480700가지.
-> 인접검사를 하려면 시작점부터 끝점까지 확인해야 하니 7회 확인연산
-> S가 Y보다 많은지 검사하려면 모든 점 확인 후 S수>Y수 인지 확인해야 하므로 약 8회 연산
-> 480700 * 15 = 7,210,500 (문제 제한시간 2초: 6000만 연산까지 안전.)
==> 이 방식 안전함.

[느낀점]
1) 순서 상관없이, 2차원 자리 25개 중 7개를 중복없이 뽑는 방법을 확실히 익혔다. 
- 첫번째: 2차원 좌표(0,0~4,4) 25개를 1차원좌표로 바꿔서(0~24) 0~24중 7개를 중복없이 뽑는 문제로 생각한다.
         (2차원 좌표가 필요하면 나중에 int r = 1차원좌표/5; int c = 1차원좌표%5; 이렇게 바꾸면 된다.)
- 두번째: 백트래킹 함수를 이용하여, 0~24 중 오름차순으로 만들 수 있는 모든 숫자조합 경우의 수를 탐색한다.
         (중복제거를 위해 오름차순으로 7개를 뽑는 것이 핵심이다. 1,3,5는 나오지만 5,3,1이나 3,5,1같은 중복 경우는 절대로 안 나온다.)

2) 1초 당 처리가능한 연산횟수를 확실히 알았다.(코테 언어로 자바를 사용할 때)
- 일반적: 1초 = 1억번의 연산
- 안전한 설계 시: 1000만~3000만 정도 내외

3) 문제를 설계할 때, 비효율적이고 시간초과가 날 것 같아 설계한 방식을 신뢰하지 못할 때가 있다.
그때 확실히 이 방식을 사용해도 되는지 검증하려면, 위의 '1초당 처리가능한 연산횟수'를 고려하여 
내가 설계한 방식이 대략 어느정도 연산 수가 나오는지 계산하고, 문제에 주어진 시간안에 처리할 수 
있는 범위인지 확인해야 한다는 걸 확실히 배웠다. 

이 문제의 경우 나는 처음에 (순서 상관없이 25자리중 7개를 뽑고 인접검사, S>Y검사 하는 방식)을 떠올리고, 
비효율적이라 판단하여 다른 방법을 계속 고민했다. 근데 마땅한 방법이 떠오르지 않았다.

연산량을 계산해보니 순서 상관없이, 25개 중 7개 뽑는 방식은 25C7 = 480700가지 이고, 
각 경우에 대하여 해야 하는 두번의 검사는 약 14회정도로 볼 수 있다.
- BFS를 돌며 인접한지 검사 -> 약 7번의 연산
- S가 Y보다 많은지 검사 -> 약 7~8번의 연산
그렇다면 480700 * 15 = 7,210,500 정도가 나온다. -> 1천만 아래의 연산이 나온다. 
심지어 
- 문제의 제한시간은 2초이기 때문에 3000만*2초 = 6000만 정도의 연산은 안전하다.
- 7개를 뽑은 다음에 인접검사에서 탈락하면 해당 케이스는 S>Y검사를 안해도 된다.
  무작위로 뽑았을 때 (인접할 확률 < 안 인접할 확률) 이므로 
  인접검사 48만가지 경우의 수 중에 절반이상은 S>Y검사를 안해도 된다.
- 안 인접할 경우에 BFS에서 시작점부터 6개는 인접한데 7번째 자리가 떨어져 있는 경우는 드물것이다.
  즉 처음 인접검사도 안 인접한 경우에 7번의 연산을 모두 하지 않고 BFS를 탈출할 것이다. 여기서도 연산수를 아낀다.

결국 이 방식(순서 상관없이 25자리중 7개를 뽑고 인접검사, S>Y검사 하는 방식)을 사용해도 된다는 결론이 나온다. 
위의 심지어~ 부분은 정확히 고려하지 않아도, 앞으로는 그냥 다른 방법이 떠오르지 않으면
'2초면 6000만까지 안전한데 1천만 아래의 연산이 예상되니 이방식으로 풀자' 하고 빠르게 풀어나가야겠다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static char[][] board = new char[5][5];
    static int[] selected7Idxs = new int[7];
    static int sevenPrincessCount;

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        init();

        select7(0,0);
        System.out.println(sevenPrincessCount);
    }

    //0~24번 칸 중에서 7개를 중복 없이 고르는 조합 생성 함수, 백트래킹 함수
    static void select7(int depth, int start) {
        //종료조건
        if (depth == 7) {
            if(is7Connected() && hasMoreS()){
                sevenPrincessCount++;
            }
            return;
        }
        //이번 재귀 선택지: 이전재귀에서 선택한 수 뒤에서만 선택가능.(오름차순으로 선택함)
        for (int i = start; i < 25; i++) {
            // 선택
            selected7Idxs[depth] = i;
            // 재귀호출
            select7(depth+1, i+1);
            // 선택취소는 명시적으로 없어도 for문으로 다음 수 선택하여 넣는 구조.
        }
    }

    static boolean is7Connected(){
        // 선택여부를 O(1) 조회하기 위해 7명 선택여부가 담긴 2차원배열 미리 만들어놓기.
        boolean[][] selected = new boolean[5][5];
        for (int idx : selected7Idxs) {
            int r = idx / 5;
            int c = idx % 5;
            selected[r][c] = true;
        }

        boolean[][] visited = new boolean[5][5];
        Queue<int[]> q = new ArrayDeque<>();

        int sr = selected7Idxs[0] / 5;
        int sc = selected7Idxs[0] % 5;

        q.offer(new int[]{sr, sc});
        visited[sr][sc] = true;

        int connectedCount = 1;

        while (!q.isEmpty()) {
            int[] cur = q.poll();

            int r = cur[0];
            int c = cur[1];

            for(int i=0; i<4; i++){
                int nr = r+dr[i];
                int nc = c+dc[i];

                if(nr<0||nc<0||nr>=5||nc>=5) continue; //범위검사
                if(visited[nr][nc]) continue; //방문검사
                if(!selected[nr][nc]) continue; //이번에 선택한 7자리 아니면 넘어감

                //여기까지 왔다는건, 
                //nr,nc가 이번에 선택된 7칸중 하나이고, 아직 방문하지 않았고, 현재 칸과 인접해 있다는 뜻
                //->인접count++해놓고 큐에넣음, 마지막에 인접count가 7인지 확인함.
                visited[nr][nc] = true;
                q.offer(new int[]{nr, nc});
                connectedCount++;
            }
        }

        return connectedCount == 7;
    }

    static boolean hasMoreS(){
        int sCount = 0;

        for (int idx : selected7Idxs) {
            int r = idx / 5;
            int c = idx % 5;

            if (board[r][c] =='S') sCount++;
        }
        return sCount >= 4;
    }

    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for(int i=0; i<5; i++){
            String str = br.readLine();
            for(int j=0; j<5; j++){
                board[i][j] = str.charAt(j);
            }
        }
    }
}