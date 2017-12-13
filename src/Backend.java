import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

public class Backend {

	
	
	public static void main(String[] args) {
		Backend main = new Backend();
		
		main.getImage();
		
	}
	
	public BufferedImage getImage(){
		BufferedImage readimage = null;
		try {
			readimage = ImageIO.read(new File("C:/Users/yukam/OneDrive/FYPGit/images/receipt4.JPG"));
			
			System.out.println("success");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readimage;
	}

}
