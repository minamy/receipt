import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;

//import ch.qos.logback.core.net.SyslogOutputStream;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.util.ImageIOHelper;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;


public class OCR{

	String text;
	float confidence;
	Rectangle boundingBox;
	PointerByReference pixa;
	PointerByReference blockids;
	
	
	public void process(BufferedImage image){
 		Tesseract instance = new Tesseract(); //JNI interface mapping
		TessAPI1.TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
		ByteBuffer buf = ImageIOHelper.convertImageData(image);
		int bpp = image.getColorModel().getPixelSize();
		int bytepp = bpp/8;
		int bytepl = (int) Math.ceil(image.getWidth()*bpp/8.0);
		//TessBaseAPI handle = new TessBaseAPI();
		//TessAPI1 tessAPI1 = new TessAPI1();
		Word word = new Word(text, confidence, boundingBox);
		
		try{
			
			
			TessAPI1.TessBaseAPIInit3(handle, "./tessdata", "eng");
			TessAPI1.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytepp, bytepl);
			
			instance.setHocr(true);
			String result = instance.doOCR(image);
			
			//IntByReference confResult = TessAPI1.TessBaseAPIAllWordConfidences(handle);
			//int valueOfConfResult = confResult.getValue();
			//System.out.println("value of confResult: "+ valueOfConfResult+"\n");
			
//			int[] confResults = confResult.getPointer().getIntArray(0, confResult.getPointer().SIZE);
//			for(int i=0; i<confResult.getPointer().SIZE; i++){
//				System.out.println(confResults[i]);
//			}
			
//			int[] confResults = confResult.getPointer().getIntArray(0, 50);
//			//System.out.println("array length: "+confResults.length);
//			for(int i=0; i<confResults.length; i++){
//				//System.out.println("---"+confResults[i]);
//			}
			
			Pointer utf8Text = TessAPI1.TessBaseAPIGetUTF8Text(handle);
			//System.out.println("utf8GetText: "+utf8Text.getString(0));
			//
			String[] words = utf8Text.getString(0).split(" ");
			//System.out.println("number of elements in utf8Text: "+words.length);
			for(int i=0; i<words.length; i++){
				//System.out.println("---"+words[i]);
			}
			//
			//System.out.println();
			//TessAPI1.TessBaseAPIGetComponentImages(handle, 1, 0, pixa, blockids);
			//System.out.println(TessBaseAPIAllWordConfidences(handle));
			//System.out.println(word.getConfidence());
			System.out.println(result);
			
			//
			String eachLineInResult[] = result.split("\n");
			//System.out.println("the number of lines in the result of doOCR: "+eachLineInResult.length);
//			ArrayList<String> wordList = new ArrayList<String>();
//			for(int i=0; i<eachLineInResult.length; i++){
//				System.out.println(eachLineInResult[i]);
//				System.out.println(i);
//				String eachWordInResult[] = eachLineInResult[i].split(" ");
//				for(int j=0; j<eachWordInResult.length; j++){
//					System.out.println("inside one loop: "+eachWordInResult[j]);
//				}
//			}
//			
//			for(int i=0; i<wordList.size(); i++){
//				System.out.println(wordList.get(i));
//			}
			
			//instance.setHocr(true);
			
			//
			TessAPI1.TessBaseAPIClearPersistentCache(handle);
			TessAPI1.TessBaseAPIClear(handle);
			TessAPI1.TessBaseAPIEnd(handle); //must be called to prevent memory leak
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
