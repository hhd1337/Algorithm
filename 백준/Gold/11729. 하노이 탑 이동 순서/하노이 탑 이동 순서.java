/*
BOJ 11729 - 하노이 탑 이동 순서 

[조건] 
1) 한번에 한개의 원판만 옮길 수 있음 
2) 큰 원판을 작은 원판 위에 올릴 수 없음 
3) 1번탑 모든 원판을 3번탑으로 옮기는 최소횟수 

[접근] 
가장 큰 원판 하나를 1번에서 3번으로 옮기려면, 
그 위의 n-1개 원판을 먼저 2번 장대로 옮겨야 한다. 
그 후 가장 큰 원판을 3번으로 옮기고, 
다시 2번 장대의 n-1개 원판을 3번 장대로 옮기면 된다.

[느낀점]
**오답노트**
처음 이 문제를 봤을 때는 하노이 탑의 정해진 재귀 구조를 몰랐기 때문에,
매 순간 가능한 이동을 직접 선택해 나가는 백트래킹 문제라고 생각하고 풀었다.
그렇게 구현을 마친 뒤 실행해보니 해결되지 않는 예외가 계속 발생했는데,
백트래킹 재귀 메서드가 계속 깊어지다가 발생하는 StackOverflowError였다.

직접적인 실패 원인은 순환 상태를 막지 못한 것이었다.
같은 상태를 다른 경로로 다시 방문할 수 있는 구조였는데,
이를 막는 방문 처리나 충분한 순환 방지 조건 없이 백트래킹 재귀를 돌리다 보니,
어떤 경우에는 재귀가 끝없이 깊어질 수밖에 없었다.
그래서 실행할 때마다 스택 오버플로우가 발생했다.

하지만 더 본질적인 패착은 이 문제를 애초에 백트래킹 문제로 분류한 점이었다.
이 문제는 매 재귀마다 선택지를 비교하며 최적해를 탐색하는 문제가 아니라,
가장 큰 원판을 옮기기 위해 필요한 선행 작업이 이미 구조적으로 정해져 있는 재귀 문제였다.
즉, '지금 어떤 이동을 선택할까'를 고민할 문제가 아니라,
'가장 큰 원판을 옮기기 전에 반드시 무엇을 해야 하는가'를 생각했어야 했다.

머리를 정말 많이 써서 구현해서 다 풀고 '정말 잘 풀었다'고 생각했는데,
접근 자체가 문제의 본질과 맞지 않았다는 점이 아쉬웠다.
이번 문제를 통해 상황이 무한순환하는 것을 막을 방법이 마땅치 않다면 
백트래킹 재귀를 사용하지 말아야 겠다고 머리에 완전히 새겼다.

**정답 풀이**
이 문제는 다음과 같은 하나의 재귀 구조로 정리된다.
1. n-1개의 원판을 출발탑에서 보조탑으로 옮긴다.
2. 가장 큰 원판 1개를 출발탑에서 목적지탑으로 옮긴다.
3. n-1개의 원판을 보조탑에서 목적지탑으로 옮긴다.
*/


import java.io.*;

public class Main {
    static int diskCount;
    static long minMoveCount = 0;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        diskCount = Integer.parseInt(br.readLine());

        moveDisks(diskCount, 1, 2, 3);

        System.out.println(minMoveCount);
        System.out.print(sb);
    }

    // 원판 diskCount개를 from에서 to로 옮기는데, via를 보조로 사용
    private static void moveDisks(int diskCount, int from, int via, int to) {
        if (diskCount == 1) {
            sb.append(from).append(" ").append(to).append('\n');
            minMoveCount++;
            return;
        }
        // 1. 위의 n-1개를 보조 탑으로 이동
        moveDisks(diskCount - 1, from, to, via);
        // 2. 제일 큰 원판 1개를 목적지로 이동
        sb.append(from).append(" ").append(to).append('\n');
        minMoveCount++;
        // 3. 보조 탑의 n-1개를 목적지로 이동
        moveDisks(diskCount - 1, via, from, to);
    }
}