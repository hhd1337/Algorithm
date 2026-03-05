/*
1. 데드라인 오름차순으로 정렬
2. for(1..문제번호..N) 
   - 데드라인 숫자가 이전 루프와 다르
*/
import java.util.*;
import java.io.*;

public class Main {

    public static class Problem implements Comparable<Problem> {
        int deadline;
        int cupCount;

        Problem(int deadline, int cupCount) {
            this.deadline = deadline;
            this.cupCount = cupCount;
        }

        @Override
        public int compareTo(Problem o) {
            return Integer.compare(this.deadline, o.deadline);
        }
    }

    private static int N; // 문제 총 개수
    private static Problem[] probs; // 문제 객체 배열

    public static void main(String[] args) throws Exception {
        init();
        Arrays.sort(probs, 1, N+1);

        PriorityQueue<Integer> selectedCupCounts = new PriorityQueue<>(); // 지금까지 선택한 문제들의 컵라면 수

        for (int i = 1; i <= N; i++) {
            int thisDeadline = probs[i].deadline;
            int thisCupCount = probs[i].cupCount;

            // 일단 넣음
            selectedCupCounts.offer(thisCupCount);
            // 문제당 1시간 걸리니까 thisDeadline시점에 최대로 풀 수 있는 문제 개수는 thisDeadline개.
            // 선택한 문제 수가 thisDeadline 초과가 되면, 가장 작은 컵라면 문제 버림
            if (selectedCupCounts.size() > thisDeadline) {
                selectedCupCounts.poll();
            }
        }

        int maxCupSum = 0;
        while (!selectedCupCounts.isEmpty()) {
            maxCupSum += selectedCupCounts.poll();
        }

        System.out.println(maxCupSum);
    }

    private static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine().trim());
        probs = new Problem[N+1];

        for (int i = 1; i <= N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int deadline = Integer.parseInt(st.nextToken());
            int cupCount = Integer.parseInt(st.nextToken());

            probs[i] = new Problem(deadline, cupCount);
        }
    }
}