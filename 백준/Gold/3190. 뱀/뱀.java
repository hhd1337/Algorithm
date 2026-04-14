/*
BOJ 3190 - 뱀

[조건]
1)뱀은 시작할 때 좌측 최상단 칸에 위치하고, 길이는 1이며, 오른쪽을 향한다. / 뱀 시작위치에는 사과가 없다.
2)매 초마다 뱀은 몸길이를 1늘려 머리를 다음칸에 위치한다.
3)이동한 칸에 사과 있으면 사과 없어지고 꼬리는 그대로.
4)이동한 칸에 사과 없으면 꼬리가 위치한 칸 비워준다.(몸길이 변화없이 앞으로 한칸 온것.)
5)뱀이 벽 또는 자신과 부딪히면 게임 끝.
이 게임이 몇 초에 끝나는지 계산하라.

[접근]
이런 문제는 완전히 시뮬레이션(구현)이라고 보였다. 
보드에는 사과:A/뱀몸:S/빈칸:null로 표시하려한다.
1)while(true)
    - 이번 초에 뱀 방향으로 다음 이동할 칸 구함
    - 뱀 이동할 칸에 사과/몸통/꼬리/벽 있는지 확인 및 움직임(보드에 반영-> 뱀을 'S'로.)
      - 벽이면 초++하고 break;
      - 몸통이면 초++하고 break;
      - 꼬리면 그칸 S표시하고 꼬리 지움 (꼬리를 머리로 바꾸면 됨)
      - 사과면 그칸 S표시하고 꼬리 냅둠
      - 다음 초에 뱀 방향 확인(앞? 좌? 우?)하고 전역필드에다 기록함
    - 초++

[느낀점]
처음에는 보드에 뱀을 'S' 표시하는 것으로만 관리하며 구현하고 있었는데, 
다음 이동할 칸이 뱀의 꼬리인지 알아낼 방법이 없어 막혔었다. 
그러다가 꼬리와 머리를 계속 넣고 빼는 작업들이 있으므로 
뭔가 자료구조를 하나 사용하여 뱀을 표현하면 쉽게 넣고 쉽게 꺼낼 수 있겠다 싶었다. 
문제를 푸는 데 성공은 한 것 같은데, 시뮬레이션 문제가 늘 그렇듯 내 풀이가 비효율적이진 않을지 궁금해진다.
고수들은 어떻게 풀었는지 확인해야만 잠을 잘수 있겠다.
++시간이 엄청 많이 걸렸다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static Character[][] board;
    static Map<Integer, Character> dirChange = new HashMap<>();

    // L이면 왼쪽으로 90도, D이면 오른쪽으로 90도-> 시계방향으로 우하좌상으로 세팅
    static int[] dr = {0,1,0,-1};
    static int[] dc = {1,0,-1,0};
    static int dIdx;
    static int headR, headC;

    public static void main(String[] args) throws IOException {
        init();

        int second = 0;
        Deque<int[]> snake = new ArrayDeque<>(); // 뱀꼬리 first----------last 뱀머리

        snake.addLast(new int[]{0, 0});
        board[0][0] = 'S';

        while (true) {
            int nr = headR + dr[dIdx];
            int nc = headC + dc[dIdx];

            //이동할 위치가 벽 밖이면
            if (outOfRange(nr, nc)) {
                second++;
                break;
            }

            Character nextSlot = board[nr][nc];

            //이동할 위치가 뱀이면
            int[] tail = snake.peekFirst();
            if (nextSlot != null && nextSlot == 'S') {
                second++;
                break;
            }

            //이동할 위치가 사과면
            if (nextSlot != null && nextSlot == 'A') {
                //머리추가하고 끝
                snake.addLast(new int[]{nr, nc}); 
                board[nr][nc] = 'S';
            } else {
                // 꼬리삭제, 꼬리 null표시
                int[] removedTail = snake.removeFirst(); 
                board[removedTail[0]][removedTail[1]] = null;
                // 머리추가
                snake.addLast(new int[]{nr, nc}); 
                board[nr][nc] = 'S';
            }

            headR = nr;
            headC = nc;

            second++;
            
            // dirChange에는 이번 초 지나고 나서 전환되는 방향이 들어있으니까 여기서 다음dIdx 업데이트해놓음. 
            Character directionChange = dirChange.get(second);
            dIdx = getDIdx(directionChange, dIdx);
        }

        System.out.println(second);
    }

    public static boolean outOfRange(int r, int c) {
        int boardLength = board.length;
        return r<0 || c<0 || r>boardLength-1 || c>boardLength-1;
    }

    private static int getDIdx(Character directionChange, int dIdx) {
        if (directionChange == null) return dIdx;
        if (directionChange == 'L') return (dIdx-1+4)%4; // dIdx가 0이면 -1%4 == -1이 반환되서 안됨.
        if (directionChange == 'D') return (dIdx+1)%4;
        return dIdx;
    }

    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());
        board = new Character[N][N];

        // 사과 넣기
        int K = Integer.parseInt(br.readLine());
        for (int k = 0; k < K; k++) {
            st = new StringTokenizer(br.readLine());
            int appleR = Integer.parseInt(st.nextToken()) - 1;
            int appleC = Integer.parseInt(st.nextToken()) - 1;
            board[appleR][appleC] = 'A';
        }

        // 뱀 방향 변환 기록
        int L = Integer.parseInt(br.readLine());
        for (int l = 0; l < L; l++) {
            st = new StringTokenizer(br.readLine());
            Integer second = Integer.parseInt(st.nextToken());
            Character direction = st.nextToken().charAt(0);
            dirChange.put(second, direction);
        }
    }
}