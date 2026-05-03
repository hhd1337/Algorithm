/*
PGS 42627 - 디스크 컨트롤러

[조건]
1) 대기 큐: 작업번호, 작업 요청시각, 작업 소요시간 저장.
2) 대기큐에서는 (작업소요시간 짧은것 > 작업 요청시각 빠른것 > 작업번호 작은것) 순으로 우선순위 높다.
3) 디스크 컨트롤러: 하드디스크가 작업을 하고 있지 않고, 대기 큐가 비어있지 않으면 
   가장 우선순위가 높은 작업을 대기큐에서 꺼내서 하드디스크에게 시킴
4) 하드디스크: 작업을 한번 시작하면 마칠때까지 그 작업만 수행함.
5) 모든 요청 작업의 반환 시간의 평균의 정수부분을 return하라.

[접근]
- Work 클래스 작성(작업번호, 작업 요청시각, 작업 소요시간),
  Comparable<Work> 구현함, 작업 우선순위에 따라 compareTo() 재정의함.
- 대기 큐는 우선순위큐로 함.
- job을 요청시각에 맞게 하나씩 대기 큐에 넣음. ...

[느낀점]
1)처음에 Work를 담을 우선순위 대기 큐를 PriorityQueue와 TreeSet 중 어떤 자료구조로 구현할지 고민했다. 

- TreeSet도 Work에 Comparable을 구현하고 compareTo()를 오버라이딩하면 소요시간, 요청시각, 작업번호 순으로 정렬할 수 있다. 
  내부적으로 레드-블랙 트리를 사용하므로 값을 넣을 때마다 정렬 기준에 맞게 저장되고, 
  pollFirst()로 가장 우선순위가 높은 작업을 꺼낼 수 있다.
  
- PriorityQueue는 내부적으로 힙 구조를 사용한다. 
  힙은 TreeSet처럼 전체 원소를 완전히 정렬된 상태로 유지하지는 않지만, 
  poll() 시점에 가장 우선순위가 높은 원소가 나오도록 관리한다. 
  따라서 전체 정렬이 필요한 상황보다는 최우선 원소를 반복해서 꺼내는 상황에 적합하다. 
  
  이 문제에서는 특정 작업을 검색하거나 임의 삭제할 필요가 없고, 
  현재 시점까지 요청된 작업 중 가장 우선순위가 높은 작업 하나를 꺼내는 연산만 필요했다. 
  TreeSet의 전체 정렬 유지나 탐색 기능은 이 문제에서는 거의 사용되지 않는다. 
  또한 두 자료구조 모두 삽입과 최우선 원소 제거는 O(log N)이지만, 
  PriorityQueue는 배열 기반 힙을 사용하므로 TreeSet보다 구조가 단순하고, 
  노드 객체나 트리 균형 조정에 드는 부가 비용이 상대적으로 적다. 
  입력 크기가 작아 실제 성능차이는 작을 수 있지만, 
  문제의 요구사항만 놓고 보면 PriorityQueue가 더 가볍고 적합한 자료구조라고 판단했다.

*/
import java.util.*;

class Solution {
    private PriorityQueue<Work> pq = new PriorityQueue<>();
    
    public int solution(int[][] jobs) { //[[작업요청시점,작업소요시간], ... ] // idx:작업번호 -> 대기큐에 그대로 들어감.
        int jobCount = jobs.length;
        Work[] works = new Work[jobCount];
        for(int i=0; i<jobCount; i++){
            works[i] = new Work(i, jobs[i][0], jobs[i][1]);
        }
    
        Arrays.sort(works, (a, b) -> {
            if (a.requestTime != b.requestTime) {
                return a.requestTime - b.requestTime;
            }
            return a.id - b.id;
        });
        
        PriorityQueue<Work> waitQ = new PriorityQueue<>();
        
        int time = 0;
        int index = 0;
        int completedCount = 0;
        int totalTurnaroundTime = 0;
        
        while (completedCount < jobCount) {

            //현재 시간까지 요청된 작업들을 모두 대기 큐에 넣는다.
            while (index < jobCount && works[index].requestTime <= time) {
                waitQ.offer(works[index]);
                index++;
            }

            //현재 실행할 수 있는 작업이 없다면, 다음 작업 요청 시각으로 넘어감
            if (waitQ.isEmpty()) {
                time = works[index].requestTime;
                continue;
            }
            
            Work current = waitQ.poll();

            //작업을 끝까지 수행한다.
            time += current.duration;

            //반환 시간 = 작업 종료 시각 - 작업 요청 시각
            totalTurnaroundTime += time - current.requestTime;

            completedCount++;
        }
            
        return totalTurnaroundTime / jobCount;
    }
    
    static class Work implements Comparable<Work> {
        int id;
        int requestTime;
        int duration;

        public Work(int id, int requestTime, int duration){
            this.id = id;
            this.requestTime = requestTime;
            this.duration = duration;
        }

        @Override
        public int compareTo(Work other){
            //작업소요시간 짧은것 > 작업 요청시각 빠른것 > 작업번호 작은것 순.
            if(this.duration != other.duration){
                return this.duration - other.duration; //내가 더 크면 양수반환
            }
            if(this.requestTime != other.requestTime){
                return this.requestTime - other.requestTime;
            }

            return this.id - other.id;
        }
    }
    
}