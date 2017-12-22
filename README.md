# QR-Reader
This part of the project detect and recognize QR codes using the following libraries:

https://github.com/zxing/zxing

--Zxing core-3.3.0 and javase-3.3.0

http://opencv.org/

--OpenCV 3.2.0-0

OpenCV is used to capture the video and process it and use Zxing to recognize and decode the QR code.

# Contour Anlaysis is used for number detection
Contour detction was used to extract the suitable contours and then pass it to Tesseract to read the contents (Row number)

# Number-Recognition
For number recognition, this project will be using Tesseract-OCR libraries:

--tesseract 3.04.01

https://github.com/tesseract-ocr/tesseract/releases

Which is based on and compliled with Leptonica libraries:

--leptonica-1.74.4

http://www.leptonica.org/download.html


# How to use it

This program assumes that you downloaded or built the previous 3 libraries from Zxing and OpenCV.

The main class is MainClass to view the captured video by caling the VideoCap method which uses OpenCV to capture the video as raw data, after that VideoCap will call the Mat2Image class to convert the raw data to visible image frame and then fetch it to the QRDetect and Contour classes to recognize and read the QR code or Row number in the image frame. So the Sequence of the classes as follow:

MainClass

--------------> VideoCap 

--------------------------> Mat2Image 

--------------------------------------> Contour 

-------------------------------------------------> readContour

-----------------------------------------------------------> Print the detected number with the corresponding frame number in the console

--------------------------------------> QRDetect 


--------------------------------------> QRSelect 

-------------------------------------------------> Print the detected QR code with the corresponding frame number in the console

The method resposible of reading the QR-Code is (readQRCode(img, hintMap)) in the MatImage class which return String that can be used for further processing.

The class resposible of reading the Row-Number is readContour class which return String via readContourNumber method that can be used for further processing.

The class Mat2Image has the following methods which are responsible of setting a QR code or Row number to detect. The passed QR code / Row number will be used by other methods and classes to track and read only the desired QR code / Row number. The class Mat2Image will draw a green box on the desired QR code and Red box around the desired Row numbe:

----setQRCode

----setRowno

----setRowFlag

----setQrFlag


# License
Tesseract, Zxing and OpenCV licenses are provided
