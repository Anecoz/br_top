package graphics.animation;

import graphics.lowlevel.Texture;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// Holds information about frames used for animation.
// Holds a time so that it knows which frame is current
// Minimum framerate is 1 Hz, or 1 frame/second (maybe change?)
public class Animation {
    private int frameRate;
    private int numFrames;
    private List<Texture> frameList;
    private int currFrameIndex;
    private Timer timer;

    public Animation(String spriteFilePath, int spriteWidth, int spriteHeight, int frameRate) {
        this.frameRate = frameRate;
        try {
            this.frameList = AnimationLoader.loadTextures(spriteFilePath, spriteWidth, spriteHeight, 0, 0);
            this.numFrames = this.frameList.size();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.currFrameIndex = 0;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currFrameIndex == (numFrames - 1))
                    currFrameIndex = 0;
                else
                    currFrameIndex++;
            }
        }, 1000/(frameRate), 1000/(frameRate));
    }

    public void pause() {
        timer.cancel();
    }

    public void stop() {
        timer.cancel();
        currFrameIndex = 0;
    }

    public Texture getFrame() {
        return frameList.get(currFrameIndex);
    }

    public void cleanUp() {
        // Clean up all the textures
        for (Texture tex: frameList) {
            tex.cleanUp();
        }
        // This'll just go on otherwise
        timer.cancel();
    }
}
