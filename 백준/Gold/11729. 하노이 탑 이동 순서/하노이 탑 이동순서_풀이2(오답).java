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
import java.util.*;

public class Main {
    static Deque<Integer> stack1 = new ArrayDeque<>();
    static Deque<Integer> stack2 = new ArrayDeque<>();
    static Deque<Integer> stack3 = new ArrayDeque<>();

    static Deque<Integer>[] from = new Deque[]{stack1,stack1,stack2,stack2,stack3,stack3};
    static Deque<Integer>[] to = new Deque[]{stack2,stack3,stack3,stack1,stack1,stack2};
    static String[] moveHistoryPool = {"1 2", "1 3", "2 3", "2 1", "3 1", "3 2"}; //(i+3)%6 하면 반대경로 나오도록 함

    static int diskCount;
    static int minMoveCount = Integer.MAX_VALUE;
    static List<String> moveHistory = new ArrayList<>();
    static List<String> minMoveHistory;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        diskCount = Integer.parseInt(br.readLine());

        initStack1(diskCount);
        backTracking(0, null);
        print();
    }

    private static void print(){
        System.out.println(minMoveCount);
        for(String move : minMoveHistory){
            System.out.println(move);
        }
    }

    private static void backTracking(int moveCount, Integer prevFromToIndex){
        // 가지치기
        if(moveCount >= minMoveCount) return;

        //종료조건
        if(stack3.size() == diskCount){
            minMoveCount = moveCount;
            // Math.min(minMoveCount, moveCount); 가지치기 안 당하고 종료까지 왔으면 moveCount < minMoveCount 라는 얘기.
            minMoveHistory = new ArrayList<>(moveHistory);
            return;
        }

        // 매 재귀의 선택지
        for(int i=0; i<6; i++){
            if(prevFromToIndex != null && i == getOppsiteIndex(prevFromToIndex)) continue; //내가 방금 온 경로 반대로 다시 가는 경우 제외
            if(from[i].isEmpty()) continue; // from에 움직일 원판 없는경우 제외
            if(!to[i].isEmpty() && (from[i].peek() > to[i].peek())) continue; // 이동하려는 원판이 도착지 탑의 top보다 큰 경우 제외

            //선택
            Integer disk = from[i].pop();
            to[i].push(disk);
            moveHistory.add(moveHistoryPool[i]);

            //재귀
            backTracking(moveCount+1, i);

            //선택복구
            to[i].pop();
            from[i].push(disk);
            moveHistory.remove(moveHistory.size()-1);
        }
    }

    private static int getOppsiteIndex(int i){
        return (i+3) % 6;
    }

    private static void initStack1(int diskCount){
        for(int disk = diskCount; disk >= 1; disk--){
            stack1.push(disk);
        }
    }
}

