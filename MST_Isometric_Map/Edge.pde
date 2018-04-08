class Edge {

  PVector p1;
  PVector p2;
  float weight = 0;

  Edge(PVector _p1, PVector _p2, float _weight) {
    p1 = _p1;
    p2 = _p2;
    weight = _weight;
  }

  Edge(PVector _p1, PVector _p2) {
    p1 = _p1;
    p2 = _p2;
  }
  
  void show() {
    stroke(255, 0, 0, 100);
    strokeWeight(3);
    line(p1.x*tileSize*2, p1.y, p1.z*tileSize*2, p2.x*tileSize*2, p2.y, p2.z*tileSize*2);
  }
}
