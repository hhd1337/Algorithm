import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int removeCount = Integer.parseInt(st.nextToken());
        int targetLength = N - removeCount;

        String number = br.readLine();
        
        ArrayDeque<Character> stack = new ArrayDeque<>();
        
        for(int i=0; i<N; i++){
            char currNum = number.charAt(i);
            // 스택 비어있지 않고(비교할 앞자리 있음), 맨뒤 숫자가 현재숫자보다 작으면 pop 하는데,
            // 무작정 pop하면 target개를 못 채울 수 있으니, pop한 뒤에도 남은 숫자들로 target을 채울 수 있을 때만 pop
            while (!stack.isEmpty() && stack.peek() < currNum && (stack.size() - 1) + (N - i) >= targetLength){
                stack.pop();
            }
            if(stack.size() < targetLength){
                stack.push(currNum);
            }
        }

        StringBuilder sb = new StringBuilder(targetLength);
        while(!stack.isEmpty()){
            sb.append(stack.pollLast());
        }

        System.out.println(sb.toString());
    }
}



/*
[왜 이 방식(윈도우 max + substring 조합)이 안 되고, 스택을 써야 했나?]

- N 최대 500,000 → "문자열을 계속 잘라(rest.substring) + 붙이기(+ keep)"를 반복하면
  매 반복마다 새 String 객체가 생성되고(불변), 내부 char[]/byte[]가 복사됨.
  => 길이 O(N)짜리 복사가 (N-K)번 발생할 수 있어 메모리 폭증 + GC 폭주 → 메모리 초과/시간 초과.

- 게다가 매 자리마다 getMaxCharAndIndex(rest)로 rest를 선형 탐색함.
  => (N-K)번 * 평균 rest 길이(최대 O(N)) = 최악 O(N^2) 수준 → 50만에서 현실적으로 불가능.

- 스택(그리디)은 문자열을 "한 번만" 왼→오로 보면서,
  현재 숫자가 더 크면 앞의 작은 숫자를 pop해서 K개만 제거하는 방식.
  => 각 문자는 push/pop 최대 1번 → 전체 O(N), 추가 메모리 O(N) (문자 배열/덱 1개)로 안정적으로 통과.
*/
/*
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        String number = br.readLine();

        int targetDigit = N - K; // 결과 자리수

        StringBuilder result = new StringBuilder(targetDigit);

        String rest = number.substring(0, number.length() - (targetDigit - 1));

        for (int i = 0; i < targetDigit; i++) {
            String keep = number.substring(number.length() - (targetDigit - 1 - i));

            int[] maxNumAndIndex = getMaxCharAndIndex(rest);

            result.append(maxNumAndIndex[0]);
            rest = rest.substring(maxNumAndIndex[1] + 1) + keep;
        }
        System.out.println(result.toString());
    }

    private static int[] getMaxCharAndIndex(String stringNum) {
        int[] MaxNumAndIndex = new int[2]; // DTO 역할
        
        for (int i = 0; i < stringNum.length(); i++) {
            int charNum = stringNum.charAt(i);
            if (charNum > MaxNumAndIndex[0]) {
                MaxNumAndIndex[0] = charNum;
                MaxNumAndIndex[1] = i;
            }
        }
        return MaxNumAndIndex;
    }
}
*/

