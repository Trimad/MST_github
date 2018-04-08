class Tile {

  boolean visited = false;
  byte x;
  byte y;
  byte z;
  float v1;
  float v2;
  float v3;
  float v4;
  float surfaceHeight = 0;
  Tile(byte xC, byte yC, byte zC) {
    this.x = xC;
    this.y = yC;
    this.z = zC;
  }

  Tile(byte xC, byte zC) {
    this.x = xC;
    this.z = zC;
  }

  void show() {
  }
  
  public void getHeightMap(float v1In, float v2In, float v3In, float v4In) {
  }
}
