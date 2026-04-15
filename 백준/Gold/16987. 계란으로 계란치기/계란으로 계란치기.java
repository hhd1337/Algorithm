/*
BOJ 16987 - 계란으로 계란치기

[조건]
1)계란이 1열로 가로로 쭉 나열되어있고, 왼쪽부터 계란을 하나씩 든다.
2)든 계란이 깨지지 않았다면, 혹은 나머지 계란이 다 깨지지 않았다면 멀쩡한 계란중에 하나 친다.
3)두 계란은 각각 상대 무게에 따른 자신의 내구도 감소한다. 내구도가 0이면 깨진다.
4)맨 마지막(맨 오른쪽)계란까지 들고, 내리쳤을때 남은 계란 개수 중 최소는 몇개일지 구하라.

[접근]
1. 경우의 수들을 완전 탐색을 해봐야 함.
2. 항상 각 단계에서 선택지가 있음, 선택/선택/선택..해서 생긴 세계관들 전부 탐색해봐야 하는 구조.(트리구조)
3. 무환순환 가능한가? no.
4. 백트래킹 구조와 맞는가?
   - 매 재귀의 선택지: 나를 제외한 안깨진 계란
   - 선택: 하나 선택해서 깸 -> 각 계란에 상대 attack 만큼 뺌!
   - 재귀호출: 방금 재귀때 손에 든 계란 인덱스 넘김
   - 선택복구: 아까 깼던거 복구 -> 각 계란에 상대 attack 만큼 다시 더함!
   - 종료조건: 이번 재귀의 계란 인덱스가 계란의 총 개수일때.
              이때 최소 egg깨진개수 = max(지금까지 최대 egg깨진개수, 이번에 egg 깨진개수) 로 갱신 
--> 백트래킹.

[느낀점]
1)이 문제는 가장 어려웠던 부분이 재귀에서 선택을 안 하고 넘어가는 경우에 대한 처리였다.
내가 익숙했던 백트래킹은 보통 '현재 단계에서 항상 하나는 고를 수 있다'는 전제가 있었기 때문에
선택지 리스트를 순회하는 for문안에서만 재귀호출이 일어났었다.
하지만 이 문제처럼 내 단계에서 선택지가 없을 경우, 선택지 리스트를 순회하기 전에
if (내 단계 선택지 없으면) {
    backtrack();
    return;
}
이렇게 재귀호출, return 으로 이번단계를 일축해야 한다는 것을 확실히 이해하고 익혔다.

2)또 종료조건에서 countBrokenEggs()로 전체 계란을 순회하는 것이 성능상 마음에 들지는 않았지만,
brokenEggCount를 전역변수로 두고 매 선택에서 갱신하려다 보니 백트래킹의 선택,재귀호출,복구 흐름이 
많이 어지러워졌다. 왜냐하면 brokenEggCount++를 하려면 (선택이전에 이미 깨져있지 않았음 && 선택이후에 깨진 상태임) 이렇게
두가지 정보가 필요해서 chosenEgg.isBroken()을 선택 전후로 호출하고, 마지막으로 종합해서 조건을 구성해야 했기 때문에
더 어지러워졌다. 그래서 계란이 최대 8개밖에 안한다는 것을 보고 그냥 종료 시점에 Egg배열을 순회하여 깨진계란을 세도록 했다.
8번 확인은 거저라고 생각되기도 해서 더 나은 방향이 맞다고 판단했다.

3)또 사소하지만, 원래 Egg.getAttack()을 this.hp = Math.min(0, this.hp - otherEggAttack); 
이렇게 구성했었는데, 이후 hp가 0이 된 경우 재귀 내 선택복구처리가 복잡해서 그냥 hp<0이 가능하도록 바꿨다.
otherEggAttack만큼 빠졌다가 다시 otherEggAttack만큼 더하면 원래 hp가 나타나도록 직관적으로 했다.
*/

import java.io.*;
import java.util.*;

public class Main {
    static int eggCount;
    static Egg[] eggs;
    static int maxEggBrokenCount;

    public static void main(String[] args) throws IOException {
        init();

        backtrack(-1);
        System.out.println(maxEggBrokenCount);
    }

    private static void backtrack(int prevIdx){
        int handEggIdx = prevIdx + 1;

        //종료조건: 마지막까지 왔거나, 다깨지고 손에 든거 하나남았거나, 이전 턴에 친 계란이 동시소멸했거나.
        int brokenEggCount = countBrokenEggs();
        if(handEggIdx == eggCount || brokenEggCount >= eggCount-1){
            maxEggBrokenCount = Math.max(maxEggBrokenCount, brokenEggCount);
            return;
        }

        // 내 계란 깨졌으면 이번 선택지는 없음. 그냥 넘어감
        if (eggs[handEggIdx].isBroken()) {
            backtrack(handEggIdx);
            return;
        }

        //매 재귀의 선택지 : 나를 제외한 안깨진 계란
        for(int i=0; i<eggCount; i++){
            if(i==handEggIdx) continue;
            if(eggs[i].isBroken()) continue;
            
            // 선택: 하나 선택 후 깸
            Egg chosenEgg = eggs[i];
            Egg hand = eggs[handEggIdx];
            
            hand.getAttack(chosenEgg.attack);
            chosenEgg.getAttack(hand.attack);

            //재귀호출
            backtrack(handEggIdx);

            //선택복구
            hand.restoreAttack(chosenEgg.attack);
            chosenEgg.restoreAttack(hand.attack);
        }
    }

    private static int countBrokenEggs(){
        int count = 0;
        for(int i=0; i<eggCount; i++){
            if(eggs[i].isBroken()) count++;
        }
        return count;
    }

    private static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        eggCount = Integer.parseInt(br.readLine());
        eggs = new Egg[eggCount];

        StringTokenizer st;
        for(int i=0; i<eggCount; i++){
            st = new StringTokenizer(br.readLine());

            int hp = Integer.parseInt(st.nextToken());
            int attack = Integer.parseInt(st.nextToken());

            eggs[i] = new Egg(hp, attack);
        }
    }

    static class Egg {
        int hp;
        int attack;

        public Egg(int hp, int attack){
            this.hp = hp;
            this.attack = attack;
        }

        public void getAttack(int otherEggAttack){
            this.hp -= otherEggAttack;
        }

        public void restoreAttack(int otherEggAttack){
            this.hp += otherEggAttack;
        }

        public boolean isBroken(){
            return hp <= 0;
        }
    }
}