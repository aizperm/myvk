package com.gmail.aizperm.sign;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(sourceImg, "JPEG", output);
        output.close();
        return output.toByteArray();
    }

}
