package com.gmail.aizperm.sign;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.apache.commons.io.IOUtils;

import com.gmail.aizperm.util.PNGArgs;
import com.gmail.aizperm.util.FontUtils;
import com.sixlegs.png.PngImage;

public class PhotoSignerImpl implements PhotoSigner
{

    @Override
    public byte[] sign(String locale, Integer type, byte[] data) throws IOException
    {
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        BufferedImage sourceImg = ImageIO.read(input);
        input.close();

        File titleFile = ImgResource.getRu(locale, type, sourceImg.getWidth());
        PngImage pngImage = new PngImage();
        BufferedImage titleImg = pngImage.read(titleFile);

        int sourceHeight = sourceImg.getHeight();
        int sourceWidth = sourceImg.getWidth();

        ColorModel titleColorModel = titleImg.getColorModel();
        WritableRaster titleRaster = titleImg.getRaster();

        int titleHeight = titleImg.getHeight();
        int titleWidth = titleImg.getWidth();

        int dY = sourceHeight - titleHeight;
        int dX = sourceWidth - titleWidth;
        if (dY < 0)
            dY = 0;
        if (dX < 0)
            dX = 0;

        for (int titleY = 0; titleY < titleHeight; titleY++)
        {
            for (int titleX = 0; titleX < titleWidth; titleX++)
            {
                Object point = titleRaster.getDataElements(titleX, titleY, null);
                int alpha = titleColorModel.getAlpha(point);

                if (alpha != 0)
                {
                    int x = dX + titleX;
                    int y = dY + titleY;
                    if (x < sourceWidth && y < sourceHeight)
                    {
                        int rgb = titleImg.getRGB(titleX, titleY);
                        sourceImg.setRGB(x, y, rgb);
                    }
                }
            }
        }

        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_METADATA);
        // jpgWriteParam.setCompressionQuality(1f);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MemoryCacheImageOutputStream outputImg = new MemoryCacheImageOutputStream(output);

        jpgWriter.setOutput(outputImg);
        IIOImage outputImage = new IIOImage(sourceImg, null, null);
        jpgWriter.write(null, outputImage, jpgWriteParam);
        jpgWriter.dispose();

        return output.toByteArray();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        byte[] data = IOUtils.toByteArray(new FileInputStream("d:/sDEct-zLSRA.jpg"));
        byte[] sign = new PhotoSignerImpl().sign(PNGArgs.LOCALE_RU, 1, data);
        IOUtils.write(sign, new FileOutputStream(new File("d:/1.jpg")));
    }

    @Override
    public byte[] sign(String text, String fontName, byte[] data) throws IOException
    {
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        BufferedImage sourceImg = ImageIO.read(input);
        input.close();

        int width = sourceImg.getWidth();
        int height = sourceImg.getHeight();

        Graphics2D g = (Graphics2D) sourceImg.getGraphics();
        int x = -1;
        int y = -1;
        int percent = 15;
        while (true)
        {
            Font font = FontUtils.getFont(fontName, (float) (height * percent / 100));

            g.setFont(font);
            g.setColor(Color.GREEN);

            FontMetrics fm = g.getFontMetrics();
            x = width - fm.stringWidth(text);
            y = height - fm.getDescent();
            if (x >= 0 || percent <= 1)
                break;
            percent--;
        }

        g.drawString(text, x, y);
        g.dispose();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MemoryCacheImageOutputStream outputImg = new MemoryCacheImageOutputStream(output);

        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_COPY_FROM_METADATA);

        jpgWriter.setOutput(outputImg);
        IIOImage outputImage = new IIOImage(sourceImg, null, null);
        jpgWriter.write(null, outputImage, jpgWriteParam);
        jpgWriter.dispose();

        return output.toByteArray();
    }

}
