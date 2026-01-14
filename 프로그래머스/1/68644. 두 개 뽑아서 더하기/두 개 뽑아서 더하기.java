import java.util.HashSet;

class Solution {
    public int[] solution(int[] numbers) {
        int[] answer = {};
        
        HashSet<Integer> sumSet = new HashSet<>();
        
        for(int i = 0; i < numbers.length - 1; i++){
            for(int j = i + 1; j < numbers.length; j++){
                sumSet.add(numbers[i] + numbers[j]);
            }
        }
        
        return sumSet.stream()
            .sorted()
            .mapToInt(Integer::intValue)
            .toArray();
    }
}