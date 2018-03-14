package com.gmail.aizperm.vk;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.gmail.aizperm.sign.PhotoSignerImpl;
import com.gmail.aizperm.util.FontUtils;

public class AddText
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        byte[] data = IOUtils.toByteArray(new FileInputStream("d:/верочка2.jpg"));

        String text = "Привет мир";

        PhotoSignerImpl signer = new PhotoSignerImpl();        
        byte[] sign = signer.sign(text, FontUtils.MAYA, data);
        IOUtils.write(sign, new FileOutputStream(new File("d:/1.jpg")));
    }

    public static void main1(String[] args) throws MalformedURLException, IOException, FontFormatException
    {
        String text = "Привет мир";

        int width = 600;
        int heigth = 300;
        BufferedImage image = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);

        Font font = FontUtils.getFont(FontUtils.MAYA, (float) (heigth * 15 / 100));

        Graphics2D g = (Graphics2D) image.getGraphics();

        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //
        // g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //
        // FontRenderContext frc = g.getFontRenderContext();
        //
        // TextLayout textTl = new TextLayout(text, font, frc);
        //
        // AffineTransform transform = new AffineTransform();
        // Shape outline = textTl.getOutline(null);

        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, width, heigth);

        g.setFont(font);
        g.setColor(Color.GREEN);

        FontMetrics fm = g.getFontMetrics();
        int x = width - fm.stringWidth(text);
        int y = heigth - fm.getDescent();

        g.drawString(text, x, y);
        g.dispose();

        ImageIO.write(image, "png", new File("src/test/java/test.png"));
    }
}
