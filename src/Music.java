import java.io.IOException;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Music {

	private static InputStream IN;
	private static AudioStream BGM;
	private static AudioPlayer MGP = AudioPlayer.player;
	
	public Music (String bgmBGM) {
		IN = Music.class.getResourceAsStream("/bgm/"+bgmBGM);
	}
	
	public static void Play() {
		try
		{
			BGM = new AudioStream(IN);
			MGP.start(BGM);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
