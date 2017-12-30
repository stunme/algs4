import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import java.util.HashMap;
import java.util.LinkedList;

public class BaseballElimination {

   private final int n_teams;
   private final int[] w, l, r;
   private final int[][] g;
   private HashMap<String, Integer> teams; 

   private String cur = null;
   private boolean cur_elit;
   private LinkedList<String> subset;

   private FordFulkerson ff;


   public BaseballElimination(String filename){                    // create a baseball division from given filename in format specified below
      In in = new In(filename);
      String[] all = in.readAllLines();
      n_teams = Integer.parseInt(all[0]);
      
      teams = new HashMap<String, Integer>();
      
      w = new int[n_teams];
      l = new int[n_teams];
      r = new int[n_teams];
      g = new int[n_teams][n_teams];
      for (int i = 0; i < n_teams; i ++){
         String[] str = all[i+1].trim().split("\\s+");

         teams.put(str[0], i);
         w[i] = Integer.parseInt(str[1]);
         l[i] = Integer.parseInt(str[2]);
         r[i] = Integer.parseInt(str[3]);
         for (int j = 0; j < n_teams; j ++){
            g[i][j] = Integer.parseInt(str[j+4]);
         }
      }
        
   }

   public int numberOfTeams(){                        // number of teams
      return n_teams;
   }

   public Iterable<String> teams(){                                // all teams
      return teams.keySet();
   }

   public int wins(String team){                      // number of wins for given team
      if (!teams.containsKey(team)) throw new java.lang.IllegalArgumentException("invalid team name");
      return w[teams.get(team)];
   }


   public int losses(String team){                    // number of losses for given team
      if (!teams.containsKey(team)) throw new java.lang.IllegalArgumentException("invalid team name");
      return l[teams.get(team)];
   }

   public int remaining(String team){                 // number of remaining games for given team
      if(!teams.containsKey(team)) throw new java.lang.IllegalArgumentException("invalid team name");
      return r[teams.get(team)];
   }


   public int against(String team1, String team2){    // number of remaining games between team1 and team2
      if (!teams.containsKey(team1)) throw new java.lang.IllegalArgumentException("invalid team1 name");
      if (!teams.containsKey(team2)) throw new java.lang.IllegalArgumentException("invalid team2 name");
      if (team1.equals(team2)) return 0;
      return g[teams.get(team1)][teams.get(team2)];
   }

   public boolean isEliminated(String team){              // is given team eliminated?
      if (!teams.containsKey(team)) throw new java.lang.IllegalArgumentException("invalid team name");
      
      int c = teams.get(team);
      cur = team;

      
      subset = new LinkedList<String>();
      
      for (String str: teams()){
         if (!str.equals(team) && w[c]+r[c] < w[teams.get(str)]){
            subset.add(str);
         }
      }
      
      if (subset.size()>0){
         cur_elit = true;
         return true;
      }
      
      
      int v = 1 + n_teams;
      for (int i = 1; i < n_teams-1; i ++){
         v += i;
      }
      
      
      FlowNetwork network = new FlowNetwork(v);
      
      v = 1 + n_teams;
      int tmp, tmpi, tmpj;
      int value = 0;
      
      
      for (int i = 0; i < n_teams; i++){
         if ( i > c ){
            tmpi = i + 1;
         }else if(i < c) {
            tmpi = i + 2;
         }else {
            continue;
         }
         
         network.addEdge(new FlowEdge(tmpi, 1, w[c]+r[c]-w[i]));

         for (int j = i+1; j < n_teams; j ++){
            if ( j > c ){
               tmpj = j + 1;
            }else if(j < c) {
               tmpj = j + 2;
            }else {
               continue;
            }
            if(g[i][j] != 0) {
               network.addEdge(new FlowEdge(0, v, g[i][j]));
               value += g[i][j];
               network.addEdge(new FlowEdge(v, tmpi, Double.POSITIVE_INFINITY));
               network.addEdge(new FlowEdge(v, tmpj, Double.POSITIVE_INFINITY));

            }
            
            v ++;
         }
      }
      
      
     // StdOut.println(network.toString());
      
      ff = new FordFulkerson(network,0,1);
      
      cur_elit = ff.value()<value;
      
      if (cur_elit) {
         for (String str: teams()){
            tmp = teams.get(str);
            if (tmp > c ) {
               tmp += 1;
            } else if ( tmp < c ){
               tmp += 2;
            } else {
               continue;
            }
            if (ff.inCut(tmp)){
               subset.add(str);
            }
         }   
      }
      
      return cur_elit;
   }


   public Iterable<String> certificateOfElimination(String team){  // subset R of teams that eliminates given team; null if not eliminated
      if (!teams.containsKey(team)) throw new java.lang.IllegalArgumentException("invalid team name");
      if (!team.equals(cur)) { 
         isEliminated(team);
      }
      if (cur_elit){
         return subset;
      }
      return null;
   }

   public static void main(String[] args) {
       BaseballElimination division = new BaseballElimination(args[0]);
       for (String team : division.teams()) {
           if (division.isEliminated(team)) {
               StdOut.print(team + " is eliminated by the subset R = { ");
               for (String t : division.certificateOfElimination(team)) {
                   StdOut.print(t + " ");
               }
               StdOut.println("}");
           }
           else {
               StdOut.println(team + " is not eliminated");
           }
       }
   }

}
