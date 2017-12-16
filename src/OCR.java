import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.*;

public class OCR {

	public void process(BufferedImage image){
		ITesseract instance = new Tesseract(); //JNI interface mapping
		try{
			String result = instance.doOCR(image);
			System.out.println(result);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
