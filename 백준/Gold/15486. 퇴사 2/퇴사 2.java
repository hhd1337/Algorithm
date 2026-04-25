/*
BOJ 15486 - 퇴사 2

[조건]
1)오늘부터 N+1일째 되는 날 퇴사하기 위해 남은 N일동안 최대한 많은 상담을 하려고 함.
2)상담을 적절히 했을 때, 백준이가 얻을 수 있는 최대 수익을 구하라.

[접근]
1) 1 ≤ N ≤ 1,500,000 : 대놓고 완전탐색으로 풀지말라 하고 있음.
2) 수익합을 누적 저장하는 배열을 하나 더 만들어서, 해당 일자에 도달했을 때 가장 큰 수익합을 누적 저장하는 방식으로 풀면 됨.
==> DP

[느낀점]
for문을 순회하면서 각 날짜(i)에 도달했을 때의 최대 누적수익 dp[i]를 기준으로, 
그 이후의 상태를 갱신해나가는 방식으로 문제를 해결했다.
특정 날짜에 상담이 끝나는 모든 경우를 미리 dp[endDay]에 반영해 두고, 
해당 날짜에 도달했을 때 그 중 최댓값만을 유지하도록 Math.max()로 갱신하는 구조가 핵심이었다.
상담을 안하고 다음날로 넘어가는 경우와 상담을 하고 종료일로 점프하는 경우를 모두 dp 배열에 누적시키는 방식을 확실히 익혔다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[] T; // 1-based 로 사용. index: i일
    static int[] P;
    static int[] dp;
    
    public static void main(String[] args) throws IOException {
        init();

        dp = new int[N + 2];

        for (int i=1; i<=N; i++) {
            //i일 상담을 하지 않고 다음 날로 넘어가는 경우
            dp[i + 1] = Math.max(dp[i + 1], dp[i]);

            //i일 상담을 하는 경우
            int endDay = i + T[i];
            if (endDay <= N+1) {
                dp[endDay] = Math.max(dp[endDay], dp[i]+P[i]);
            }
        }
         
        System.out.println(dp[N + 1]);
    }
    
    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        T = new int[N+1];
        P = new int[N+1];
        
        for(int i=1; i<=N; i++){
            st = new StringTokenizer(br.readLine());
            T[i] = Integer.parseInt(st.nextToken());
            P[i] = Integer.parseInt(st.nextToken());
        }
    }
}