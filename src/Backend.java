import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.*;


import javax.imageio.ImageIO;

public class Backend {
	
	private static int confidence[];
	private static ArrayList<String> result;
	private static ArrayList<String> nameAndPriceList;
	
	public Backend(){
		confidence = null;
		//result = new ArrayList<String>();
		nameAndPriceList = new ArrayList<String>();
	}
	
	public static void main(String[] args) {
		Backend main = new Backend();
		OCR ocr = new OCR(main.getImage());
		
		ocr.processOCR();
		
		nameAndPriceList = ocr.getNameAndPriceList();
		
		String[] nameList = main.convertToArray(ocr.getNameList());
		String[] priceList= main.convertToArray(ocr.getPriceList());
		
//		main.sendData(nameList, priceList);
		main.sendData(nameAndPriceList);
		
		confidence = ocr.getConfidence();
		
	}
	
	public String concat(ArrayList<String> nameAndPriceList){
		int size = nameAndPriceList.size();
		String data = "";
		String key = "";
		String value = "";
		for(int i=0; i<size; i++){
			key = "key"+ Integer.toString(i);
			value = nameAndPriceList.get(i);
			try {
				data += (URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8"));
				if(i<(size-1)){
					data += "&";
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}
	
	public void sendData(ArrayList<String> nameAndPriceList){
		
//			URL url = new URL("http://192.168.0.102/test.php");
		try {
			URL url = new URL("http://172.20.10.2/connectToDB.php");
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();  
			// Setting the DoOutput flag true for using output   
			con.setDoOutput(true);  
			con.setRequestMethod("POST");  
			// Encoding the POST data  
			//String data = URLEncoder.encode("key1", "UTF-8") + "=" + URLEncoder.encode("Hello", "UTF-8") + "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("World", "UTF-8");  
			String data = concat(nameAndPriceList);
			System.out.println("data"+data);
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());  
			// Sending parameter to site  
			osw.write(data);  
		   	osw.flush();  
		   	con.getInputStream();  
		   	//receive data from php
		   	System.out.println("RESPONSE CODE = " + con.getResponseCode());  
		   	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));  
		   	String line = null;  
		   	while ((line = in .readLine()) != null) {  
			   System.out.println(line);  
		   	}  
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public BufferedImage getImage(){
		BufferedImage readimage = null;
		try {
			readimage = ImageIO.read(new File("C:/Users/yukam/OneDrive/FYPGit/images/receipt4.JPG"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readimage;
	}
	
	public String[] convertToArray(ArrayList<String> arrayList){
		int length = arrayList.size();
		String[] temp = new String[length];
		for(int i=0; i<length; i++){
			temp[i] = arrayList.get(i);
		}
		return temp;
	}

}
