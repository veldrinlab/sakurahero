package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

//TODO tutaj dodaæ animacjê efektu spadania odpowiedni¹
public class SakuraTree {

	
	public SakuraTreeDescriptor leaves;

	public SceneEntity tree;
	public SpriteBatch sakuraTreeBatch;
	public Stage sakuraTreeStage;
	
	public Sprite leafSprite;
	
	public SakuraTree(final Sprite treeSprite, final Sprite leafSprite) {
		this.leafSprite = leafSprite;
		
		tree = new SceneEntity(treeSprite);
		tree.updateEntityState(0,-10);

		sakuraTreeBatch = new SpriteBatch();
		sakuraTreeStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,sakuraTreeBatch);
		
		sakuraTreeStage.addActor(tree);
		
		leaves = new SakuraTreeDescriptor();
		
	}
	
	public void init() {
		for(int i = 0; i < leaves.leaves.size; ++i) {
			
			SceneEntity flower = new SceneEntity(Renderer.sceneAtlas.createSprite("sakuraFlower"),32,32);
			flower.rotation = leaves.leaves.get(i).rotation;
			flower.updateEntityState(leaves.leaves.get(i).x-flower.width*0.5f, leaves.leaves.get(i).y-flower.height*0.5f);
		
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
