/**
 * Reading the QR code inside the image frame
 * 
 * @author Taha Alhersh
 */
import java.awt.image.BufferedImage;
import java.io.*;
import net.sourceforge.tess4j.*;

public class readContour {
	
	public static String readContourNumber(BufferedImage bufImg) {
		ITesseract instance = new Tesseract();
		//Make Tess4j recognize only digits
		instance.setTessVariable("tessedit_char_whitelist", "0123456789");

		try{

			String results = instance.doOCR(bufImg);
			
			return results;
		}
		catch (TesseractException e){System.err.println(e.getMessage());}
		
		return "";
	}
}