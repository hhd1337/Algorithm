/*
BOJ 12865 - 평범한 배낭

[조건]
1)물건이 여러개인데, 각 물건은 무게W와 가치V가 있다.
2)배낭에는 최대 K만큼의 무게만 들어간다. 배낭에 넣을 수 있는 물건들의 가치의 최댓값을 구하라

[접근]
배낭에 꽉 채우는 모든 경우의 수를 탐색해봐야 할 것 같음.

[느낀점]
처음에는 물건을 넣는 모든 조합을 탐색하는 백트래킹 문제처럼 보였지만,
N이 최대 100이기 때문에 선택/미선택 조합을 모두 보면 2^100 이 나온다. 
2^10이 1024이므로 대략 10^3이라고 잡고 계산해보면 2^100 = 10^30 이 나오고,
10^30 > 6천만~2억 이므로 2초 시간 내에 10^30의 연산을 처리하기란 불가능하다고 판단했다. 

경우의 수가 너무 많아 DP를 의심해봤는데, 어떤 식으로 정보를 누적해야 할 지 감이 안왔다.
이 문제의 핵심 단서는 
- 각 물건을 넣는다/넣지않는다 중 하나로만 결정한다는 점과, 
- 배낭의 최대 무게 K라는 명확한 제한이 있다는 점이었다. 

즉 무게 별 최대 가치를 저장하는 dp배열을 하나 만들고(dp[w] = 무게 w 이하에서 얻을 수 있는 최대 가치를 저장하는 배열)
각 물건을 하나씩 보면서, 해당 물건을 (넣는경우/안넣는경우) 중 더 큰 가치로 dp를 갱신했다.

여기서 중요한 점은 dp를 갱신할 때 무게를 K부터 현재 물건의 무게까지 역순으로 순회해야 한다는 것이다.
현재 물건을 넣는 경우는 dp[w - weight] + value로 계산한다. 이때 dp[w - weight]는 “현재 물건을 넣기 전의 상태”여야 한다. 
그래야 현재 물건을 한 번만 사용한 결과가 된다.

만약 무게를 작은 값부터 큰 값으로 정순 순회하면, 이번 물건으로 갱신된 dp값을 같은 반복문 안에서 다시 참조하게 된다. 
예를 들어 무게 3, 가치 6인 물건이 하나만 있을 때 정순으로 돌면 dp[3]이 먼저 6으로 갱신되고, 
이후 dp[6]을 계산할 때 dp[3] + 6이 되어 같은 물건을 두 번 넣은 것처럼 처리될 수 있다.

하지만 이 문제는 각 물건을 넣거나 넣지 않는 0/1 배낭 문제이므로, 하나의 물건은 최대 한 번만 사용할 수 있다. 
따라서 큰 무게부터 작은 무게로 역순 순회하여, 이번 물건으로 아직 갱신되지 않은 이전 dp값만 참조하도록 해야 한다.
정말 많이 배웠다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static int N, K; //N:물품의 수, K:준서가 버틸 수 있는 최대무게
    static int[] itemWeights;
    static int[] itemValues;
    static int[] dp; // idx: W / value: W이하에서 최대 V합
    
    public static void main(String[] args) throws IOException {
        init();
       
        //각 물건을 하나씩 확인.
        for(int i=0; i<N; i++){
            int weight = itemWeights[i]; //현재 물건의 무게
            int value = itemValues[i]; //현재 물건의 가치
            
            for(int w=K; w>=weight; w--){
                dp[w] = Math.max(dp[w], dp[w - weight] + value);
            }
            
        }
        System.out.println(dp[K]);
    }
    
    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        
        itemWeights = new int[N];
        itemValues = new int[N];
        dp = new int[K+1];
        
        for(int i=0; i<N; i++){
            st = new StringTokenizer(br.readLine());
            itemWeights[i] = Integer.parseInt(st.nextToken());
            itemValues[i] = Integer.parseInt(st.nextToken());
        }
    }
}