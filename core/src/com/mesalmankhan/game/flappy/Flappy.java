package com.mesalmankhan.game.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Flappy extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;

	Texture[] birds;
	int flapstate = 0;
	float birdY = 0; // X remains same only Y vertical position changes
	Circle birdCircle;
	float velocity = 0;
	float gravity = 1.1f;
	int score = 0;
	int scoringTube=0;
	BitmapFont font;

	Texture topTube;
	Texture bottomTube;
	Texture gameOver;
	float gap = 400;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;


	int gamestate = 0;

	float maximumTubeOfset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float distanceBetweenTubes;
	float[] tubeOfset = new float[numberOfTubes];



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameOver = new Texture("gameover.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10 );


		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maximumTubeOfset = Gdx.graphics.getHeight() / 2 - gap/2 -100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		topTubeRectangle = new Rectangle[numberOfTubes];
		bottomTubeRectangle = new Rectangle[numberOfTubes];

		startGame();

	}

	public void startGame(){

		birdY = (Gdx.graphics.getHeight()/2)- (birds[0].getHeight());

		for(int i=0;i<numberOfTubes;i++){
			tubeOfset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = (Gdx.graphics.getWidth()/2) - (topTube.getWidth()/2) + Gdx.graphics.getWidth()+ i* distanceBetweenTubes;
			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();


		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gamestate == 1) {
			//GAME LOGIC


			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score++;
				Gdx.app.log("Score", String.valueOf(score) );
				if(scoringTube<numberOfTubes - 1){
					scoringTube++;
				}else {
					scoringTube = 0;
				}
			}

			if(Gdx.input.justTouched()) {
				velocity -= 35;

			}

			//TUBES
			for(int i=0;i<numberOfTubes;i++) {

				if(tubeX[i] < (-topTube.getWidth())){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOfset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else{
					tubeX[i] = tubeX[i] - tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], (Gdx.graphics.getHeight() / 2) + gap / 2 + tubeOfset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOfset[i]);

				topTubeRectangle[i]= new Rectangle((tubeX[i]), (Gdx.graphics.getHeight() / 2) + gap / 2 + tubeOfset[i],topTube.getWidth(),topTube.getHeight());
				bottomTubeRectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOfset[i],bottomTube.getWidth(),bottomTube.getHeight());
			}
			//END TUBES

			if(birdY > 0 ){
				velocity = velocity + gravity;
				birdY -= velocity;
			} else {
				gamestate = 2;

			}

		} else if(gamestate ==0){

			if(Gdx.input.justTouched()){
				gamestate = 1;
			}
		} else if(gamestate ==2){
			batch.draw(gameOver, Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			if(Gdx.input.justTouched()){
				gamestate = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;

			}
		}

		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}

		batch.draw(birds[flapstate], (Gdx.graphics.getWidth()/2) - (birds[flapstate].getWidth()), birdY );
		font.draw(batch,String.valueOf(score),100,200);


		batch.end();

		birdCircle.set((Gdx.graphics.getWidth()/2) - (birds[flapstate].getWidth()/2) , birdY + birds[flapstate].getHeight() / 2, birds[flapstate].getWidth() / 2 );

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x , birdCircle.y, birdCircle.radius);

		for(int i=0;i<numberOfTubes;i++) {
			//shapeRenderer.rect((tubeX[i]), (Gdx.graphics.getHeight() / 2) + gap / 2 + tubeOfset[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOfset[i],bottomTube.getWidth(),bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle,topTubeRectangle[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangle[i])){
				//Gdx.app.log("Collission", "Yes");
				gamestate = 2;
			}

		}

		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
