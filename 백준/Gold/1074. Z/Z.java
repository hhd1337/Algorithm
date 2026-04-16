/*
BOJ 1074 - Z

[조건]
1)2^N × 2^N인 2차원 배열을 Z모양으로 탐색한다. (r,c)는 몇번째로 탐색되는가?
2)예시를 보면 0행, 0열부터 시작한다.

[접근]
1)
0 | 1
-----   <= 이렇게 Z탐색순서에 맞게 0,1,2,3 사분면 정의.
2 | 3

2)보드를 4등분했을 때 r,c가 0,1,2,3사분면 중 어디에 위치하는지 알아냄 ->사분면 번호 * 사분면 크기 누적합.
3)또 해당 사분면을 4등분했을 때 r,c가 0,1,2,3사분면 중 어디에 위치하는지 알아냄 ->사분면 번호 * 사분면 크기 누적합
... 같은 문제 반복되고, 다음 단계로 상태를 넘기기만 하면 되는 구조
4)종료조건: 해당 사분면 크기가 1인경우가 마지막 try임.
--> 재귀

[재귀 3구조]
1)이 함수 한번은 무엇을 처리한다: 보드를 4등분했을 때 r,c가 0,1,2,3사분면 중 어디에 위치하는지 알아내고 사분면 번호 * 사분면 크기 누적합한다.
2)종료조건: 보드 한 변 길이 == 1 이면 사분면 계산하지 말고 즉시 종료한다. 
3)다음 호출로 무엇을 넘긴다: 사분면길이, r%(사분면길이), c%(사분면길이)

[느낀점]
1)재귀 문제는 함수를 어떻게 설계해야 할 지 항상 감이 쉽게 오지 않는다.
그래서 이번 기회에 재귀함수를 짤 때 반복되는 3구조를 한번 정립해 봤다.
이 문제를 풀기 전에도 3구조에 대한 생각만 했을 뿐인데 재귀함수를 어떻게 구성해야 할 지 감이 왔다. 
앞으로 이 [재귀 3구조]를 적고 시작하면 재귀함수 구성에 도움이 될 것 같다.

2)또 boolean cBigger = quadrantSize <= c; 이 부분에서 등호를 붙이지 않아 한동안 테스트에 실패했다.
처음에는 단순히 절반보다 크면 오른쪽(아래쪽) 사분면이라고 생각했는데, 
4×4 보드를 절반으로 나눠 직접 확인해 보니 열 인덱스는 0,1/2,3으로 나뉘었다.
즉 사분면 한변 길이가 2일 때, 인덱스 2부터는 이미 오른쪽(아래쪽) 사분면에 속한다.
이 경계값 하나를 놓쳐서 오래 헤맸는데, 결국 분할 기준이 되는 실제 인덱스 범위를 
작은 예시로 직접 써보는 것이 중요하다는 걸 느꼈다.
앞으로 경계값이 중요한 문제를 풀 때는 무조건 작은 입력을 직접 그려 보고, 
포함되는 쪽이 어디인지 먼저 확인한 뒤 조건식을 세워야겠다는 것을 확실히 익혔다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static int boardLength; // 보드 한 변 크기
    static int r, c; //문제에서 정의된 대로 0-based 타겟칸 좌표
    static int sum;
    
    public static void main(String[] args) throws IOException {
        init();
        
        recursion(boardLength, r, c);
        System.out.println(sum);
    }
    
    private static void recursion(int size, int r, int c){
        //종료조건
        if(size == 1) return;
        
        //이 함수가 담당하는 문제
        int quadrantSize = size/2;
        boolean cBigger = quadrantSize <= c;
        boolean rBigger = quadrantSize <= r;

        //0사분면
        if(!cBigger && !rBigger){
            //sum += (quadrantSize * quadrantSize) * 0;
            recursion(quadrantSize, r%quadrantSize, c%quadrantSize);
        }
        //1사분면
        if(cBigger && !rBigger){
            sum += (quadrantSize * quadrantSize) * 1;
            recursion(quadrantSize, r%quadrantSize, c%quadrantSize);
        }
        //2사분면
        if(!cBigger && rBigger){
            sum += (quadrantSize * quadrantSize) * 2;
            recursion(quadrantSize, r%quadrantSize, c%quadrantSize);
        }
        //3사분면
        if(cBigger && rBigger){
            sum += (quadrantSize * quadrantSize) * 3;
            recursion(quadrantSize, r%quadrantSize, c%quadrantSize);
        }
    }

    
    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int N = Integer.parseInt(st.nextToken());
        boardLength = (int) Math.pow(2, N);
        
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
    }
}