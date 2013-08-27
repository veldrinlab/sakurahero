package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakuraEngine.core.SpriteActor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

//TODO tutaj dodaæ animacjê efektu spadania odpowiedni¹
public class SakuraTree {

	
	public SakuraTreeDescriptor leaves;

	public SpriteActor tree;
	public SpriteBatch sakuraTreeBatch;
	public Stage sakuraTreeStage;
	
	public Texture leafTexture;
	
	public SakuraTree(final Texture treeTexture, final Texture leafTexture) {
		this.leafTexture = leafTexture;
		
		tree = new SpriteActor(treeTexture);
		tree.getSprite().setY(-10.0f);

		sakuraTreeBatch = new SpriteBatch();
		sakuraTreeStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,sakuraTreeBatch);
		
		sakuraTreeStage.addActor(tree);
		
		leaves = new SakuraTreeDescriptor();
		

	}
	
	//TODO maybe some init method
	
	// moze path to pliku?
	public void init() {
		for(int i = 0; i < leaves.leaves.size; ++i) {
			
			// to nie tutaj jebnac
			SpriteActor flower = new SpriteActor(leafTexture);
			flower.getSprite().setPosition(leaves.leaves.get(i).x-flower.getSprite().getWidth()*0.5f, leaves.leaves.get(i).y-flower.getSprite().getHeight()*0.5f);
			flower.getSprite().setRotation(leaves.leaves.get(i).rotation);

			sakuraTreeStage.addActor(flower);
		}
	}
	
	public void update(final float deltaTime) {
		//TODO logika drzewa, gry, trybu oraz animacja spadania/umierania liœci itp.
	}
	
	public void render() {
		sakuraTreeStage.draw();
	}
}
