package components;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;

public class Imagecon extends ImageIcon {
    
    public Imagecon() {
        super();
    }
    
    public Imagecon(byte[] imageData) {
        super(imageData);
    }
    
    public Imagecon(Image image) {
        super(image);
    }
    
    public Imagecon(URL image) {
        super(image);
    }
    
    public Imagecon(String filename) {
        super(filename);
    }
    
    

    public ImageIcon getScaledIcon(int width, int height) {
        Image scaled = this.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    
 
    public ImageIcon getScaledIconToFit(Dimension maxDimension) {
        double widthRatio = maxDimension.getWidth() / this.getIconWidth();
        double heightRatio = maxDimension.getHeight() / this.getIconHeight();
        double ratio = Math.min(widthRatio, heightRatio);
        
        int newWidth = (int)(this.getIconWidth() * ratio);
        int newHeight = (int)(this.getIconHeight() * ratio);
        
        return getScaledIcon(newWidth, newHeight);
    }
    
  
    public BufferedImage toBufferedImage() {
        Image image = this.getImage();
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        BufferedImage bufferedImage = new BufferedImage(
            image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        
        bufferedImage.getGraphics().drawImage(image, 0, 0, null);
        return bufferedImage;
    }
    
    
    public boolean isValidImage() {
        return this.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE;
    }
    
    
    public double getAspectRatio() {
        return (double)this.getIconWidth() / (double)this.getIconHeight();
    }
    
    
    public ImageIcon copy() {
        return new ImageIcon(this.getImage());
    }
    

    public ImageIcon rotate(double degrees) {
        BufferedImage bufferedImage = this.toBufferedImage();
        java.awt.geom.AffineTransform transform = new java.awt.geom.AffineTransform();
        transform.rotate(
            Math.toRadians(degrees), 
            bufferedImage.getWidth() / 2, 
            bufferedImage.getHeight() / 2
        );
        
        java.awt.image.AffineTransformOp op = new java.awt.image.AffineTransformOp(
            transform, java.awt.image.AffineTransformOp.TYPE_BILINEAR);
        
        return new ImageIcon(op.filter(bufferedImage, null));
    }
}