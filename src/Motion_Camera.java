import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * Created by jugs on 10/11/16.
 */
public class Motion_Camera {

    public static void main(String[] args)  {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        JFrame jframe = new JFrame("Duke Apps");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setSize(640, 480);
        jframe.setVisible(true);

        Mat frame = new Mat();
        Mat prevFrame = new Mat();
        Mat frameClone;
        VideoCapture camera = new VideoCapture(0);//capturing camera frames

        Boolean pass = false;
        while (true) {
            if (camera.read(frame)) {
                //DIP BLOCK
                System.out.println(frame);


                frameClone = frame.clone();
                if(pass)
                    Core.absdiff(frameClone, prevFrame, frame);

                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                Imgproc.threshold(frame, frame, 30, 255, Imgproc.THRESH_BINARY);

                ImageIcon image = new ImageIcon(createBufferedImage(frame));
                vidpanel.setIcon(image);
                vidpanel.repaint();

                prevFrame = frameClone;
                pass = true;
                //DIP BLOCK
            }//if
        }//while
    }

    public static BufferedImage createBufferedImage(Mat mat) {
        int type = 0;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (mat.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        } else {
            return null;
        }

        BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        mat.get(0, 0, data);
        mat.release();

        return image;
    }//createBufferedImage
}//class