class Solution {
    public int[] solution(int[] prices) {
        int[] answer = new int[prices.length];
        
        for(int i = 0; i < prices.length; i++){
            int second = calculateNotDroppedSeconds(prices, i);
            answer[i] = second;
        }
        
        return answer;
    }
    // 몇 초 간 가격이 안떨어졌는지 반환하는 함수
    private int calculateNotDroppedSeconds(int[] prices, int index){
        int basePrice = prices[index];
        int count = 0;
        
        for(int i = index + 1; i < prices.length; i++){ // 다음 요소부터 
            count++;
            if(prices[i] < basePrice){
                break;
            }
        }
        return count;
    }
}