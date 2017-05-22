/**
 * Mat2Image class: converting the captured Mat video to an visible image frame and send it
 * to QRDetect for interpretation
 * 
 * @author Taha Alhersh
 */
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/*
 * This class will change the Mat data read by opencv to image
 * Also it take advantage from the QRDetect class to detect and read the QR code in each frame
 * */
public class Mat2Image {
	
    Mat mat = new Mat();
    BufferedImage img;
    byte[] dat;
    QRDetect qr = new QRDetect();
    Map hintMap = new HashMap();
    String str="";
    public int frame_cntr;
		
    public Mat2Image() {
	    frame_cntr =0;
    }
    public Mat2Image(Mat mat) {
	frame_cntr =0;
        getSpace(mat);
    }
    public void getSpace(Mat mat) {
        this.mat = mat;
        int w = mat.cols(), h = mat.rows();
        if (dat == null || dat.length != w * h * 3)
            dat = new byte[w * h * 3];
        if (img == null || img.getWidth() != w || img.getHeight() != h || img.getType() != BufferedImage.TYPE_3BYTE_BGR)
                img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        }
        BufferedImage getImage(Mat mat){
            getSpace(mat);
            mat.get(0, 0, dat);
            //Change from BGR to RGB
            for (int i = 0; i < dat.length; i+= 3) {
                byte redChannel = dat[i];
                byte blueChannel = dat[i+2];

                dat[i] = blueChannel;
                dat[i+2] = redChannel;

            }
          
            img.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), dat);
            //Printing the QR content and the number of frame
            frame_cntr +=1;
            //Printing the QR content
            str = qr.readQRCode(img, hintMap);
            if(str != null && !str.isEmpty())
		// Print the content of the QR code and the correponding frame number
            	System.out.println(str + " in Frame : " + frame_cntr);
            else
            	System.out.println("Frame : " + frame_cntr); // General tracking for fame number			

        return img;
    }
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
