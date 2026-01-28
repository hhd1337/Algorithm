// [구현 아이디어 2]
// 1.그냥 먼저 report에서 중복신고 다 distinct()로 제거해버림
// 2.신고당한 횟수 집계
// 3.신고당한 횟수가 k이상인 유저 Set 만듦
// 4.mailCount 세서 배열에 담아 반환
// -> 그런데 이 방법은 시간복잡도 많이 큼

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

class Solution {
    public int[] solution(String[] id_list, String[] report, int k) {
        List<String> distinctReport = Arrays.stream(report).distinct().collect(Collectors.toList());
        HashMap<String, Integer> reportedCount = new HashMap<>();

        // 신고당한 횟수 집계, 블랙리스트 만듦
        for(String r : distinctReport){
            String target = r.split(" ")[1];
            reportedCount.put(target, reportedCount.getOrDefault(target, 0) + 1);
        }

        Set<String> blackListSet = reportedCount.entrySet().stream()
                .filter(e -> e.getValue() >= k)
                .map(e -> e.getKey())
                .collect(Collectors.toSet());

        int[] mailCounts = new int[id_list.length];

        for(int i=0; i<id_list.length; i++){
            final String user = id_list[i];

            int mailCount = (int) distinctReport.stream()
                    .filter(s -> s.startsWith(user+ " ")) // 이 유저가 신고한 사람만 필터링
                    .filter(s -> blackListSet.contains(s.split(" ")[1])) // 신고한 사람이 블랙리스트에 있는 경우만 필터링
                    .count();

            mailCounts[i] = mailCount;
        }

        return mailCounts;
    }
}