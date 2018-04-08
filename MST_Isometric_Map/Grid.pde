class Grid {

  Tile [][] tiles;
  PerlinMap perlinMask;

  Grid() {
    tiles = new Tile[gridSize][gridSize];
    perlinMask  = new PerlinMap(tileSize*heightModifier, 0.1);
  }

  void generateTerrain() {

    //Firstly, generate a 2D perlin noise map.
    perlinMask.generate();
    //Secondly, generate a grid of tiles.
    for (byte x = 0; x < gridSize; x++) {
      for (byte z = 0; z < gridSize; z++) {

        //If the elevation is zero or below zero, make it a water tile and don't add a height map.
        if ((perlinMask.points[x][z] + perlinMask.points[x+1][z] + perlinMask.points[x+1][z+1] + perlinMask.points[x][z+1])/4 <= 0) {
          tiles[x][z] = new Tile_Water(x, z);
        } else if ((perlinMask.points[x][z] + perlinMask.points[x+1][z] + perlinMask.points[x+1][z+1] + perlinMask.points[x][z+1])/4 > tileSize) {
          tiles[x][z] = new Tile_Dirt(x, z);
          tiles[x][z].getHeightMap(perlinMask.points[x][z], perlinMask.points[x+1][z], perlinMask.points[x+1][z+1], perlinMask.points[x][z+1]);
        } else if ((perlinMask.points[x][z] + perlinMask.points[x+1][z] + perlinMask.points[x+1][z+1] + perlinMask.points[x][z+1])/4 > 0 && (perlinMask.points[x][z] + perlinMask.points[x+1][z] + perlinMask.points[x+1][z+1] + perlinMask.points[x][z+1])/4 <= tileSize ) {
          tiles[x][z] = new Tile_Sand(x, z);
          //tiles[x][z].getHeightMap(perlinMask.points[x][z], perlinMask.points[x+1][z], perlinMask.points[x+1][z+1], perlinMask.points[x][z+1]);
        }
      }
    }
  }

  void show() {

    for (byte x = 0; x < gridSize; x++) {
      for (byte z = 0; z < gridSize; z++) {
        tiles[x][z].show();
      }
    }
  }
}
