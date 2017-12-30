import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Backend {
	
	private static int confidence[];
	private static ArrayList<String> result;
	
	public Backend () {
		confidence = null;
		result = new ArrayList<String>();
	}
	
	public static void main(String[] args) {
		Backend main = new Backend();
		OCR ocr = new OCR(main.getImage());
		
		result = ocr.processOCR();
		
		for(int i=0; i<result.size(); i++){
			System.out.println(result.get(i));
		}
		confidence = ocr.getConfidence();
	}
	
	public BufferedImage getImage(){
		BufferedImage readimage = null;
		try {
			readimage = ImageIO.read(new File("C:/Users/yukam/OneDrive/FYPGit/images/receipt4.JPG"));
			
			//System.out.println("success");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readimage;
	}

}
