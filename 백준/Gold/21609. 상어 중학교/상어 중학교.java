import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int M;
    static int[][] board;

    static final int BLACK = -1;
    static final int EMPTY = -2;
    static final int RAINBOW = 0;

    // 상,하,좌,우 방향 -> 데카르트 좌표의 x,y 따름
    static final int[] dx = {0, 0, -1, 1};
    static final int[] dy = {1, -1, 0, 0};

    public static void main(String[] args) throws IOException {
        initBoard();
        int score = 0;

        while (true) {
            Group group = getBestGroupOrNull();
            if (group == null) {
                break;
            }
            score += group.getScore();
            removeGroup(group);
            applyGravity();
            rotateBoard();
            applyGravity();
        }

        System.out.println(score);
    }

    static void removeGroup(Group group) {
        for (Block block : group.blockList) {
            board[block.y][block.x] = EMPTY;
        }
    }

    static void initBoard() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        board = new int[N][N];
        for (int y = 0; y < N; y++) {
            st = new StringTokenizer(br.readLine());
            for (int x = 0; x < N; x++) {
                board[y][x] = Integer.parseInt(st.nextToken());
            }
        }
    }

    static void applyGravity() {
        for (int x = 0; x < N; x++) {                 // 열마다 처리
            for (int y = N - 2; y >= 0; y--) {        // 아래칸이 있으려면 y는 N-2부터
                if (board[y][x] == BLACK) {
                    continue;   // 검은 블록은 고정
                }
                if (board[y][x] == EMPTY) {
                    continue;   // 빈칸은 떨어질 것도 없음
                }

                // 현재 블록을 아래로 갈 수 있을 때까지 계속 내림
                int currY = y;
                while (currY + 1 < N && board[currY + 1][x] == EMPTY) {
                    board[currY + 1][x] = board[currY][x];
                    board[currY][x] = EMPTY;
                    currY++;
                }
            }
        }
    }

    static void rotateBoard() {
        int[][] rotated = new int[N][N];
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                rotated[y][x] = board[x][N - 1 - y];
            }
        }
        board = rotated;
    }

    static Group getBestGroupOrNull() {
        Group bestGroup = null;

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                // 일반블록 아니거나 그룹이 안만들어지면 넘어감
                if (board[y][x] <= 0) {
                    continue;
                }
                Group g = getGroupOrNull(x, y);
                if (g == null) {
                    continue;
                }

                if (g.betterThan(bestGroup)) {
                    bestGroup = g;
                }
            }
        }
        return bestGroup;
    }

    static Group getGroupOrNull(int sx, int sy) {
        int startColor = board[sy][sx]; // 기준좌표 색

        boolean[][] visited = new boolean[N][N];
        Queue<int[]> bfsQueue = new ArrayDeque<>();

        // 시작점 방문처리, 큐에 넣어놓음
        visited[sy][sx] = true;
        bfsQueue.add(new int[]{sx, sy});

        List<Block> blockList = new ArrayList<>();
        int rainbowCount = 0;

        while (!bfsQueue.isEmpty()) {
            int[] curr = bfsQueue.poll();

            int cx = curr[0];
            int cy = curr[1];
            int currColor = board[cy][cx];

            blockList.add(new Block(cx, cy, currColor)); // 큐에 있던건 검증 된것들이니 poll() 시점에 add
            if (currColor == RAINBOW) {
                rainbowCount++;
            }

            // (cx,cy) 상하좌우 4방향으로 확장하며 탐색
            for (int d = 0; d < 4; d++) {
                int nx = cx + dx[d];
                int ny = cy + dy[d];

                if (!inRange(nx, ny) || visited[ny][nx]) { // 범위 벗어나거나, visited이면 무시
                    continue;
                }

                int nextColor = board[ny][nx];
                if (nextColor != startColor && nextColor != RAINBOW) { // 같은색 or 무지개색만 큐에 추가
                    continue;
                }

                // 방문처리, 큐에 넣어서 이후에 이 칸 기준으로도 탐색 이어나감
                visited[ny][nx] = true;
                bfsQueue.add(new int[]{nx, ny});
            }
        }

        if (blockList.size() < 2) { // 그룹에 속한 블록의 개수는 2이상이어야 함
            return null;
        }

        Block standardBlock = findStandardBlock(blockList);

        return new Group(blockList, rainbowCount, standardBlock);
    }

    static Block findStandardBlock(List<Block> blockList) {
        Block standardBlock = null;

        for (Block block : blockList) {
            if (block.color == RAINBOW) { // 무지개 블록은 기준 블록 안됨
                continue;
            }
            // 맨 처음 블록 하나 잡음 (초기화)
            if (standardBlock == null) {
                standardBlock = block;
                continue;
            }

            // 1. 행(y)이 더 작은 블록
            // 2. 행이 같다면, 열(x)이 더 작은 블록
            if (block.y < standardBlock.y || (block.y == standardBlock.y && block.x < standardBlock.x)) {
                standardBlock = block;
            }
        }

        // 최종적으로 선택된 기준 블록 반환
        // (이론상 null이면 안 됨: 그룹 시작점은 항상 일반 블록이기 때문)
        return standardBlock;
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }
}

class Group {
    List<Block> blockList;
    int rainbowCount;
    Block standardBlock;

    public Group(List<Block> blockList, int rainbowCount, Block standardBlock) {
        this.blockList = blockList;
        this.rainbowCount = rainbowCount;
        this.standardBlock = standardBlock;
    }

    public int getScore() {
        return (int) Math.pow(blockList.size(), 2);
    }

    public boolean betterThan(Group other) {
        if (other == null) {
            return true;
        }
        if (this.getSize() != other.getSize()) { // 1. 블럭개수비교
            return this.getSize() > other.getSize();
        }
        if (this.rainbowCount != other.rainbowCount) { // 2. 무지개블럭 개수비교
            return this.rainbowCount > other.rainbowCount;
        }
        if (this.standardBlock.y != other.standardBlock.y) { // 3. 기준블록 행 큰 것
            return this.standardBlock.y > other.standardBlock.y;
        }
        return this.standardBlock.x > other.standardBlock.x; // 4. 기준블록 열 큰 것
    }

    private int getSize() {
        return blockList.size();
    }
}

class Block {
    int x;
    int y;
    int color;

    public Block(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}