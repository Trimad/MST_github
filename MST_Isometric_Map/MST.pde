//!!!8-BITS MAXIMUM!!!
class MST {

  ArrayList<Edge> edges = new ArrayList<Edge>();

  MST() {
  }

  //Maximum of 2^7 vertices
  public void kruskal(ArrayList<Node> list) {
    ArrayList<PVector> reached = new ArrayList();
    ArrayList<PVector> unreached = new ArrayList();
  }


  //Maximum of 2^7 vertices
  public void prim(ArrayList<Node> list) {

    ArrayList<PVector> reached = new ArrayList();
    ArrayList<PVector> unreached = new ArrayList();

    for (byte i = 0; i < list.size(); i++) {
      unreached.add(list.get(i).v);
    }

    reached.add(unreached.get(0));
    unreached.remove(0);

    while (unreached.size() > 0) {

      float longestDistance = Float.MAX_VALUE; //infinity

      short reachedIndex = 0;
      short unreachedIndex = 0;

      for (byte i = 0; i < reached.size(); i++) {
        for (byte j = 0; j < unreached.size(); j++) {
          PVector v1 = reached.get(i);
          PVector v2 = unreached.get(j);
          float d = dist(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);

          if (d < longestDistance) {
            longestDistance = d;
            reachedIndex = i;
            unreachedIndex = j;
          }
        }
      }

      PVector p1 = reached.get(reachedIndex);
      PVector p2 = unreached.get(unreachedIndex);
      edges.add(new Edge(p1, p2));
      reached.add(p2);
      unreached.remove(unreachedIndex);
    }
  }
  void show() {
    //Show the edges form the minimum spanning tree
    if (edges.size() > 1) {
      for (byte i = 0; i < edges.size(); i++) {
        edges.get(i).show();
      }
    }
  }
}
