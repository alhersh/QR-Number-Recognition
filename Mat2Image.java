/**
 * Mat2Image class: converting the captured Mat video to an visible image frame and send it
 * to QRDetect for interpretation
 * 
 * @author Taha Alhersh
 */
import java.util.List;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.MatOfPoint;

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
 * Contour class is called to detect and read row number
 * */
public class Mat2Image {
	
    Mat mat = new Mat();
    BufferedImage img;
    String QRCode;
    String RowNo;
    byte[] dat;
    QRDetect qr = new QRDetect();
    QRSelect qrs = new QRSelect();
    Contour contour = new Contour();
	Map hintMap = new HashMap();
	String outStrQR="",inStrQR="";
	String inStrRow="";
    String inRowNo = "";
    String outRowNo = "";
    boolean rowFlag;
    boolean qrFlag;
	public int frame_cntr;
		
    public Mat2Image() {
    	frame_cntr =0;
    	QRCode = "";
    	RowNo = "";
    	rowFlag = false;
    	qrFlag = false;
    }
    
    public Mat2Image(Mat mat) {
    	frame_cntr =0;
    	QRCode = "";
    	RowNo = "";
        getSpace(mat);
    }
    public void setQRCode(String qr){
    	QRCode = qr;
    }
    public void setRowno(String no){
    	RowNo = no;
    }
    public void setRowFlag(boolean flag){
    	rowFlag = flag;
    }
    public void setQrFlag(boolean flag){
    	qrFlag = flag;
    }
    public void getSpace(Mat mat) {
    	
    	/*
    	 * Setting setRowFlag and setQrFlag as true will detect and select both of them online. Those flags can be set
    	 * as prefered. The default is flase for both of them
    	 * */
    	
    	Mat2Image m2i = new Mat2Image();
        this.mat = mat;

		//***********************//
        //Set a QR Code to select//
        inStrQR = "13-02-009";
		m2i.setQRCode(inStrQR); // The passed QR Code is an example ...
		//***********************//
		
        int w = mat.cols(), h = mat.rows();
        if (dat == null || dat.length != w * h * 3)
            dat = new byte[w * h * 3];
        if (img == null || img.getWidth() != w || img.getHeight() != h || img.getType() != BufferedImage.TYPE_3BYTE_BGR)
                img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        
        
        //Tracking only the selected QR code
        setQrFlag(true);
        if (qrFlag){
        	outStrQR = qr.readQRCode(img, hintMap);
        	if (inStrQR.equals(outStrQR))
        		qrs.selectQR(img, m2i.QRCode);
        }
        	
        //*************************
		//***********************//
        //Set a Row No to select and Track//
        inRowNo = "15";
        //calling contour function to extract contours for further processing number extraction and classification
        setRowFlag(true);
        if (rowFlag)
        	outRowNo = contour.process(mat, inRowNo);
        	if (outRowNo.equals(inRowNo))
        		setRowno(outRowNo);
        //**************************

        
        //System.out.println(contour.getContours().size());
        //card.drawContour(mat, contour.getContours().get(3), color);
        if (outStrQR.equals(inStrQR)){
        	Imgproc.rectangle(mat, new Point(qrs.x, qrs.y), new Point(qrs.x - 15*qrs.size, qrs.y + 15*qrs.size),new Scalar(0, 255, 0),4);
        	
        } // If found QR in the frame
		 	
      }//get space

    
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
            //outStrQR = qr.readQRCode(img, hintMap);
            
             if(outStrQR != null && !outStrQR.isEmpty() && qrFlag)
            	 //if (outStrQR.equals(QRCode))
            		 System.out.println("QR code : "+outStrQR + " in Frame : " + frame_cntr);

             
             if(outRowNo != null && !outRowNo.isEmpty() && outRowNo.equals(RowNo) && rowFlag)
            	System.out.println("Row number : "+ outRowNo + " in Frame : " + frame_cntr);
             
            

        return img;
    }
        
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
