package pl.veldrinlab.sakuraEngine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MultitouchGestureDetector extends InputAdapter {
	private static final int MAX_TOUCHES = 10;
	   
	   private final MultitouchGestureListener listener;
	   
	   private float tapSquareSize;
	   private long tapCountInterval;
	   private float longPressSeconds;
	   private long maxFlingDelay;
	   
	   private boolean[] inTapSquare = new boolean[MAX_TOUCHES];
	   private int[] tapCount = new int[MAX_TOUCHES];
	   private long[] lastTapTime = new long[MAX_TOUCHES];
	   private boolean[] longPressFired = new boolean[MAX_TOUCHES];
	//   private boolean[] pinching = new boolean[MAX_TOUCHES];
	   private boolean[] panning = new boolean[MAX_TOUCHES];
	   
	   private VelocityTracker[] tracker = new VelocityTracker[MAX_TOUCHES];
	   private float[] tapSquareCenterX = new float[MAX_TOUCHES];
	   private float[] tapSquareCenterY = new float[MAX_TOUCHES];
	   private long[] gestureStartTime = new long[MAX_TOUCHES];
	//   Vector2 pointer1 = new Vector2();
	//   private final Vector2 pointer2 = new Vector2();
	//   private final Vector2 initialPointer1 = new Vector2();
	//   private final Vector2 initialPointer2 = new Vector2();
	   
	   private LongPressTask[] longPressTask = new LongPressTask[MAX_TOUCHES];
	   
	//   private final Task longPressTask = new Task()
	//   {
//	      public void run()
//	      {
//	         if (listener.longPress(pointer1.x, pointer1.y))
//	            longPressFired = true;
//	      }
	//   };
	   
	   public MultitouchGestureDetector(MultitouchGestureListener listener)
	   {
	      this(20, 0.4f, 1.1f, 0.15f, listener);
	   }
	   
	   public MultitouchGestureDetector(float halfTapSquareSize, float tapCountInterval, float longPressDuration, float maxFlingDelay, MultitouchGestureListener listener)
	   {
	      this.tapSquareSize = halfTapSquareSize;
	      this.tapCountInterval = (long) (tapCountInterval * 1000000000l);
	      this.longPressSeconds = longPressDuration;
	      this.maxFlingDelay = (long) (maxFlingDelay * 1000000000l);
	      this.listener = listener;
	      
	      for (int i = 0; i < MAX_TOUCHES; i++)
	      {
	         tracker[i] = new VelocityTracker();
	         longPressTask[i] = new LongPressTask(i);
	      }
	   }
	   
	   @Override
	   public boolean touchDown(int x, int y, int pointer, int button)
	   {
	      if (pointer >= MAX_TOUCHES)
	         return false;
	      
	      gestureStartTime[pointer] = Gdx.input.getCurrentEventTime();
	      tracker[pointer].start(x, y, gestureStartTime[pointer]);
	      
	      inTapSquare[pointer] = true;
//	      pinching = false;
	      longPressFired[pointer] = false;
	      tapSquareCenterX[pointer] = x;
	      tapSquareCenterY[pointer] = y;
	      if (!longPressTask[pointer].isScheduled())
	         Timer.schedule(longPressTask[pointer], longPressSeconds);
	      
	      return listener.touchDown(x, y, pointer);
	   }
	   
	   @Override
	   public boolean touchDragged(int x, int y, int pointer)
	   {
	      if (pointer >= MAX_TOUCHES)
	         return false;
	      
	      if (longPressFired[pointer])
	         return false;
	      
	      // handle pinch zoom
//	      if (pinching)
//	      {
//	         if (pointer == 0)
//	            pointer1.set(x, y);
//	         else
//	            pointer2.set(x, y);
//	         if (listener != null)
//	         {
//	            boolean result = listener.pinch(initialPointer1, initialPointer2, pointer1, pointer2);
//	            return listener.zoom(initialPointer1.dst(initialPointer2), pointer1.dst(pointer2)) || result;
//	         }
//	         return false;
//	      }
	      
	      // update tracker
	      tracker[pointer].update(x, y, Gdx.input.getCurrentEventTime());
	      
	      // check if we are still tapping.
	      if (inTapSquare[pointer] && (Math.abs(x - tapSquareCenterX[pointer]) >= tapSquareSize || Math.abs(y - tapSquareCenterY[pointer]) >= tapSquareSize))
	      {
	         longPressTask[pointer].cancel();
	         inTapSquare[pointer] = false;
	      }
	      
	      if (!inTapSquare[pointer])
	      {
	         // handle pan
	         inTapSquare[pointer] = false;
	         panning[pointer] = true;
	         return listener.pan(x, y, tracker[pointer].deltaX, tracker[pointer].deltaY, pointer);
	      }
	      
	      return false;
	   }
	   
	   @Override
	   public boolean touchUp(int x, int y, int pointer, int button)
	   {
	      if (pointer >= MAX_TOUCHES)
	         return false;
	      
	      // check if we are still tapping.
	      if (inTapSquare[pointer] && (Math.abs(x - tapSquareCenterX[pointer]) >= tapSquareSize || Math.abs(y - tapSquareCenterY[pointer]) >= tapSquareSize))
	         inTapSquare[pointer] = false;
	      
	      longPressTask[pointer].cancel();
	      panning[pointer] = false;
	      if (longPressFired[pointer])
	         return false;
	      if (inTapSquare[pointer])
	      {
	         // handle taps
	         if (TimeUtils.nanoTime() - lastTapTime[pointer] > tapCountInterval)
	            tapCount[pointer] = 0;
	         tapCount[pointer]++;
	         lastTapTime[pointer] = TimeUtils.nanoTime();
	         gestureStartTime[pointer] = 0;
	         return listener.tap(x, y, tapCount[pointer], pointer);
	      }
//	      else if (pinching)
//	      {
//	         // handle pinch end
//	         pinching = false;
//	         panning = true;
//	         // we are in pan mode again, reset velocity tracker
//	         if (pointer == 0)
//	         {
//	            // first pointer has lifted off, set up panning to use the second pointer...
//	            tracker.start(pointer2.x, pointer2.y, Gdx.input.getCurrentEventTime());
//	         }
//	         else
//	         {
//	            // second pointer has lifted off, set up panning to use the first pointer...
//	            tracker.start(pointer1.x, pointer1.y, Gdx.input.getCurrentEventTime());
//	         }
//	      }
	      else
	      {
	         gestureStartTime[pointer] = 0;
	         // handle fling
	         long time = Gdx.input.getCurrentEventTime();
	         if (time - tracker[pointer].lastTime < maxFlingDelay)
	         {
	            tracker[pointer].update(x, y, time);
	            return listener.fling(tracker[pointer].startX, tracker[pointer].startY, tracker[pointer].lastX, tracker[pointer].lastY, tracker[pointer].getVelocityX(), tracker[pointer].getVelocityY(), pointer);
	         }
	      }
	      
	      return listener.touchUp(x, y, pointer);
	   }
	   
	   static class VelocityTracker
	   {
	      int sampleSize = 10;
	      float startX, startY;
	      float lastX, lastY;
	      float deltaX, deltaY;
	      long lastTime;
	      int numSamples;
	      float[] meanX = new float[sampleSize];
	      float[] meanY = new float[sampleSize];
	      long[] meanTime = new long[sampleSize];
	      
	      public void start(float x, float y, long timeStamp)
	      {
	         startX = x;
	         startY = y;
	         lastX = x;
	         lastY = y;
	         deltaX = 0;
	         deltaY = 0;
	         numSamples = 0;
	         for (int i = 0; i < sampleSize; i++)
	         {
	            meanX[i] = 0;
	            meanY[i] = 0;
	            meanTime[i] = 0;
	         }
	         lastTime = timeStamp;
	      }
	      
	      public void update(float x, float y, long timeStamp)
	      {
	         long currTime = timeStamp;
	         deltaX = (x - lastX);
	         deltaY = (y - lastY);
	         lastX = x;
	         lastY = y;
	         long deltaTime = currTime - lastTime;
	         lastTime = currTime;
	         int index = numSamples % sampleSize;
	         meanX[index] = deltaX;
	         meanY[index] = deltaY;
	         meanTime[index] = deltaTime;
	         numSamples++;
	      }
	      
	      public float getVelocityX()
	      {
	         float meanX = getAverage(this.meanX, numSamples);
	         float meanTime = getAverage(this.meanTime, numSamples) / 1000000000.0f;
	         if (meanTime == 0)
	            return 0;
	         return meanX / meanTime;
	      }
	      
	      public float getVelocityY()
	      {
	         float meanY = getAverage(this.meanY, numSamples);
	         float meanTime = getAverage(this.meanTime, numSamples) / 1000000000.0f;
	         if (meanTime == 0)
	            return 0;
	         return meanY / meanTime;
	      }
	      
	      private float getAverage(float[] values, int numSamples)
	      {
	         numSamples = Math.min(sampleSize, numSamples);
	         float sum = 0;
	         for (int i = 0; i < numSamples; i++)
	         {
	            sum += values[i];
	         }
	         return sum / numSamples;
	      }
	      
	      private long getAverage(long[] values, int numSamples)
	      {
	         numSamples = Math.min(sampleSize, numSamples);
	         long sum = 0;
	         for (int i = 0; i < numSamples; i++)
	         {
	            sum += values[i];
	         }
	         if (numSamples == 0)
	            return 0;
	         return sum / numSamples;
	      }
	      
//	      private float getSum(float[] values, int numSamples)
//	      {
//	         numSamples = Math.min(sampleSize, numSamples);
//	         float sum = 0;
//	         for (int i = 0; i < numSamples; i++)
//	         {
//	            sum += values[i];
//	         }
//	         if (numSamples == 0)
//	            return 0;
//	         return sum;
//	      }
	   }
	   
	   private class LongPressTask extends Task
	   {
	      private int pointer;
	      
	      public LongPressTask(int pointer)
	      {
	         this.pointer = pointer;
	      }
	      
	      @Override
	      public void run()
	      {
	         if (listener.longPress(tracker[pointer].lastX, tracker[pointer].lastY, pointer))
	            longPressFired[pointer] = true;
	      }
	   }
}
