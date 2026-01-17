import java.util.*;

class Solution {
    public int solution(String s) {
        int count = 0;
        
        for(int i = 0; i < s.length(); i++){
            String rotatedString = rotateParentheses(s, i);
            if(isCorrectParentheses(rotatedString)){
                count++;
            }
        }
        
        return count;
    }
    
    private String rotateParentheses(String s, int count){
        int length = s.length();
        int lastLoopRotationCount = count % length; // 여러번 회전했을 경우 나머지(마지막 바퀴 돈 횟수)만 사용
        
        return s.substring(lastLoopRotationCount) + s.substring(0, lastLoopRotationCount);
    }
    
    private boolean isCorrectParentheses(String s){
        ArrayDeque<Character> stack = new ArrayDeque<>();
        
        for(char ch : s.toCharArray()){
            if(ch == '('||ch == '{'||ch == '['){
                stack.push(ch);
                continue;
            }
            
            if(stack.isEmpty()){
                return false;
            }
            if(ch == ')' && stack.peek() != '('){
                return false;
            }
            if(ch == '}' && stack.peek() != '{'){
                return false;
            }
            if(ch == ']' && stack.peek() != '['){
                return false;
            }
            
            stack.pop();
        }
        
        return stack.isEmpty();
    }
}