/*
[구현 아이디어2 - 이중 for문에서 j를 i+1부터 시작하지 말고, 
이전에 멈춘 j부터 시작하도록 함. 즉 j를 전역으로 관리, 절대 감소시키지 않음.
왜냐하면 배열을 정렬 해놓았기 때문에 이번 루프 j는 저번루프 j이상의 값일 수 밖에 없음.]
1. 정렬을 함
2. M 이상이면서 최솟값인 전역변수 선언 (minValidDiff)
2. 정렬된 배열 앞에서 부터 한 요소씩 선택하여
   - 오른쪽으로 한칸씩 가며 차이가 M 이상이 되는 수를 찾는다.(찾으면 멈춤, 찾은 값은 전역에서 관리.)
   ... 배열 끝에서 두번째요소까지 진행한다.
3. minValidDiff 출력한다.
*/

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken()); // 수열길이
        long M = Integer.parseInt(st.nextToken()); // M이상인 가장 작은 수 반환해야함
        
        int[] numbers = new int[N];
        
        for(int i=0; i<N; i++){
            st = new StringTokenizer(br.readLine());
            numbers[i] = Integer.parseInt(st.nextToken());
        }
        
        Arrays.sort(numbers);
        
        long minValidDiff = Long.MAX_VALUE;
        
        int j = 1;
        
        for(int i=0; i<N-1; i++){
            if (j < i+1){
                j = i+1;
            }
            int thisNum = numbers[i];
            
            while(j<N){
                long thisDiff = (long)numbers[j] - thisNum;
                if (thisDiff >= M){
                    minValidDiff = Math.min(minValidDiff, thisDiff);
                    break;
                }
                j++;
            }
        }
        
        System.out.println(minValidDiff);
    }
}


/*
[구현 아이디어1 - 이중 for문: 시간 초과]
1. 정렬을 함
2. M 이상이면서 최솟값인 전역변수 선언 (minValidDiff)
2. 정렬된 배열 앞에서 부터 한 요소씩 선택하여
   - 오른쪽으로 한칸씩 가며 차이가 M 이상이 되는 수를 찾는다.(찾으면 멈춤, 찾은 값은 전역에서 관리.)
   ... 배열 끝에서 두번째요소까지 진행한다.
3. minValidDiff 출력한다.
*/
/*
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken()); // 수열길이
        long M = Integer.parseInt(st.nextToken()); // M이상인 가장 작은 수 반환해야함
        
        int[] numbers = new int[N];
        
        for(int i=0; i<N; i++){
			st = new StringTokenizer(br.readLine());
			numbers[i] = Integer.parseInt(st.nextToken());
		}
        
        Arrays.sort(numbers);
        
        long minValidDiff = Long.MAX_VALUE;
        
        for(int i=0; i<N-1; i++){
            int thisNum = numbers[i];
            
            for(int j=i+1; j<N; j++){
                long thisDiff = (long)numbers[j] - thisNum;
                if(thisDiff >= M && thisDiff < minValidDiff){
                    minValidDiff = thisDiff;
                    break;
                }
            }
        }
        
        System.out.println(minValidDiff);
    }
}
*/

