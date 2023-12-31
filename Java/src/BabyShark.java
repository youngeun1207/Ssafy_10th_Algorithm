import java.util.*;
import java.io.*;

/**
 * 백준 16236 아기상어
 * https://www.acmicpc.net/problem/16236
 * 
 * @author youngeun
 * <pre>
 * BFS 사용
 * BFS로 먹을 수 있는 먹이까지 가는데 걸리는 시간 구함
 * 가장 가까운 먹이 > 위 우선 > 왼쪽 우선 조건 충족을 위해 먹을 수 있는 물고기를 비교해서 최종 물고기 선정
 * 메모리 초과 방지를 위해 queue에서 pop할 때 visited 체크하는 것이 아닌 queue에 넣을 때 visited 체크함!!!
 * </pre>
 */
public class BabyShark {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    static int N;
    static int[][] ocean;
    static int[] shark;
    static int size;
    static Deque<Fish> queue;
    static int[][] dirs = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}}; // 위 -> 왼 -> 오른 -> 아래 순으로 이동;
    static int answer;

    public static void main(String[] args) throws IOException {
        N = Integer.parseInt(br.readLine());

        ocean = new int[N][N]; // 물고기 위치 저장
        shark = new int[2]; // 현재 상어 위치 저장
        size = 2; // 현재 상어 크기 저장
        queue = new ArrayDeque<>(); // BFS용 이동 위치 저장 queue

        answer = 0;

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                ocean[i][j] = Integer.parseInt(st.nextToken());
                if (ocean[i][j] == 9) { // 상어 초기 위치
                    ocean[i][j] = 0;
                    shark[0] = i;
                    shark[1] = j;
                }
            }
        }

        int cnt = 0; // 물고기 몇마리 먹었는지 저장 -> 현재 크키만큼 먹으면 0으로 리셋하고 상어 크기++
        while (true) {
            queue.offer(new Fish(0, shark[0], shark[1]));
            Fish result = BFS();
            int addTime = result.distance;
            int[] location = {result.y, result.x};

            if (addTime > 0) {
                cnt++;
                answer += addTime;
                shark = location;

                if (cnt == size) {
                    size++;
                    cnt = 0;
                }
            } else {
                break;
            }
        }
        System.out.println(answer);
    }

    public static Fish BFS() {
        Fish ate = new Fish(100000, 100000, 100000); // 먹을 수 있는 물고기 중 가장 우선순위 높은 물고기 저장
        boolean[][] visited = new boolean[N][N];

        while (!queue.isEmpty()) {
            Fish current = queue.poll();
            int time = current.distance;
            int nowY = current.y;
            int nowX = current.x;

            for (int[] dir : dirs) {
                int y = nowY + dir[0];
                int x = nowX + dir[1];
                if (y >= 0 && y < N && x >= 0 && x < N && !visited[y][x]) { // 이동할 수 있음
                    if (ocean[y][x] == 0 || ocean[y][x] == size) {
                        queue.offer(new Fish(time + 1, y, x));
                    } else if (ocean[y][x] < size) { // 먹을 수 있음
                    	Fish newFish = new Fish(time + 1, y, x);
                    	ate = CompareFish(ate, newFish); // 가장 우선순위 높은 물고기로 변경
                    }
                    visited[y][x] = true; // 여기서 방문 체크 해야 중복 방지로 메모리 초과 안됨!!!!
                }
                visited[nowY][nowX] = true;
            }
        }
        if (ate.y < N) { // 먹을 수 있는 물고기가 있으면
            ocean[ate.y][ate.x] = 0;
            return ate; // 이동에 걸린 시간, 이동한 위치 반환
        } else { // 먹을 수 있는 물고기가 없음 -> 엄마 불러
            return new Fish(-1, -1, -1);
        }
    }

    public static class Fish {
        int distance;
        int y;
        int x;

        public Fish(int distance, int y, int x) {
            super();
            this.distance = distance;
            this.y = y;
            this.x = x;
        }
    }
    
    public static Fish CompareFish(Fish a, Fish b) {
    	if (a.distance != b.distance) { // 거리
    		if(a.distance > b.distance) {
    			return b;
    		}else {
    			return a;
    		}
    	} else if (a.y != b.y) { // 위 우선
    		if(a.y > b.y) {
    			return b;
    		}else {
    			return a;
    		}
    	} else { // 왼쪽 우선
    		if(a.x > b.x) {
    			return b;
    		}else {
    			return a;
    		}
        }
    }
}
