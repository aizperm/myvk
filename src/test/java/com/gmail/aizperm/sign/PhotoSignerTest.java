package com.gmail.aizperm.sign;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.sixlegs.png.PngImage;

public class PhotoSignerTest
{

    @Test
    public void testPrint() throws IOException
    {
        PngImage pngImage = new PngImage();
        BufferedImage img = pngImage.read(ImgResource.getRu());
        Assert.assertNotNull(img);

        ColorModel colorModel = img.getColorModel();
        WritableRaster raster = img.getRaster();

        int width = img.getWidth();
        int height = img.getHeight();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                Object point = raster.getDataElements(x, y, null);
                int alpha = colorModel.getAlpha(point);
                if (alpha != 0)
                    System.out.print("X");
                else System.out.print(" ");
            }
            System.out.println("");
        }
    }

    @Test
    public void testSign() throws FileNotFoundException, IOException
    {
        byte[] data = IOUtils.toByteArray(new FileInputStream("d:/барахло/фото_тест/4annXyBi9qs.jpg"));

        byte[] sign = new PhotoSignerImpl().sign(data);

        IOUtils.write(sign, new FileOutputStream("d:/1.jpg"));

    }

}
