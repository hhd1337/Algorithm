import java.util.*;

class Solution {
    public String solution(int initRowCount, int k, String[] cmd) {
        // 삭제된 행의 인덱스를 저장하는 스택
        Stack<Integer> deletedRowKeys = new Stack<>();
        
        // 각 행을 기준으로 cmd 연산에 따라 변한 위치를 표시하기 위한 배열. (내 위는 몇번? 내 아래는 몇번?)
        // 행 번호를 노드로 갖는 양방향 연결 리스트 를 배열로 구현한 것.
        int[] up = new int[initRowCount+2];
        int[] down = new int[initRowCount+2];
        
        for(int i=0; i < initRowCount+2; i++){
            up[i] = i - 1;
            down[i] = i + 1;
        }
        // 구현 시 1-based 인덱스를 쓸거라 우선 ++ 해줌
        k++;
        
        // 명령에 따라 연산 수행
        for(String c : cmd){
            if (c.startsWith("C")) {
                deletedRowKeys.push(k);
                up[down[k]] = up[k];
                down[up[k]] = down[k];
                // 삭제한 행의 아래 행을 선택하되, 그게 마지막 행 너머(가짜노드)라면 위 행을 선택
                k = initRowCount < down[k] ? up[k] : down[k];
            }
            else if (c.startsWith("Z")) {
                int restore = deletedRowKeys.pop();
                down[up[restore]] = restore;
                up[down[restore]] = restore;
            }
            else {
                String[] splitedCmd = c.split(" ");
                int count = Integer.parseInt(splitedCmd[1]);
                
                for(int i = 0; i < count; i++){
                    if(splitedCmd[0].equals("U")){
                        k = up[k];
                    }
                    if(splitedCmd[0].equals("D")){
                        k = down[k];
                    }
                }
            }
        }
        
        // OX문자열 생성
        char[] answer = new char[initRowCount];
        Arrays.fill(answer, 'O');
        
        for(int i : deletedRowKeys){
            answer[i-1] = 'X';
        }
        
        return new String(answer);
    }
}


// 1차 시도 풀이 
// 풀이 설계 및 접근을 잘하자. 문제도 꼼꼼히 읽자.
// 어떤 알고리즘/자료구조를 사용하면 좋을 지 심사숙고 후 구현시작할 것.

// import java.util.*;
// import java.util.stream.Collectors;
// import java.time.LocalDateTime;

// class Solution {
//     private Table table = new Table(); // 테이블 먼저 초기화

//     public String solution(int recordCount, int initRecordKey, String[] cmd) {
//         int currKey = initRecordKey;   // 0-base

//         for (String unitCmd : cmd) {
//             String[] splitCmd = unitCmd.split(" ");

//             if (splitCmd[0].equals("U")) {
//                 currKey -= Integer.parseInt(splitCmd[1]); // 위
//             }
//             if (splitCmd[0].equals("D")) {
//                 currKey += Integer.parseInt(splitCmd[1]); // 아래
//             }
//             if (splitCmd[0].equals("C")) {
//                 boolean wasLastRecord = table.isLastAliveRecord(currKey); // 삭제 전 기준으로 판단해야 함
//                 table.deleteRecord(currKey);

//                 if (wasLastRecord) {
//                     // 마지막 행이었다 → 바로 위 행 선택
//                     // 위로 올라가면서 삭제되지 않은 행을 찾는 로직 필요
//                     currKey = table.findPrevAlive(currKey);
//                 } else {
//                     // 아래 행 선택
//                     currKey = table.findNextAlive(currKey);
//                 }
//             }
//             if (splitCmd[0].equals("Z")) {
//                 table.restoreRecord();
//             }
//         }

//         return makeResultString(recordCount);
//     }

//     private String makeResultString(int recordCount) {
//         List<Integer> deletedRecordKeys = table.getDeletedRecordKeys();
//         StringBuilder sb = new StringBuilder();

//         for (int i = 0; i < recordCount; i++) {
//             if (deletedRecordKeys.contains(i)) {
//                 sb.append('X');
//             } else {
//                 sb.append('O');
//             }
//         }
//         return sb.toString();
//     }
// }

// class Record {
//     private final int initKey;          // 0-base
//     private int currKey;                // 0-base
//     private String name;
//     private boolean deleted;
//     private LocalDateTime deletedAt;

//     public Record(int initKey, String name){
//         this.initKey = initKey;
//         this.currKey = initKey;
//         this.name = name;
//         this.deleted = false;
//         this.deletedAt = null;
//     }

//     public boolean isDeleted(){
//         return this.deleted;
//     }

//     public int getInitKey(){
//         return this.initKey;
//     }

//     public int getCurrKey() {
//         return this.currKey;
//     }

//     public LocalDateTime getDeletedAt() {
//         return this.deletedAt;
//     }

//     public void deleteRecord(){
//         this.deleted = true;
//         this.deletedAt = LocalDateTime.now();
//     }

//     public void restoreRecord(){
//         this.deleted = false;
//         this.deletedAt = null;
//     }

//     public boolean isBehind(int key){
//         return key < this.currKey;
//     }

//     public boolean isSameOrBehind(int key){
//         return key <= this.currKey;
//     }

//     public void decreaseCurrKey(){
//         this.currKey--;
//     }

//     public void increaseCurrKey(){
//         this.currKey++;
//     }
// }

// class Table {
//     private List<Record> recordList;

//     public Table(List<Record> recordList){
//         recordList = new ArrayList<>();
//     }

//     public List<Integer> getDeletedRecordKeys(){
//         return recordList.stream()
//             .filter(Record::isDeleted)
//             .map(Record::getInitKey)
//             .collect(Collectors.toList());
//     }

//     public void deleteRecord(int currKey){ // 0-base
//         recordList.get(currKey).deleteRecord();

//         recordList.stream()
//             .filter(record -> !record.isDeleted())
//             .filter(record -> record.isBehind(currKey))
//             .forEach(Record::decreaseCurrKey);
//     }

//     public boolean isIndexOutOfBound(int currKey) {
//         return currKey < 0 || currKey >= recordList.size();
//     }

//     public boolean isLastAliveRecord(int currKey) {
//         // currKey 아래에 삭제되지 않은 레코드가 하나라도 있으면 false
//         for (int i = currKey + 1; i < recordList.size(); i++) {
//             if (!recordList.get(i).isDeleted()) {
//                 return false;
//             }
//         }
//         return true;
//     }


//     public void restoreRecord(){
//         Record lastDeletedRecord = recordList.stream()
//             .filter(Record::isDeleted)
//             .max(Comparator.comparing(Record::getDeletedAt))
//             .orElse(null);

//         if (lastDeletedRecord == null) {
//             throw new IllegalArgumentException("아직 삭제된 레코드가 없습니다.");
//         }

//         int restoredIndex = lastDeletedRecord.getCurrKey();
//         lastDeletedRecord.restoreRecord();

//         recordList.stream()
//             .filter(record -> !record.isDeleted())
//             .filter(record -> record != lastDeletedRecord)
//             .filter(record -> record.isSameOrBehind(restoredIndex))
//             .forEach(Record::increaseCurrKey);
//     }
// }
