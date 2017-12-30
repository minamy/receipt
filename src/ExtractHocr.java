import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ExtractHocr {

	private ArrayList<String> namePriceList;
	
	public ExtractHocr () {
		namePriceList = new ArrayList<String>();
	}
	
	public ArrayList<String> extract(String result){
		Document doc = Jsoup.parse(result);
		Elements spans = doc.select("span");
		
		for(int i=0; i<spans.size(); i++){
			Element oneSpan = spans.get(i);
			String oneSpanClass = oneSpan.attr("class");
			String oneSpanTitle = oneSpan.attr("title");
			String oneSpanTitleElements[] = oneSpanTitle.split("; ");
			String bboxElements[] = oneSpanTitleElements[0].split(" ");
			String bboxX1String = bboxElements[1];
			int bboxX1 = Integer.parseInt(bboxX1String);
			if(oneSpanClass.equals("ocr_line")&&(bboxX1>=25)&&(bboxX1<=80)){
				if(!oneSpan.text().contains("BALANCE")){
					namePriceList.add(oneSpan.text());
				}
			}
		}
		
//		System.out.println("span"+spans);
//		String texts = doc.text();
//		Element oneSpan = doc.select("span[title]").first();
//		String oneSpanAttr = oneSpan.attr("title");
//		System.out.println("oneSpan.attr(title): "+oneSpanAttr);
//		System.out.println("oneSpan.text(): "+oneSpan.text());
		return namePriceList;
	}
	
}
