package pl.veldrinlab.sakuraEngine.core;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Class represents abstarct SceneEntity of 3D world. Store geoemtry, material and all default data like position, rotation and visibility.
 * All entities in game must extends this class. SceneEntities can be organized in hierarchy tree - binary tree.
 * @author Szymon Jab³oñski
 *
 */
public abstract class SceneEntity {

	public Mesh mesh;
	public Material material;
	public Texture texture;
	public ShaderProgram shader;
	public BoundingBox bounds;
	public Vector3 boundsMin;
	public Vector3 boundsMax;

	public Vector3 position;
	public Vector3 velocity;
	public Vector3 acceleration;
	public Vector3 scale;
	public Vector3 rotation;
	public Vector3 rotationVelocity;
	public Vector3 moveDirection;
	public Matrix4 modelMatrix;
	public boolean isVisible;
	
	public SceneEntity parent;
	public SceneEntity leftBrother;
	public SceneEntity rightBrother;
	
	/**
	 * Class constructor. Initialize entity, create default matrices and calculate bounding volume for collision detection.
	 * @param mesh
	 * @param texture
	 * @param shader
	 */
	public SceneEntity(final Mesh mesh, final Texture texture, final ShaderProgram shader) {
		this.mesh = mesh;
		this.texture = texture;
		this.shader = shader;
		
		material = new Material();
		bounds = new BoundingBox();
		bounds = mesh.calculateBoundingBox();
		boundsMin = new Vector3(bounds.min);
		boundsMax = new Vector3(bounds.max);
		
		position = new Vector3();
		velocity = new Vector3();
		acceleration = new Vector3();
		scale = new Vector3();
		rotation = new Vector3();
		rotationVelocity = new Vector3();
		modelMatrix = new Matrix4();
		modelMatrix.idt();
		material.alpha = 1.0f;
	}
	
	/**
	 * Method is used to update bounding volume in world space.
	 */
	protected void updateBounds() {
		bounds.min.set(position);
		bounds.min.add(boundsMin);
		bounds.max.set(position);
		bounds.max.add(boundsMax);	
	}
	
	public abstract void updateEntity(final float deltaTime);
}
