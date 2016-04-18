package audio;

import org.joml.Vector2f;
import org.lwjgl.openal.*;
import utils.WaveData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;

public class AudioMaster {

    private static List<Integer> buffers = new ArrayList<Integer>();

    private static ALDevice device;
    private static ALContext context;

    public static void init(){
        try {
            ByteBuffer buffer = null;
            context = ALContext.create();
            device = context.getDevice();
            ALCCapabilities capabilities = device.getCapabilities();
            if (!capabilities.OpenALC10)
                throw new RuntimeException("OpenAL Context Creation failed");

        } catch (OpenALException e){
            e.printStackTrace();
        }
    }

    public static void setListenerData(Vector2f position, Vector2f velocity){
        AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, velocity.x, velocity.y, 0);
    }

    public static int loadSound(String file){
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        WaveData waveFile = WaveData.create(file);
        AL10.alBufferData(buffer, AL10.AL_FORMAT_MONO16, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }

    public static void cleanUp(){
        for(int buffer : buffers){
            AL10.alDeleteBuffers(buffer);
        }
        device.destroy();
        context.destroy();
    }
}
