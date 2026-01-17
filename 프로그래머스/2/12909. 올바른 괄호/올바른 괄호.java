import java.util.*;

class Solution {
    boolean solution(String s) {
        ArrayDeque<Character> stack = new ArrayDeque<>();
        
        for(char ch : s.toCharArray()){
            if(ch == '('){
                stack.push(ch);
                continue;
            }
            // ch == ')'일때
            else{
                if(stack.isEmpty()){
                    return false;
                }
                stack.pop();
            }
        }
        
        return stack.isEmpty();
    }
}