import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Path_Finding extends PApplet {

PImage dirt;
PImage grass;
PImage lava;
PImage rock;
PImage sand;
PImage snow;
PImage water;

//Custom camera controls
int upDown;
int leftRight;
boolean [] keys;

//T H E G R I D
int gridSize = 32;
int tileSize = 32;
Grid grid;

ArrayList<Character> NPC = new ArrayList<Character>();
Character player = new Character(floor(gridSize/2), 0, floor(gridSize/2));
public void setup() {
  //fullScreen(P3D);
  
  frameRate(10);
  textureMode(NORMAL);
  textureWrap(REPEAT);
  keys = new boolean[4];
  dirt =  loadImage("assets/dirt.jpg");
  grass = loadImage("assets/grass.jpg");
  lava =  loadImage("assets/lava.jpg");
  rock =  loadImage("assets/rock.jpg");
  sand =  loadImage("assets/sand.jpg");
  snow =  loadImage("assets/snow.jpg");
  water =  loadImage("assets/water.jpg");

  grid = new Grid();
  grid.generateTerrain();
  spawnRandomNPC(30);
}


public void draw() {
  background(51);
  noStroke();
  translate(width/2-(tileSize*2*player.v.x), height/2-(tileSize*2*player.v.z), -512-(player.v.y*2));
  rotateX(radians(90));
  rotateZ(radians(180));
  rotateY(radians(-180));
      println(player.v.x+", "+player.v.y+", "+player.v.z);
  //ortho();

  //makeIsometric();

  //rotateX(-atan(1 / sqrt(2)));
  //rotateY(QUARTER_PI);



  lights();

  toggleGuideLines(keyPressed);

  //Show the grid
  grid.show();

  //Show the player
  player.show(tileSize);
  player.getElevation(grid.tiles);

  //Shwo the NPC's
  if (NPC.size() > 1) {
    for (int i = 0; i < NPC.size(); i++) {
      NPC.get(i).show(tileSize);
      NPC.get(i).getElevation(grid.tiles);
    }
    MST(NPC);
  }


  //Custom camera controls
  if (keys[0]) {//w
    if (player.v.z > 0){
      //if (grid.tiles[(int)player.v.x][(int)player.v.x+1] instanceof Tile_Dirt)
      player.v.z--;
    }
  }
  if (keys[1]) { //d
    if (player.v.x < gridSize-1){
      //if (grid.tiles[(int)player.v.z][(int)player.v.z] instanceof Tile_Dirt)
      player.v.x++;
    }
  }
  if (keys[2]) { //s
    if (player.v.z < gridSize-1) {
      //if(grid.tiles[(int)player.v.x][(int)player.v.x-1] instanceof Tile_Dirt)
      player.v.z++;
    }
  }
  if (keys[3]) { //a
    if (player.v.x > 0) {
      //if(grid.tiles[(int)player.v.z+1][(int)player.v.z] instanceof Tile_Dirt)
      player.v.x--;
    }
  }
}

public void keyPressed()
{
  if (key=='w' || keyCode == UP)
  keys[0]=true;
  if (key=='d' || keyCode == RIGHT)
  keys[1]=true;
  if (key=='s' || keyCode == DOWN)
  keys[2]=true;
  if (key=='a' || keyCode == LEFT)
  keys[3]=true;
}

public void keyReleased()
{
  if (key=='w' || keyCode == UP)
  keys[0]=false;
  if (key=='d' || keyCode == RIGHT)
  keys[1]=false;
  if (key=='s' || keyCode == DOWN)
  keys[2]=false;
  if (key=='a' || keyCode == LEFT)
  keys[3]=false;
}

public void spawnRandomNPC(int oneIn) {

  for (int x = 0; x < gridSize; x++) {
    for (int z = 0; z < gridSize; z++) {
      int chance = floor(random(1, oneIn));
      if (chance == 1) {

        if (grid.tiles[x][z] instanceof Tile_Dirt) {
          NPC.add(new Character(x, 0, z));
        }
      }
    }
  }
}

public void makeIsometric() {
  rotateX(-atan(1 / sqrt(2)));
  rotateY(QUARTER_PI);
}

public void toggleGuideLines(boolean state) {

  if (key == ' ' && state) {
    strokeWeight(3);
    //x-axis, green positive
    stroke(100, 255, 100);
    line(0, 0, 0, 4096, 0, 0);
    //x-axis, green negative
    stroke(0, 100, 0);
    line(0, 0, 0, -4096, 0, 0);
    //z-axis, red positive
    stroke(255, 100, 100);
    line(0, 0, 0, 0, 0, 4096);
    //z-axis, red negative
    stroke(100, 0, 0);
    line(0, 0, 0, 0, 0, -4096);
    //y-axis, blue positive
    stroke(100, 100, 255);
    line(0, 0, 0, 0, 4096, 0);
    //y-axis, blue negative
    stroke(0, 0, 100);
    line(0, 0, 0, 0, -4096, 0);
    strokeWeight(1);
    stroke(0);
  }
}


public void MST(ArrayList<Character> list) {

  ArrayList<PVector> reached = new ArrayList<PVector>();
  ArrayList<PVector> unreached = new ArrayList<PVector>();

  for (int i = 0; i < list.size(); i++) {
    unreached.add(list.get(i).v);
  }

  reached.add(unreached.get(0));
  unreached.remove(0);

  while (unreached.size() > 0) {
    float record = 9999; //infinity
    int rIndex = 0;
    int uIndex = 0;
    for (int i = 0; i < reached.size(); i++) {
      for (int j = 0; j < unreached.size(); j++) {
        PVector v1 = reached.get(i);
        PVector v2 = unreached.get(j);
        float d = dist(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
        if (d < record) {
          record = d;
          rIndex = i;
          uIndex = j;
        }
      }
    }
    stroke(255);
    strokeWeight(2);
    PVector p1 = reached.get(rIndex);
    PVector p2 = unreached.get(uIndex);
    line(p1.x*tileSize*2, p1.y, p1.z*tileSize*2, p2.x*tileSize*2, p2.y, p2.z*tileSize*2);
    reached.add(p2);
    unreached.remove(uIndex);
  }
  noStroke();
}

public void randomWalk(){
  if(frameCount%2==0){
    int xr = round(random(-1,1));
    int zr = round(random(-1,1));

    switch(xr){
      case -1:
      if(player.v.x > 2){
        player.v.x += xr;
      }
      break;
      case 1:
      if(player.v.x <gridSize-2){
        player.v.x += xr;
      }
      break;
    }

    switch(zr){
      case -1:
      if(player.v.z > 2){
        player.v.z += zr;
      }
      break;
      case 1:
      if(player.v.z <gridSize-2){
        player.v.z += zr;
      }
      break;
    }

  }
}
/*
y is a function of x and z.
 x and z are integers the represent the position in the grid.
 y is a negative value and absolute position of the Character.
 */


class Character {

  PVector v;
  float yModifier;

  Character(int xC, float yC, int zC) {
    this.v = new PVector(xC, yC, zC);
  }

  public void show(int tileSize) {
    translate(v.x*tileSize*2, v.y, v.z*tileSize*2);
    sphereDetail(16);
    sphere(tileSize);
    translate(-v.x*tileSize*2, -v.y, -v.z*tileSize*2);
  }

  public void getElevation(Tile[][] tiles) {
    this.v.y = -(gridSize*2+((tiles[(int)v.x][(int)v.z].v1 + 
      tiles[(int)v.x][(int)v.z].v2 + 
      tiles[(int)v.x][(int)v.z].v3 + 
      tiles[(int)v.x][(int)v.z].v4)/4));
  }
}
class Grid {

  Tile [][] tiles;
  PerlinMap perlinMask;

  Grid() {
    tiles = new Tile[gridSize][gridSize];
    perlinMask  = new PerlinMap(tileSize*10, 0.1f);
  }

  public void generateTerrain() {

    //Firstly, generate a 2D perlin noise map.
    perlinMask.generate();
    //Secondly, generate a grid of tiles.
    for (int x = 0; x < gridSize; x++) {
      for (int z = 0; z < gridSize; z++) {
        tiles[x][z] = new Tile_Water(x, 0, z);
        //If the elevation is zero or below zero, make it a water tile and don't add a height map.
        if ((perlinMask.points[x][z] + perlinMask.points[x+1][z] + perlinMask.points[x+1][z+1] + perlinMask.points[x][z+1])/4 <= 0) {
          tiles[x][z] = new Tile_Water(x, 0, z);
        } else {
          tiles[x][z] = new Tile_Dirt(x, 0, z);
          tiles[x][z].getHeightMap(perlinMask.points[x][z], perlinMask.points[x+1][z], perlinMask.points[x+1][z+1], perlinMask.points[x][z+1]);
        }
      }
    }
  }

  public void show() {

    for (int x = 0; x < gridSize; x++) {
      for (int z = 0; z < gridSize; z++) {
        tiles[x][z].show();
      }
    }
  }
}
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
  public void generate() {
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
class Tile {

  int x;
  int y;
  int z;
  float v1;
  float v2;
  float v3;
  float v4;

  Tile(int xC, int yC, int zC) {

    this.x = xC;
    this.y = yC;
    this.z = zC;
  }

  public void show(){}


  public void getHeightMap(float v1In, float v2In, float v3In, float v4In) {
    this.v1 = v1In;
    this.v2 = v2In;
    this.v3 = v3In;
    this.v4 = v4In;
  }
}
public class Tile_Dirt extends Tile {

  Tile_Dirt(int xC, int yC, int zC) {
    super(xC, yC, zC);
  }

  public void show() {

    translate(this.x*tileSize*2, -this.y, this.z*tileSize*2);

    beginShape(QUADS);
    texture(grass);
    // -Y "top" face
    vertex(-tileSize, -tileSize-v1, -tileSize, 0, 0);
    vertex( tileSize, -tileSize-v2, -tileSize, 1, 0);
    vertex( tileSize, -tileSize-v3, tileSize, 1, 1);
    vertex(-tileSize, -tileSize-v4, tileSize, 0, 1);
    // +Y "bottom"
    vertex(-tileSize, tileSize, tileSize, 0, 0);
    vertex( tileSize, tileSize, tileSize, 1, 0);
    vertex( tileSize, tileSize, -tileSize, 1, 1);
    vertex(-tileSize, tileSize, -tileSize, 0, 1);
    endShape();

    beginShape(QUADS);
    texture(dirt);
    // +Z "front" face
    vertex(-tileSize, -tileSize-v4, tileSize, 0, 0);
    vertex( tileSize, -tileSize-v3, tileSize, 1, 0);
    vertex( tileSize, tileSize, tileSize, 1, 2);
    vertex(-tileSize, tileSize, tileSize, 0, 2);
    // -Z "back"
    vertex( tileSize, -tileSize-v2, -tileSize, 0, 0);
    vertex(-tileSize, -tileSize-v1, -tileSize, 1, 0);
    vertex(-tileSize, tileSize, -tileSize, 1, 2);
    vertex( tileSize, tileSize, -tileSize, 0, 2);
    // +X "right"
    vertex( tileSize, -tileSize-v3, tileSize, 0, 0);
    vertex( tileSize, -tileSize-v2, -tileSize, 1, 0);
    vertex( tileSize, tileSize, -tileSize, 1, 2);
    vertex( tileSize, tileSize, tileSize, 0, 2);
    // -X "left"
    vertex(-tileSize, -tileSize-v1, -tileSize, 0, 0);
    vertex(-tileSize, -tileSize-v4, tileSize, 1, 0);
    vertex(-tileSize, tileSize, tileSize, 1, 2);
    vertex(-tileSize, tileSize, -tileSize, 0, 2);
    endShape();
    translate(-this.x*tileSize*2, this.y, -this.z*tileSize*2);
  }

  public void getHeightMap(float v1In, float v2In, float v3In, float v4In) {
    this.v1 = v1In;
    this.v2 = v2In;
    this.v3 = v3In;
    this.v4 = v4In;
  }

}
public class Tile_Water extends Tile {

  float v1;
  float v2;
  float v3;
  float v4;

  Tile_Water(int xC, int yC, int zC) {
    super(xC, yC, zC);
  }

  public void show() {

    translate(this.x*tileSize*2, -this.y, this.z*tileSize*2);
    beginShape(QUADS);
    texture(water);

    // -Y "top" face
    vertex(-tileSize, -tileSize-v1, -tileSize, 0, 0);
    vertex( tileSize, -tileSize-v2, -tileSize, 1, 0);
    vertex( tileSize, -tileSize-v3, tileSize, 1, 1);
    vertex(-tileSize, -tileSize-v4, tileSize, 0, 1);
    // +Y "bottom" face
    vertex(-tileSize, tileSize, tileSize, 0, 0);
    vertex( tileSize, tileSize, tileSize, 1, 0);
    vertex( tileSize, tileSize, -tileSize, 1, 1);
    vertex(-tileSize, tileSize, -tileSize, 0, 1);
    // +Z "front" face
    vertex(-tileSize, -tileSize-v4, tileSize, 0, 0);
    vertex( tileSize, -tileSize-v3, tileSize, 1, 0);
    vertex( tileSize, tileSize, tileSize, 1, 2);
    vertex(-tileSize, tileSize, tileSize, 0, 2);
    // -Z "back"
    vertex( tileSize, -tileSize-v2, -tileSize, 0, 0);
    vertex(-tileSize, -tileSize-v1, -tileSize, 1, 0);
    vertex(-tileSize, tileSize, -tileSize, 1, 2);
    vertex( tileSize, tileSize, -tileSize, 0, 2);
    // +X "right"
    vertex( tileSize, -tileSize-v3, tileSize, 0, 0);
    vertex( tileSize, -tileSize-v2, -tileSize, 1, 0);
    vertex( tileSize, tileSize, -tileSize, 1, 2);
    vertex( tileSize, tileSize, tileSize, 0, 2);
    // -X "left"
    vertex(-tileSize, -tileSize-v1, -tileSize, 0, 0);
    vertex(-tileSize, -tileSize-v4, tileSize, 1, 0);
    vertex(-tileSize, tileSize, tileSize, 1, 2);
    vertex(-tileSize, tileSize, -tileSize, 0, 2);
    endShape();
    translate(-this.x*tileSize*2, this.y, -this.z*tileSize*2);
  }
}
  public void settings() {  size(900, 900, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Path_Finding" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
