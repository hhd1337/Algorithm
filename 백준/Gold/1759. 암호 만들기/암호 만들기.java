/*
BOJ 1759 - 암호 만들기
[조건]
1)암호는 서로 다른 L개의 알파벳 소문자들로 구성
2)최소 한 개의 모음(a, e, i, o, u)과 최소 두 개의 자음으로 구성되어 있음
3)암호를 이루는 알파벳은 오름차순

[접근]
1)선택지: 가능성 있는 문자들 char[]
2)선택: char[] 인덱스를 돌면서 하나씩 선택, password 끝에 추가
  - 조건)이전 문자보다 char[] 인덱스가 뒤인 문자만 선택(char배열 정렬해놓음)
3)재귀호출: 직전 인덱스를 다음 재귀함수로 넘김
4)선택 복구: password 끝에 문자 하나 제거
5)종료조건: password 길이 == L 이 되었을 때.
          (이때 password의 모음개수>=1 && 자음개수>=2 이면 password 출력)
*)무한루프 가능성: 없음.
--> 백트래킹

[느낀점]
변수를 선언할 때 어떤 자료형을 사용할까 성능 관점에서 굉장히 고민을 많이 했다. 
전형적인 백트래킹 문제였던 것 같다.
++ I/O 작업은 일반적인 메모리 연산보다 무겁다. 따라서 뭔가 많이 출력해야 할 때는 
StringBuilder에 모아서 한꺼번에 출력하는 것이 성능상 낫다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static final Set<Character> VOWELS = Set.of('a', 'e', 'i', 'o', 'u');
    
    static int L, C; // L:암호길이, C:사용된문자 수
    static List<Character> password = new ArrayList<>();
    static char[] candidateChars;
    static StringBuilder sb = new StringBuilder();
    
    public static void main(String[] args) throws IOException {
        init();
        backtrack(-1);
        System.out.print(sb);
    }
    
    private static void backtrack(int prev){
        //가지치기: candidateChars에서 prev뒤~마지막 인덱스까지 남은거 다 써도 암호길이 안되면 더 해보지 말고 리턴.
        int leftCandidateCount = C-1 - prev;
        if(password.size() + leftCandidateCount < L) return;
        
        //종료조건
        if(password.size() == L){
            if(vowelConsonantRule()){
                for(char ch: password){
                    sb.append(ch);
                }
                sb.append('\n');
            }
            return;
        }
        
        // 선택지
        for(int i=prev+1; i<C; i++){
            //선택
            password.add(candidateChars[i]);
            //재귀호출
            backtrack(i);
            //복구
            password.remove(password.size()-1);
        }
    }
    
    private static boolean vowelConsonantRule(){
        int vowelCount = 0;
        for(char ch : password){
            if(VOWELS.contains(ch)) vowelCount++;
        }
        return vowelCount >= 1 && L-vowelCount >= 2 ;
    }
    
    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        L = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        candidateChars = new char[C];
        
        st = new StringTokenizer(br.readLine());
        for(int i=0; i<C; i++){
            candidateChars[i] = st.nextToken().charAt(0);
        }
        
        Arrays.sort(candidateChars);
    }
}