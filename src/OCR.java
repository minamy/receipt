import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import ch.qos.logback.core.net.SyslogOutputStream;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.util.ImageIOHelper;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;


public class OCR{

	ExtractHocr exHocr;
	Tesseract instance;
	TessAPI1.TessBaseAPI handle;
	
	float confidence;
	Rectangle boundingBox;
	PointerByReference pixa;
	PointerByReference blockids;
	BufferedImage image;
	ByteBuffer buf;
	int bpp;
	int bytepp;
	int bytepl;
	
	ArrayList<String> nameAndPriceList = new ArrayList<String>();
	
	public OCR(BufferedImage image){
		exHocr = new ExtractHocr();
		instance = new Tesseract(); //JNI interface mapping
//		instance.setTessVariable("load_system_dawg", "F");
//		instance.setTessVariable("load_freq_dawg", "F");
//		instance.setTessVariable("user_words_suffix", "user-words");
//		instance.setTessVariable("user_patterns_suffix", "user-patterns");
//		List<String> configs = Arrays.asList("bazaar"); //could cause an error
//		instance.setConfigs(configs); //could cause an error
		handle = TessAPI1.TessBaseAPICreate();
		
		this.image = image;
		buf = ImageIOHelper.convertImageData(image);
		bpp = image.getColorModel().getPixelSize();
		bytepp = bpp/8;
		bytepl = (int) Math.ceil(image.getWidth()*bpp/8.0);
		
	}
	
	public void processOCR(){
		String result = "";
		try{
			instance.setTessVariable("tessedit_char_blacklist", "€");
			TessAPI1.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO);
			TessAPI1.TessBaseAPIInit3(handle, "./tessdata", "eng");
//			instance.setTessVariable("load_system_dawg", "F");//could cause an error
//            instance.setTessVariable("load_freq_dawg", "F");//could cause an error
//            instance.setTessVariable("user_words_suffix", "user-words");//could cause an error
//            instance.setTessVariable("user_patterns_suffix", "user-patterns");//could cause an error
//			List<String> configs = Arrays.asList("bazaar");//could cause an error
//			instance.setConfigs(configs);//could cause an error
//			PointerByReference configP = (PointerByReference) configs;//could cause an error
//			TessAPI1.TessBaseAPIInit1(handle, "./tessdata", "eng", 0, configP, configs.size());//could cause an error
			TessAPI1.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytepp, bytepl);
			
			instance.setHocr(true);
			result = instance.doOCR(image);
			
			nameAndPriceList = exHocr.extract(result);
			
			TessAPI1.TessBaseAPIClearPersistentCache(handle);
			TessAPI1.TessBaseAPIClear(handle);
			TessAPI1.TessBaseAPIEnd(handle); //must be called to prevent memory leak
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<String> getNameAndPriceList(){
		return nameAndPriceList;
	}
	
	public ArrayList<String> getNameList(){
		return exHocr.getNameList();
	}
	
	public ArrayList<String> getPriceList(){
		return exHocr.getPriceList();
	}
	
	
	public int[] getConfidence(){
		TessAPI1.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO);
		TessAPI1.TessBaseAPIInit3(handle, "./tessdata", "eng");
		TessAPI1.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytepp, bytepl);
		
		Pointer utf8Text = TessAPI1.TessBaseAPIGetUTF8Text(handle);
        String utf8Result = utf8Text.getString(0);
		String eachLine[] = utf8Result.split("\n");
		ArrayList<String> wordList = new ArrayList<String>();
		for(int i=0; i<eachLine.length; i++){
			String eachWord[] = eachLine[i].split(" ");
			for(int j=0; j<eachWord.length; j++){
				wordList.add(eachWord[j]);
			}
		}
		
		IntByReference confResult = TessAPI1.TessBaseAPIAllWordConfidences(handle);
		
		int[] confResults = confResult.getPointer().getIntArray(0, wordList.size());
		for(int i=0; i<wordList.size(); i++){
			//System.out.println(wordList.get(i)+" :"+confResults[i]);
		}
		
		TessAPI1.TessBaseAPIClearPersistentCache(handle);
		TessAPI1.TessBaseAPIClear(handle);
		TessAPI1.TessBaseAPIEnd(handle); //must be called to prevent memory leak
		
		return confResults;
	}
}
