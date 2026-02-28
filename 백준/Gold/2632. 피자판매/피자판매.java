/*
[구현 아이디어]
- 두 피자 각각에서 만들 수 있는 연속된 조각 크기 모든경우를 미리 count함.
- 한쪽에서 만든 크기와 다른 쪽에서 만든 크기를 서로 보완해서 주문한 크기가 되는 조합의 수를 count한다.
  (한 종류만 사용하는 경우와 섞어 사용하는 경우 모두 포함해야 함)
  
- 원형 구조는 배열을 두 번 이어 붙여 선형처럼 본다.
- 누적합으로 구간 크기를 빠르게 계산한다. 
- 전체 한 판을 선택하는 경우는 한 번만 별도로 처리한다.
*/

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int targetSize = Integer.parseInt(br.readLine().trim()); // 손님이 원하는 크기 (T)
        StringTokenizer st = new StringTokenizer(br.readLine());
        int m = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());

        int[] A = new int[m];
        int[] B = new int[n];

        for (int i = 0; i < m; i++) {
            A[i] = Integer.parseInt(br.readLine().trim());
        }
        for (int i = 0; i < n; i++) {
            B[i] = Integer.parseInt(br.readLine().trim());
        }

        long[] countA = buildSumCount(A, m, targetSize);
        long[] countB = buildSumCount(B, n, targetSize);

        long answer = 0;
        for (int aSum = 0; aSum <= targetSize; aSum++) {
            int bSum = targetSize - aSum;
            answer += countA[aSum] * countB[bSum];
        }
        System.out.println(answer);
    }

    private static long[] buildSumCount(int[] slices, int len, int target) {
        long[] count = new long[target + 1];
        count[0] = 1; // 이 피자를 아예 안 고르는 경우

        // 원형을 2배로 펼쳐서 선형처럼 봄.
        int[] doubled = new int[len * 2];
        for (int i = 0; i < len * 2; i++) {
            doubled[i] = slices[i % len];
        }

        long[] prefix = new long[len * 2 + 1];
        for (int i = 0; i < len * 2; i++) {
            prefix[i + 1] = prefix[i] + doubled[i];
        }

        for (int start = 0; start < len; start++) {
            for (int length = 1; length <= len - 1; length++) {
                long sum = prefix[start + length] - prefix[start];
                if (sum > target) {
                    break; //양수만 있으니 length 늘리면 sum은 더 커짐. break.
                }
                count[(int) sum]++;
            }
        }

        long totalSum = 0;
        for (int v : slices) {
            totalSum += v;
        }
        if (totalSum <= target) {
            count[(int) totalSum]++;
        }

        return count;
    }
}