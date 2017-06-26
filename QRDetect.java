/**
 * Reading the QR code inside the image frame
 * 
 * @author Taha Alhersh
 */
import java.awt.image.BufferedImage;
import java.util.Map;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


public class QRDetect {
	
	public static String readQRCode(BufferedImage bufImg, Map hintMap) {

		try{
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer( new BufferedImageLuminanceSource(bufImg)));
			Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
			return qrCodeResult.getText();
		}
		catch (NotFoundException nfe){/*System.out.println("Waiting QR Code");*/}
		return "";
	}
}
