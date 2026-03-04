/*
1~N까지 싹다 검사..? 
-> N이 '10^6 -1'일 경우(999999) 경우의수를 다 따져야 함.
-> 이정도면 완탐가능할듯.
*/

import java.util.*;
import java.io.*;

public class Main {
    private static int N, K, P, X;
    private static final boolean[][] NUM = {
        {true,true,true,false,true,true,true},   //0
        {false,false,true,false,false,true,false}, //1
        {true,false,true,true,true,false,true},  //2
        {true,false,true,true,false,true,true},  //3
        {false,true,true,true,false,true,false}, //4
        {true,true,false,true,false,true,true},  //5
        {true,true,false,true,true,true,true},   //6
        {true,false,true,false,false,true,false},//7
        {true,true,true,true,true,true,true},    //8
        {true,true,true,true,false,true,true}    //9
    };
    private static int[][] diff = new int[10][10];
    private static int answerCnt = 0;
    
    public static void main(String[] args) throws Exception {
        init();
        
        for(int floor=1; floor<=N; floor++){
            if(floor==X) continue;
            
            int x = X;
            int thisNum = floor;
            
            int sum = 0;
            
            for(int i=0; i<K; i++){
                int xDigit = x % 10;
                int floorDigit = thisNum % 10;
                
                sum += diff[xDigit][floorDigit];
                if(sum > P) break;
                
                x /= 10;
                thisNum /= 10;
            }
            if (1 <= sum && sum <= P){
                answerCnt++;
            }
        }
        
        System.out.println(answerCnt);
    }
    
    static int calDiff(int n1, int n2) {
        int cnt = 0;
        for (int i = 0; i < 7; i++) {
            if (NUM[n1][i] != NUM[n2][i]) cnt++;
        }
        return cnt;
    }
    
    public static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken()); // 최대층수
        K = Integer.parseInt(st.nextToken()); // 자리수
        P = Integer.parseInt(st.nextToken()); // 최대 몇개 바꿀 수 있나?
        X = Integer.parseInt(st.nextToken()); // 현재 층
        
        for(int n1=0; n1<=9; n1++){
            for(int n2=0; n2<=9; n2++){
                diff[n1][n2] = calDiff(n1, n2);
            }
        }
    }
}