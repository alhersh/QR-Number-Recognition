/**
 * Capturing Class, using the default capture input to get the video
 * 
 * @author Taha Alhersh
 * 
 */

import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

public class VideoCap {
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }// To use the opencv libraries 

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();
    
    VideoCap(){
        cap = new VideoCapture();
        cap.open(0);
    } 
 
    BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);
        return mat2Img.getImage(mat2Img.mat);
    }
}