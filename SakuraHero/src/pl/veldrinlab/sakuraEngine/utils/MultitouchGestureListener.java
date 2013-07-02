package pl.veldrinlab.sakuraEngine.utils;

public interface MultitouchGestureListener
{
   public boolean touchDown(float x, float y, int pointer);
   public boolean touchUp(float x, float y, int pointer);
   public boolean tap(float x, float y, int count, int pointer);
   public boolean longPress(float x, float y, int pointer);
   public boolean fling(float startX, float startY, float endX, float endY, float velocityX, float velocityY, int pointer);
   public boolean pan(float x, float y, float deltaX, float deltaY, int pointer);
   //public boolean zoom(float initialDistance, float distance);
   //public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2);
}