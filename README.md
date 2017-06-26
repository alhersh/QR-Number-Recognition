# QR-Reader
This project detect and recognize QR codes using the following libraries:

https://github.com/zxing/zxing

--Zxing core-3.3.0 and javase-3.3.0

http://opencv.org/

--OpenCV 3.2.0-0

OpenCV is used to capture the video and process it and use Zxing to recognize and decode the QR code.

# How to use it

This program assumes that you downloaded or built the previous 3 libraries from Zxing and OpenCV.

The main class is MainClass to view the captured video by caling the VideoCap method which uses OpenCV to capture the video as raw data, after that VideoCap will call the Mat2Image class to convert the raw data to visible image frame and then fetch it to the QRDetect class to recognize and read the QR code in the image frame. So the Sequence of the classes as follow:

MainClass

--------------> VideoCap 

--------------------------> Mat2Image 

--------------------------------------> QRDetect 


--------------------------------------> QRSelect 

-------------------------------------------------> Print the detected QR code with the corresponding frame number in the console

The method resposible of reading the QR-Code is (readQRCode(img, hintMap)) in the MatImage class which return String that can be used for further processing.

The class Mat2Image has the method "setQRCode("QR-Code")" which is responsible of setting a QR code to track. The passed QR code will be used by the method selectRQ from the QRSelect class to track only the desired QR code. The class Mat2Image will draw a green box on the desired QR code.

# License
Zxing and OpenCV licenses are provided
