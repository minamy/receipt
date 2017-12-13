import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Backend {

	public static void main(String[] args) {
		Backend main = new Backend();
		main.getImage();
		
	}
	
	public void getImage(){
		try{
			BufferedImage image = ImageIO.read(new File("/Users/al/some-picture.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
