import java.util.ArrayDeque;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();  // 사람 수
        int k = sc.nextInt();  // K번째 사람 제거함

        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) {
            deque.addLast(i);
        }

        StringBuilder sb = new StringBuilder();
        sb.append('<');

        while (!deque.isEmpty()) {
            // 앞사람을 맨 뒤로 보냄(회전 로직) -> k-1번 함.
            for (int i=0; i < k-1; i++) {
                deque.addLast(deque.removeFirst());
            }
            //맨앞으로 온 K번째 사람 제거
            int removed = deque.removeFirst();
            sb.append(removed);

            if (!deque.isEmpty()) {
                sb.append(", ");
            }
        }
        sb.append('>');
        System.out.println(sb.toString());
    }
}
