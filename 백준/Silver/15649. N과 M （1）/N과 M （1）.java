/*
BOJ 15649 - N과 M (1)
[조건]
1) 1부터 N까지 자연수 중에서 중복 없이 M개를 고른 수열
[접근]
백트래킹
1) 현재 수열에서 추가할 수 있는 자연수를 탐색
2) 자연수가 기존 수열에서 이미 사용한 수라면, 해당 수를 선택한 탐색은 진행하지 않고 이전 단계로 돌아감.
3) 수열의 길이가 M이 될 때 해당 수열의 정보 출력
[느낀점]

*/

import java.io.*;
import java.util.*;

public class Main {
    static int N,M;
    static boolean[] visited;
    static int[] sequence;
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken()); // 1부터 N까지
        M = Integer.parseInt(st.nextToken()); // 중복 없이 M개를 고른 수열
        sequence = new int[M];
        visited = new boolean[N+1]; // idx: 자연수
        
        backtracking(0);
    }
    
    private static void backtracking(int length){
        if(length == M) { // 종료조건: 정답인지 확인해서 리턴.
            printArray();
            return;
        }
        
        for(int i=1; i<=N; i++){ // 갈 수 있는 모든 선택지 확인
            if(!visited[i]){
                visited[i] = true;
                sequence[length] = i;
                
                backtracking(length+1);
                
                visited[i] = false;
            }
        }
    }
    
    private static void printArray(){
        for(int i=0; i<M-1; i++){
            System.out.print(sequence[i] + " ");
        }
        System.out.println(sequence[M-1]);
    }
}