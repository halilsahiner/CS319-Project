package client.GameModels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class XRectangle extends Rectangle {

    public static final String IMG0_URL = "assets/CubeFaces/1.png";
    public static final String IMG1_URL = "assets/CubeFaces/2.png";
    public static final String IMG2_URL = "assets/CubeFaces/3.png";
    public static final String IMG3_URL = "assets/CubeFaces/4.png";
    public static final String IMG4_URL = "assets/CubeFaces/5.png";
    public static final String IMG5_URL = "assets/CubeFaces/6.png";
    private static final int SIZE = 128;

    //private final String imgUrl;
    private final int id;

    private Image faceImage;
    private Image faceImagePad;

    public XRectangle(int id, Image faceImage, double size, double posX, double posY, double posZ) throws FileNotFoundException {
        super();

        this.id = id;
        //this.imgUrl = imgUrl;
        //this.faceImage = new Image(new FileInputStream(imgUrl),SIZE,SIZE,true,false);
        this.faceImage = faceImage;
        this.faceImagePad = addBorders(this.faceImage);
        setFill(new ImagePattern(faceImagePad));

        setHeight(size);
        setWidth(size);

        setTranslateX(posX);
        setTranslateY(posY);
        setTranslateZ(posZ);
    }

    public void hl(boolean isHL) throws FileNotFoundException {
        if (isHL) {
            setFill(new ImagePattern(changeImageColor()));
        }
        else {
            setFill(new ImagePattern(faceImagePad));
        }
    }

    public boolean isInFront() {
        Bounds boundsInScene = localToScene(getBoundsInLocal());
        long X = Math.round(boundsInScene.getMaxX());
        long Y = Math.round(boundsInScene.getMaxY());
        long Z = Math.round(boundsInScene.getMaxZ());

        return X < 0 || Y < 0 || Z < 0;
    }

    public int getID() {
        return this.id;
    }


    private Image addBorders( Image img ){
        BufferedImage buffImg =  SwingFXUtils.fromFXImage(img, null);

        int widthPad = (int) ( img.getWidth() * 6) / 100;
        int heightPad = (int) ( img.getHeight() * 6) / 100;
        BufferedImage newImage = new BufferedImage(buffImg.getWidth()+ widthPad, buffImg.getHeight() + heightPad, buffImg.getType());

        Graphics g = newImage.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, buffImg.getWidth() + widthPad, buffImg.getHeight() + heightPad);
        g.drawImage(buffImg, (int)widthPad/2, (int)heightPad/ 2 , null);
        g.dispose();

        Image tempImg = SwingFXUtils.toFXImage(newImage, null );
        ImageView tempView = new ImageView(tempImg);
        tempView.setFitWidth(img.getWidth());
        tempView.setPreserveRatio(true);

        return  tempView.snapshot(null, null);
    }

    private Image changeImageColor() {
        BufferedImage base  =  SwingFXUtils.fromFXImage(this.faceImage, null);
            try {

            BufferedImage overlay = SwingFXUtils.fromFXImage(new Image(new FileInputStream("assets/CubeFaces/selected.png")),null);

            Graphics2D g2d = base.createGraphics();
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.3f));
            int x = (base.getWidth() - overlay.getWidth()) / 2;
            int y = (base.getHeight() - overlay.getHeight()) / 2;
            g2d.drawImage(overlay, x, y, null);
            g2d.dispose();


        } catch (IOException e) {
            e.printStackTrace();
        }
        Image tempImg = SwingFXUtils.toFXImage(base, null);
        ImageView temp = new ImageView(tempImg);
        temp.setFitWidth(this.faceImage.getWidth());
        temp.setPreserveRatio(true);
        return temp.snapshot(null, null);
    }

    public Image getFaceImage() {
        return this.faceImage;
    }

    public void setFaceImage(Image img){
        this.faceImage = img;
        this.faceImagePad = addBorders(this.faceImage);

    }

}