import java.awt.image.BufferedImage;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.detector.FinderPattern;

public class QRSelect {
	
	float x,y,size;
	
	public QRSelect(){
	
		x =0;
		y=0;
		size=0;
	}
	public void selectQR(BufferedImage img, String QRCode){
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(img)));
        Reader reader = new QRCodeReader();
        Result result;
		try {
			result = reader.decode(binaryBitmap);
			if (result.getText().equalsIgnoreCase(QRCode)){
//				System.out.println("text: " + result.getText());
            ResultPoint[] resultPoints = result.getResultPoints();

            for (int i = 0; i < resultPoints.length; i++) {
                ResultPoint resultPoint = resultPoints[i];
                x = resultPoint.getX();
                y = resultPoint.getY();

                if (resultPoint instanceof FinderPattern)
                	size = ((FinderPattern) resultPoint).getEstimatedModuleSize();
            }//for
			}//if

            
		} catch (NotFoundException | ChecksumException | FormatException e) {
			// No QR frame
			//e.printStackTrace();
		}
	}//selectQR
	

}
