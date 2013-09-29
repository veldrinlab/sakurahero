package pl.veldrinlab.sakurahero;

import pl.veldrinlab.sakuraEngine.core.Configuration;
import pl.veldrinlab.sakuraEngine.core.Renderer;
import pl.veldrinlab.sakuraEngine.core.SceneEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

//TODO tutaj dodaæ animacjê efektu spadania odpowiedni¹
public class SakuraTree {

	private SceneEntity tree;
	private SpriteBatch sakuraTreeBatch;
	private Stage sakuraTreeStage;

	//animacja dla kazdego
	private Array<SakuraLeaf> sakuraLeaves;
	private SakuraTreeDescriptor leavesDescriptors;
	
	private Array<Onigiri> onigiriArmy;
	
	private boolean treeIsDead;
	
	public SakuraTree(final Sprite treeSprite, final Array<Onigiri> onigiriArmy) {
		this.onigiriArmy = onigiriArmy;
		
		tree = new SceneEntity(treeSprite);
		tree.updateEntityState(0,-10.0f);

		sakuraTreeBatch = new SpriteBatch();
		sakuraTreeStage = new Stage(Configuration.getWidth(), Configuration.getHeight(),false,sakuraTreeBatch);

		sakuraLeaves = new Array<SakuraLeaf>();
		leavesDescriptors = new SakuraTreeDescriptor();
	}

	public void update(final float deltaTime) {
		for(SakuraLeaf s : sakuraLeaves) {
			s.update(deltaTime);
			s.collisionDetection(onigiriArmy);
		}
		
		for(int i = 0; i < sakuraLeaves.size; ++i)
			if(sakuraLeaves.get(i).isDead())
				sakuraLeaves.removeIndex(i);
		
		Gdx.app.log("size ", String.valueOf(sakuraLeaves.size));
		
		if(sakuraLeaves.size == 0)
			treeIsDead = true;
	}

	public void render() {
		//moze w przyszlosci optymalizacja renderingu tutaj - zobaczymy testy
		sakuraTreeStage.draw();
	
		for(SakuraLeaf s : sakuraLeaves)
			s.render();
	}

	public void addSakuraLeaf(final SceneEntity flower) {
		sakuraTreeStage.addActor(flower);
		leavesDescriptors.leaves.add(new SakuraLeafDescriptor(flower.position.x,flower.position.y, flower.rotation));
	}

	public void saveSakuraTree(final String filePath) {
		Json json = new Json();		
		FileHandle file = Gdx.files.local(filePath);		
		String jsonData = json.toJson(leavesDescriptors);
		file.writeString(jsonData, false);
	}

	public void loadSakuraTree(final String filePath) {
		Json json = new Json();		
		FileHandle file = Gdx.files.local("level.json");

		String jsonData = file.readString();

		try {
			leavesDescriptors = json.fromJson(SakuraTreeDescriptor.class, jsonData);			
		} catch(Exception e ) {

			Gdx.app.log("SakuraHero ","Level file loading exception");
			e.printStackTrace();
		}

		sakuraTreeStage.clear();
		sakuraTreeStage.addActor(tree);
		
		for(int i = 0; i < leavesDescriptors.leaves.size; ++i) {

			SakuraLeaf flower = new SakuraLeaf(Renderer.sceneAtlas.createSprite("sakuraFlower"),32,32);
			flower.rotation = leavesDescriptors.leaves.get(i).rotation;
			flower.updateEntityState(leavesDescriptors.leaves.get(i).x-flower.width*0.5f, leavesDescriptors.leaves.get(i).y-flower.height*0.5f);

			sakuraLeaves.add(flower);
			sakuraTreeStage.addActor(flower);
		}
	}
	
	public boolean isTreeDead() {
		return treeIsDead;
	}
	
	public Array<SakuraLeaf> getSakuraLeaves() {
		return sakuraLeaves;
	}
}
