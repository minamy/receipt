import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class ExtractHocr {

	private ArrayList<String> namePriceList;
	private ArrayList<String> nameList;
	private ArrayList<String> priceList;
	private ArrayList<String> shopList;
	private boolean shopFound;
	
	public ExtractHocr () {
		namePriceList = new ArrayList<String>();
		nameList = new ArrayList<String>();
		priceList = new ArrayList<String>();
		shopList = new ArrayList<String>();
		shopList.add("Sainsbury's");
		shopList.add("sainsbury's");
		shopList.add("Sainsburys");
		shopList.add("Tesco");
		shopFound = false;
	}
	
	//--------------------------I should be able to do something about the shop lists. 
	//For instance if the result contains a similar shop name which differs by 1 character,
	//It should still recognise that as a shop name
	//Or it could double check with the user and add the word to the list
	public ArrayList<String> extract(String result){
		
		//System.out.println(result);
		
		Document doc = Jsoup.parse(result);
		Elements spans = doc.select("span");
		
		ArrayList<String> nameAndPriceList = new ArrayList<String>();
		
		for(int i=0; i<spans.size(); i++){
			Element oneSpan = spans.get(i);
//			System.out.println("oneSpan: "+spans.get(i));
			String oneSpanClass = oneSpan.attr("class");
			
			//identify the shop names
			if(!shopFound){
				for(int j=0; j<shopList.size(); j++){
					if(oneSpan.text().contains(shopList.get(j))){
						nameAndPriceList.add(0, shopList.get(j));
						shopFound = true;
					}
						
				}
			}
			
			if(oneSpanClass.equals("ocr_line")){
				Elements insideOneSpans = oneSpan.children();
				int numberOfWords = insideOneSpans.size();
				//System.out.println("should be the words inside the line class: "+insideOneSpans);
//				System.out.println("the number of words: "+numberOfWords);
				
				if(numberOfWords>1){
					//take x1 from last word
					Element lastWord = insideOneSpans.get(numberOfWords-1);
					String lTitle = lastWord.attr("title");
					String lTitleElements[] = lTitle.split("; ");
					String lbboxElements[] = lTitleElements[0].split(" ");
					String bboxX1String = lbboxElements[1];
					int X1 = Integer.parseInt(bboxX1String);
					//take x2 from second last word
					Element slastWord = insideOneSpans.get(numberOfWords-2);
					String slTitle = slastWord.attr("title");
					String slTitleElements[] = slTitle.split("; ");
					String slbboxElements[] = slTitleElements[0].split(" ");
					String bboxX2String = slbboxElements[3];
					int X2 = Integer.parseInt(bboxX2String);
					
					if(X1-X2>400){
						if(oneSpan.text().contains("£")){
							
							if(!oneSpan.text().contains("BALANCE")){
								if(!oneSpan.text().contains("VISACREDIT")){
									//System.out.println("inside "+oneSpan.text());
									namePriceList.add(oneSpan.text());
									String[] temp = oneSpan.text().split(" £");
									nameAndPriceList.add(temp[0]);
									nameAndPriceList.add(temp[1]);
									nameList.add(temp[0]);
									priceList.add(temp[1]);
								}
							}
							
						}else{ //if oneSpan.text() contains number
							
						}
					}
					
				}
				//change this check to something like "get lines which contain £ sign"
			}
			
		}
		
		return nameAndPriceList;
		
	}
	
	public ArrayList<String> shopName(ArrayList<String> nameAndPriceList){
		
		return nameAndPriceList;
	}
	
	
	
	public ArrayList<String> getNameList(){
		return nameList;
	}
	
	public ArrayList<String> getPriceList(){
		return priceList;
	}
	
}
