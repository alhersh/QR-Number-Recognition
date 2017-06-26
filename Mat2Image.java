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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

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

/*
 * This class will change the Mat data read by opencv to image
 * Also it take advantage from the QRDetect class to detect and read the QR code in each frame
 * */
public class Mat2Image {
	
    Mat mat = new Mat();
    BufferedImage img;
    String QRCode;
    byte[] dat;
    QRDetect qr = new QRDetect();
	Map hintMap = new HashMap();
	String str="";
	public int frame_cntr;
		
    public Mat2Image() {
    	frame_cntr =0;
    	QRCode = "";
    	
    }
    public Mat2Image(Mat mat) {
    	frame_cntr =0;
    	QRCode = "";
        getSpace(mat);
    }
    public void setQRCode(String qr){
    	QRCode = qr;
    }
    public void getSpace(Mat mat) {
    	Mat2Image m2i = new Mat2Image();
        this.mat = mat;
        QRSelect qrs = new QRSelect();
		//***********************//
        //Set a QR Code to select//
		m2i.setQRCode("13-02-009"); // The passed QR Code is an example ...
		//***********************//
        int w = mat.cols(), h = mat.rows();
        if (dat == null || dat.length != w * h * 3)
            dat = new byte[w * h * 3];
        if (img == null || img.getWidth() != w || img.getHeight() != h || img.getType() != BufferedImage.TYPE_3BYTE_BGR)
                img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        str = qr.readQRCode(img, hintMap);
        //Tracking only the selected QR code
        qrs.selectQR(img, m2i.QRCode);
        if (str != "")
        	Imgproc.rectangle(mat, new Point(qrs.x, qrs.y), new Point(qrs.x - 15*qrs.size, qrs.y + 15*qrs.size),new Scalar(0, 255, 0));
        }//get space
    //

    //
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
            frame_cntr +=1;
            //Printing the QR content
            str = qr.readQRCode(img, hintMap);
            
            

            
            if(str != null && !str.isEmpty())
            	System.out.println(str + " in Frame : " + frame_cntr);
            //else
            	//System.out.println("Frame : " + frame_cntr);
            

        return img;
    }
        
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
