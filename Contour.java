import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator; 
import java.util.List; 
 
import org.opencv.core.Core; 
import org.opencv.core.CvType; 
import org.opencv.core.Mat; 
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.common.StringUtils; 

public class Contour {
 
	private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>(); 
	
	Size sz = new Size(5,5);

	readContour readCont = new readContour(); 
	
    public String process(Mat rgbaImage, String rowNo) { 
    	////////////////// Clean Process ///////////////////////////////////////////////////////

    	getContours(rgbaImage);
    	List<MatOfPoint2f> contours_side = new ArrayList<>();
    	String num = "";
    	String nums = "";
    	//System.out.println("Before="+mContours.size());
    	contours_side = checkInsideContour(mContours);
    	//System.out.println("After="+contours_side.size());
    	
    		//detecting selected contours
        	for (MatOfPoint2f contour: contours_side) {
        		
            	double w = contour.size().width;
            	double h = contour.size().height;
            	MatOfPoint contourN = new MatOfPoint();
            	if (contour.size().area() > 25 && (h/w >= 1.1) && (w < h) && contour.size().area() < 250){
            		Point p1,p2 = new Point();
            		
            		contour.convertTo(contourN,CvType.CV_32S);
           	    	Rect rect = Imgproc.boundingRect(contourN);
           	    	p1 = new Point(rect.x, rect.y);
           	    	p2 = new Point((rect.x + rect.width), (rect.y + rect.height));
           	    	//This line could be used to save the extracted contour as exported image
           	    	//Imgcodecs.imwrite("Frame:"+ contours_side.indexOf(contour)+".jpg", rgbaImage.submat(rect));
           	    	num = readCont.readContourNumber(mat2Img(rgbaImage.submat(rect)));

           	    	if (!num.isEmpty())
           	    		if (Character.isDigit(num.charAt(0)))
           	    			nums = nums + num.charAt(0);
            	}//if
            	if (nums.equals(rowNo)){
            		Rect rect = Imgproc.boundingRect(contourN);
            		Imgproc.rectangle(rgbaImage, new Point(rect.x - rect.width, rect.y), new Point((rect.x + rect.width), (rect.y + rect.height)), new Scalar(0, 255, 0),4);
               		//RotatedRect rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));
               		//drawRotatedRect(rgbaImage, rotatedRect, new Scalar(0, 255, 0), 2);
            	}
        	}//for MatOfPoint

        		
        	return nums;
        	
    	
    	
    	        
    }//Process method
 

	public void getContours(Mat inMat) {
		Size sz = new Size(5,5);
		Mat gray = new Mat();
		Mat hierarchy = new Mat();
		Mat outMat = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.GaussianBlur(inMat, outMat, sz, 0);
		Imgproc.cvtColor(outMat,gray,Imgproc.COLOR_BGR2GRAY);
		Imgproc.adaptiveThreshold(gray, gray, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 75, 10);
		Core.bitwise_not(outMat, outMat);
		Imgproc.findContours(gray, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
		mContours = contours; 
    } 	
	
	public List<MatOfPoint2f> checkInsideContour(List<MatOfPoint> contours){

		List<MatOfPoint2f> contours_side = new ArrayList<>();
		for (int i =0 ; i < contours.size(); i ++){
			Rect rect_i = Imgproc.boundingRect(contours.get(i));
        	double w = contours.get(i).size().width;
        	double h = contours.get(i).size().height;

			if(i+1 < contours.size() && contours.get(i).size().area() > 25 && (h/w >= 1.1) && (w < h) && contours.get(i).size().area() < 250){
				Rect rect_s = Imgproc.boundingRect(contours.get(i+1));

				if (Math.abs(rect_i.y - rect_s.y) < 10 &&  Math.abs(rect_i.x - rect_s.x) < 50 &&(((contours.get(i).size().area()/contours.get(i+1).size().area())>0.9) && (contours.get(i).size().area()/contours.get(i+1).size().area()) <1.5)){

					MatOfPoint2f newPoint1 = new MatOfPoint2f(contours.get(i).toArray());
					MatOfPoint2f newPoint2 = new MatOfPoint2f(contours.get(i+1).toArray());
					contours_side.add(newPoint2);
					contours_side.add(newPoint1);
					
					
				}//if
			}//size
			
		}//i
		return contours_side;
	}//checkInsideContour
	

    public static void drawRotatedRect(Mat image, RotatedRect rotatedRect, Scalar color, int thickness) {
        Point[] vertices = new Point[4];
        rotatedRect.points(vertices);
        MatOfPoint points = new MatOfPoint(vertices);
        Imgproc.drawContours(image, Arrays.asList(points), -1, color, thickness);
    }
    
    public static BufferedImage mat2Img(Mat in)
    {
        BufferedImage out;
        int h,w;
        
        w = in.cols();
        h = in.rows();
        
        byte[] data = new byte[h * w * (int)in.elemSize()];
        int type;
        in.get (0, 0, data);
        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(w, h, type);

        out.getRaster().setDataElements(0, 0, w, h, data);
        return out;
    }// mat2Img method   
}//contour class
