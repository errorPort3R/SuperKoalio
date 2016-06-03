package com.theironyard.javawithclojure.porter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	TextureRegion stand;
	TextureRegion jump;
	float x, y, xv, yv;
	boolean canJump;
	boolean faceRight = true;
	Animation walk;
	float time;

	static final float MAX_VELOCITY = 300f;
	static final float MAX_JUMP_VELOCITY = 2000f;
	static final float DECLERATION_RATE = 0.95f;
	static final int WIDTH = 18;
	static final int HEIGHT = 26;
	static final int GRAVITY = -50;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture sheet = new Texture("koalio.png");
		TextureRegion[] [] tiles = TextureRegion.split(sheet, WIDTH, HEIGHT);
		stand = tiles[0][0];
		jump = tiles[0][1];
		walk = new Animation(0.15f, tiles[0][2], tiles[0][3], tiles[0][4]);
	}

	@Override
	public void render () {
		move();

		time += Gdx.graphics.getDeltaTime();

		TextureRegion img;
		if (y>0)
		{
			img = jump;
		}
		else if (xv!= 0)
		{
			img = walk.getKeyFrame(time, true);
		}
		else

		{
			img = stand;
		}


		Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if (faceRight)
		{
			batch.draw(img, x, y, WIDTH * 3, HEIGHT * 3);
		}
		else
		{
			batch.draw(img, x + WIDTH*3, y, WIDTH* -3, HEIGHT* 3 );
		}

		batch.end();
	}

	public void move()
	{
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && canJump)
		{
			yv = MAX_JUMP_VELOCITY;
			canJump = false;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
		{
			yv = -MAX_VELOCITY;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			xv = MAX_VELOCITY;
			faceRight = true;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			xv = -MAX_VELOCITY;
			faceRight = false;
		}

		yv += GRAVITY;

		float delta = Gdx.graphics.getDeltaTime();
		y+= yv * delta;
		x+= xv * delta;
		yv = decelerate(yv);
		xv = decelerate(xv);

		if (y<0)
		{
			y = 0;
			canJump =true;
		}

		if (x<0)
		{
			x = 0;
		}
		if (x>(800-(WIDTH*3)))
		{
			x = (800-(WIDTH*3));
		}


	}

	public float decelerate(float velocity)
	{
		velocity *= DECLERATION_RATE;
		if (Math.abs(velocity) < 10)
		{
			velocity = 0;
		}
		return velocity;
	}
}


