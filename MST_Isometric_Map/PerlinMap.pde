class PerlinMap {

  float [][] points;
  float flying = 0;
  int maxHeight;
  float intensity;

  PerlinMap(int maxHeightC, float intensityC) {
    this. points = new float[gridSize*4][gridSize*4];
    this.maxHeight = maxHeightC;
    this.intensity = intensityC;
  }
  void generate() {
    float yOffset = flying;
    for (int y = 0; y < gridSize*4; y++) {
      float xOffset = 0;
      for (int x = 0; x < gridSize*4; x++) {
        points[x][y] = map(noise(xOffset, yOffset), 0, 1, -maxHeight/2, maxHeight);
        xOffset+=intensity;
      }
      yOffset +=intensity;
    }
  }
}
